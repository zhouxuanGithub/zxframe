package zxframe.jpa.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import zxframe.config.ZxFrameConfig;
import zxframe.jpa.ex.JpaRuntimeException;

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
		        datasource.setTestOnBorrow(getDatasourceConfig(cmap, "testOnBorrow", key).equals("true")?true:false);    
		        datasource.setTestOnReturn(getDatasourceConfig(cmap, "testOnReturn", key).equals("true")?true:false);    
		        datasource.setTestWhileIdle(getDatasourceConfig(cmap, "testWhileIdle", key).equals("true")?true:false);    
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
		        	ArrayList<DataSource> arrayList = rDataSource.get(key);
		        	if(arrayList==null) {
		        		arrayList=new ArrayList<DataSource>();
		        		rDataSource.put(key, arrayList);
		        	}
		        	arrayList.add(datasource);
		        }
		        if(pattern.indexOf("w")!=-1) {
		        	if(wDataSource.get(key)==null) {
		        		wDataSource.put(key, datasource);
		        	}else {
		        		throw new JpaRuntimeException("已经存在写的数据源，只允许配置一个相同dsnane的写数据源："+key);
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
			throw new RuntimeException("数据源"+dsname+"配置错误，缺少："+key);
		}
		return value;
	}
	/**
	 * 获得当前线程的写数据源
	 * @return
	 */
	public static Connection getCurrentWConnection(String dsname) {
		if(ZxFrameConfig.showlog) {
			logger.info("use write dsname:"+dsname);
		}
		try {
			String transactionId = Thread.currentThread().getName();
			ConcurrentMap<String,Connection> map = DataSourceManager.uwwcMap.get(transactionId);
			if(map==null) {
				map=new ConcurrentHashMap<String,Connection>();
				DataSourceManager.uwwcMap.put(transactionId, map);
			}
			Connection connection = map.get(dsname);
			if(connection==null) {
				connection = DataSourceManager.wDataSource.get(dsname).getConnection();
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
	 */
	public static Connection getRConnection(String dsname) {
		if(ZxFrameConfig.showlog) {
			logger.info("use read dsname:"+dsname);
		}
		try {
			ArrayList<DataSource> list = rDataSource.get(dsname);
			return list.get(new Random().nextInt(list.size())).getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
