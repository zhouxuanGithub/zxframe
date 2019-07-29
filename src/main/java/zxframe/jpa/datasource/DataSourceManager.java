package zxframe.jpa.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import zxframe.aop.ServiceAspect;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.ex.JpaRuntimeException;
import zxframe.util.JsonUtil;
import zxframe.util.MathUtil;

public class DataSourceManager {
	private static Logger logger = LoggerFactory.getLogger(DataSourceManager.class);
	//写数据源
	public static ConcurrentMap<String, DataSource> wDataSource=new ConcurrentHashMap<String, DataSource>();
	//读数据源
	public static ConcurrentMap<String, ArrayList<DataSource>> rDataSource=new ConcurrentHashMap<String, ArrayList<DataSource>>();
	//被熔断的读数据源
	public static ConcurrentMap<String, ArrayList<DataSource>> errRDataSource=new ConcurrentHashMap<String, ArrayList<DataSource>>();
	//当前使用中的写数据源
	public static ConcurrentMap<String, ConcurrentMap<String,Connection>> uwwcMap=new ConcurrentHashMap<String, ConcurrentMap<String,Connection>>();
	//初始化数据源
	public static void init() {
		Iterator<String> iterator = ZxFrameConfig.datasources.keySet().iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			ArrayList<ConcurrentHashMap<String, String>> dsList = ZxFrameConfig.datasources.get(key);
			for (int i = 0; i < dsList.size(); i++) {
				ConcurrentHashMap<String, String> cmap = dsList.get(i);
				DruidDataSource datasource = new DruidDataSource();    
		        datasource.setUrl(getDatasourceConfig(cmap, "url", key));    
		        datasource.setUsername(getDatasourceConfig(cmap, "username", key));    
		        datasource.setPassword(getDatasourceConfig(cmap, "password", key));    
		        datasource.setDriverClassName(getDatasourceConfig(cmap, "driver-class-name", key));    
		            
		        //configuration    
		        datasource.setInitialSize(Integer.parseInt(getDatasourceConfig(cmap, "initialSize", key)));    
		        datasource.setMinIdle(Integer.parseInt(getDatasourceConfig(cmap, "minIdle", key)));    
		        datasource.setMaxActive(Integer.parseInt(getDatasourceConfig(cmap, "maxActive", key)));
		        datasource.setMaxWait(Integer.parseInt(getDatasourceConfig(cmap, "maxWait", key)));//获取连接时最大等待时间，单位毫秒
		        //用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
		        datasource.setValidationQuery(getDatasourceConfig(cmap, "validationQuery", key));
		        //申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
		        datasource.setTestOnBorrow(getDatasourceConfig(cmap, "testOnBorrow", key).equals("true")?true:false);
		        //归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
		        datasource.setTestOnReturn(getDatasourceConfig(cmap, "testOnReturn", key).equals("true")?true:false);
		        //建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
		        datasource.setTestWhileIdle(getDatasourceConfig(cmap, "testWhileIdle", key).equals("true")?true:false);
		        //有两个含义：
		        //1) Destroy线程会检测连接的间隔时间，如果连接空闲时间大于等于minEvictableIdleTimeMillis则关闭物理连接。
		        //2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
		        datasource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(getDatasourceConfig(cmap, "timeBetweenEvictionRunsMillis", key)));
		        //连接保持空闲而不被驱逐的最小时间
		        datasource.setMinEvictableIdleTimeMillis(Integer.parseInt(getDatasourceConfig(cmap, "minEvictableIdleTimeMillis", key)));
		        try {    
		            datasource.setFilters(getDatasourceConfig(cmap, "filters", key));    
		        } catch (SQLException e) {    
		            logger.error("druid configuration initialization filter", e);    
		        }   
		        DruidPooledConnection con=null;
		        String pattern = getDatasourceConfig(cmap, "pattern", key);
		        try {
		        	logger.info("dataSource "+key+"[pattern:"+pattern+"] init :"+getDatasourceConfig(cmap, "url", key));
		        	con = datasource.getConnection();
				} catch (Exception e) {
					throw new JpaRuntimeException(e);
				}finally {
					try {
						if (con != null) {
							con.close();
							con = null;
						}
					} catch (SQLException e) {
						throw new JpaRuntimeException(e);
					}
				}
		        if(pattern.indexOf("r")!=-1) {
		        	String[] keysplit = key.split(",");
		        	for (int j = 0; j < keysplit.length; j++) {
		        		String keysplitValue = keysplit[j];//数据源支持逗号分割
		        		ArrayList<DataSource> arrayList = rDataSource.get(keysplitValue);
			        	if(arrayList==null) {
			        		arrayList=new ArrayList<DataSource>();
			        		rDataSource.put(keysplitValue, arrayList);
			        	}
			        	arrayList.add(datasource);
					}
		        }
		        if(pattern.indexOf("w")!=-1) {
		        	String[] keysplit = key.split(",");
		        	for (int j = 0; j < keysplit.length; j++) {
		        		String keysplitValue = keysplit[j];
		        		if(wDataSource.get(keysplitValue)==null) {
			        		wDataSource.put(keysplitValue, datasource);
			        	}else {
			        		logger.error("已经存在写的数据源，只允许配置一个相同dsnane的写数据源："+key);
			        	}
		        	}
		        }
			}
		}
	}
	
	private static String getDatasourceConfig(ConcurrentHashMap<String, String> cmap,String key,String dsname) {
		String value=cmap.get(key);
		if(value==null) {
			value=ZxFrameConfig.common.get(key);
		}
		if(value==null) {
			//给默认值
			if(key.equals("initialSize")) {
				return "1";
			}else if(key.equals("minIdle")){
				return "10";
			}else if(key.equals("maxActive")){
				return "200";
			}else if(key.equals("maxWait")){
				return "2000";
			}else if(key.equals("testOnBorrow")){
				return "false";
			}else if(key.equals("testOnReturn")){
				return "false";
			}else if(key.equals("testWhileIdle")){
				return "true";
			}else if(key.equals("validationQuery")){
				return "select 'x'";
			}else if(key.equals("timeBetweenEvictionRunsMillis")){
				return String.valueOf(30*1000);
			}else if(key.equals("minEvictableIdleTimeMillis")){
				return String.valueOf(30*60*1000);
			}else if(key.equals("filters")){
				return "stat";
			}else {
				throw new RuntimeException("数据源"+dsname+"配置错误，缺少："+key);
			}
		}
		return value;
	}
	/**
	 * 获得当前线程的写数据源
	 * @return
	 */
	public static Connection getCurrentWConnection(String dsname) {
//		if(ZxFrameConfig.showlog) {
//			logger.info("use write dsname:"+dsname);
//		}
		try {
			String transactionId = Thread.currentThread().getName();
			ConcurrentMap<String,Connection> map = DataSourceManager.uwwcMap.get(transactionId);
			if(map==null) {
				throw new JpaRuntimeException("请将对数据库的增删改操作放在Service层的指定方法名前缀中【"+JsonUtil.obj2Json(ServiceAspect.requiredTx)+"】");
//				map=new ConcurrentHashMap<String,Connection>();
//				DataSourceManager.uwwcMap.put(transactionId, map);
			}
			Connection connection = map.get(dsname);
			if(connection==null) {
				connection = DataSourceManager.wDataSource.get(dsname).getConnection();
				if(connection==null) {
					throw new JpaRuntimeException(dsname+"写数据源为空，未配置");
				}
				connection.setAutoCommit(false);// 更改JDBC事务的默认提交方式 
				map.put(dsname, connection);
			}
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得的读数据源
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getRConnection(String dsname) throws SQLException {
//		if(ZxFrameConfig.showlog) {
//			logger.info("use read dsname:"+dsname);
//		}
		try {
			ArrayList<DataSource> list = rDataSource.get(dsname);
			if(list==null) {
				throw new JpaRuntimeException(dsname+"读数据源为空，未配置");
			}
			return list.get(MathUtil.nextInt(list.size())).getConnection();
		} catch (SQLException e) {
			throw e;
		}
	}
}
