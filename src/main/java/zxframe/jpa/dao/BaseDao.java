package zxframe.jpa.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import zxframe.cache.mgr.CacheManager;
import zxframe.cache.mgr.CacheModelManager;
import zxframe.cache.transaction.CacheTransaction;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.annotation.Id;
import zxframe.jpa.datasource.DataSourceManager;
import zxframe.jpa.ex.DataExpiredException;
import zxframe.jpa.ex.JpaRuntimeException;
import zxframe.jpa.model.DataModel;
import zxframe.jpa.util.SQLParsing;
import zxframe.util.JsonUtil;


@Repository
public class BaseDao {
	private Logger logger = LoggerFactory.getLogger(BaseDao.class);  
	@Resource
	private CacheTransaction ct;
	@Resource
	private CacheManager cacheManager;
	/**
	 * 增：保存对象
	 * @param obj 需要保存的对象
	 * @return id
	 */
	public Serializable save(Object obj) {
		Serializable id = null;
		try {
			String group=obj.getClass().getName();
			DataModel cm = CacheModelManager.getDataModelByGroup(group);
			//存值
			ArrayList<Object> argsList=new ArrayList<Object>();
			StringBuffer sql=new StringBuffer();
			sql.append("insert into ").append(obj.getClass().getSimpleName()).append("(");
			Id ida = CacheModelManager.cacheIdAnnotation.get(group);
			Field idField = CacheModelManager.cacheIdFieldMap.get(group);
			Map<String, Field> fieldMap = CacheModelManager.cacheFieldsMap.get(group);
			Iterator<String> iterator = fieldMap.keySet().iterator();
			int length=0;
			while(iterator.hasNext()) {
				Field field = fieldMap.get(iterator.next());
				if(idField!=null&&ida!=null&&field.getName().equals(idField.getName())) {
					if(ida.auto()) {
						continue;
					}else {
						id=(Serializable) idField.get(obj);
					}
				}
				if(length++>0) {
					sql.append(",");
				}
				sql.append(field.getName());
				argsList.add(field.get(obj));
			}
			sql.append(") values (");
			length=argsList.size();
			Object[] args=new Object[length];
			for(int i=0;i<length;i++){
				args[i]=argsList.get(i);
				if(i>0) {
					sql.append(",");
				}
				sql.append("?");
			}
			sql.append(")");
			execute(SQLParsing.getDSName(null, obj.getClass(),null),sql.toString(),cm,args);
			//缓存事务操作
			if(cm!=null&&id!=null) {
				ct.put(cm, id.toString(), obj);
			}
		} catch (Exception e) {
			throw new JpaRuntimeException(e);
		}
		return id;
	}
	/**
	 * 查：获取对象
	 * @param clas 对象class
	 * @param id 主键
	 */
	public <T> T get(Class<T> clas, Serializable id) {
		String group=clas.getName();
		T obj =null;
		//去事务或者缓存中去查
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm!=null) {
			obj =(T) ct.get(group, id.toString());
		}
		if(obj==null) {
			//尝试去数据库查
			String sql="select * from "+clas.getSimpleName()+" where  "+CacheModelManager.cacheIdFieldMap.get(group).getName()+" = ? ";
			obj=get(clas,sql,cm,id);
			//缓存事务操作
			if(cm!=null&&obj!=null) {
				ct.put(cm, id.toString(), obj);
			}
		}
		return obj;
	}
	/**
	 * 查询对象
	 * @param group group
	 * @param args sql参数
	 * @return
	 */
	public Object get(String group,Object... args) {
		return get(group,null,args);
	}
	/**
	 * 查询对象
	 * @param group group
	 * @param Map sql增强部分替换
	 * @param args sql参数
	 * @return
	 */
	public Object get(String group,Map<String,String> map,Object... args) {
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm==null) {
			throw new JpaRuntimeException("请配置数据模型，可能你忘了加@DataMapper注解，group:"+group);
		}
		return get(cm.getResultClass(),SQLParsing.replaceSQL(cm.getSql(),map),cm,args);
	}
	private <T> T get(Class<T> clas, String sql,DataModel cacheModel,Object... args) {
		List<T> list = getList(clas, sql,cacheModel,args);
		if(list==null||list.size()==0) {
			return null;
		}else {
			return list.get(0);
		}
	}
	/**
	 * 删除
	 * @param clas
	 * @param id
	 */
	public void delete(Class clas, Serializable id) {
		String group=clas.getName();
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		String sql = "delete from "+clas.getSimpleName()+" where "+CacheModelManager.cacheIdFieldMap.get(group).getName()+" = ?";
		//执行删除
		execute(SQLParsing.getDSName(null, clas,null),sql,id);
		if(cm!=null) {
			ct.remove(group, id.toString());
		}
	}
	/**
	 * 改：更新数据
	 * @param obj 需要更新的对象
	 * @param fields 需要更新的字段，不传则全更新
	 */
	public void update(Object obj,String... fields) {
		if(obj==null) {
			logger.error("更新错误，更新对象不能为空！");
			return;
		}
		String group=obj.getClass().getName();
		Serializable id =null;
		try {
			int length=fields.length;
			Map<String, Field> fieldMap = CacheModelManager.cacheFieldsMap.get(group);
			if(length<=0) {
				throw new JpaRuntimeException("更新提示：请填写更新字段，指定想更新的字段！列如：update(user,\"name\",\"age\");");
				//没传参就进行全部更新
//				length=fieldMap.size();
//				fields=new String[length];
//				Iterator<String> iterator = fieldMap.keySet().iterator();
//				int i=0;
//				while(iterator.hasNext()) {
//					fields[i++]=iterator.next();
//				}
			}
			//执行更新
			ArrayList<Object> argsList=new ArrayList<Object>();
			Field idField = CacheModelManager.cacheIdFieldMap.get(group);
			if(idField==null) {
				throw new JpaRuntimeException("请给对象添加主键直接后可执行更新："+group);
			}
			Field versionField=CacheModelManager.cacheIdVersionMap.get(group);
			id = (Serializable) idField.get(obj);
			DataModel cm = CacheModelManager.getDataModelByGroup(group);
			StringBuffer sql=new StringBuffer();
			sql.append("update ").append(obj.getClass().getSimpleName()).append(" set ");
			for (int i = 0; i < length; i++) {
				String field = fields[i];
				if((!field.equals(idField))&&(!field.equals(versionField))) {
					sql.append(field).append(" = ?");
					argsList.add(fieldMap.get(field).get(obj));
					if(i+1!=length) {
						sql.append(" , ");
					}
				}
			}
			//乐观锁更新
			int cversion=0;
			if(versionField!=null) {
				sql.append(" , ");
				sql.append(versionField.getName()).append(" = ? ");
				cversion=1+Integer.parseInt(versionField.get(obj).toString());
				argsList.add(cversion);
			}
			sql.append(" where ").append(idField.getName()).append(" = ? ");
			argsList.add(idField.get(obj));
			if(versionField!=null) {
				//乐观锁判断
				sql.append(" and ").append(versionField.getName()).append(" = ? ");
				argsList.add(versionField.get(obj));
			}
			length=argsList.size();
			Object[] args=new Object[length];
			for(int i=0;i<length;i++){  
				args[i]=argsList.get(i);  
			}
			//执行
			int execute = (int) execute(SQLParsing.getDSName(null,obj.getClass(),null),sql.toString(),null,args);
			if(execute<1) {
				//版本控制出现问题
				logger.warn("StaleObjectStateException :"+JsonUtil.obj2Json(obj));
				ct.remove(group, id.toString());
				throw new DataExpiredException("数据 version 已过期。");
			}
			//更新成功，更新版本
			if(versionField!=null) {
				versionField.set(obj, cversion);
			}
			if(cm!=null) {
				ct.put(cm, id.toString(), obj);
			}
		} catch (Exception e) {
			ct.remove(group, id.toString());
			throw new JpaRuntimeException(e);
		}
	}
	/**
	 * 查：根据sql查询对象集合
	 * @param group group
	 * @param args 参数
	 */
	public List getList(String group, Object... args) {
		return getList(group,null,args);
	}
	/**
	 * 查：根据sql查询对象集合
	 * @param group group
	 * @param map sql增强部分替换
	 * @param args 参数
	 */
	public List getList(String group,Map<String,String> map, Object... args) {
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm==null) {
			throw new JpaRuntimeException("请配置数据模型[getList]，可能你忘了加@DataMapper注解，group:"+group);
		}
		return getList(cm.getResultClass(),SQLParsing.replaceSQL(cm.getSql(),map), cm ,args);
	}
	
	private <T> List<T> getList(Class<T> clas,String sql,DataModel cacheModel, Object... args) {
		Connection con =null;
		ResultSet rs=null;
		try {
			String cid = null;
			ArrayList<T> list = new ArrayList<T>();
			String group=clas.getName();
			if(cacheModel!=null&&cacheModel.isQueryCache()) {
				//查询
				cid = CacheManager.getQueryKey(sql, args);
				list=(ArrayList<T>) ct.get(cacheModel.getGroup(), cid);
				if(list!=null) {
					return list;
				}
			}
			//计算数据源
			String dsname=SQLParsing.getDSName(cacheModel!=null?cacheModel.getDsClass():null,clas,sql);
			//打开连接
			con = DataSourceManager.getRConnection(dsname);
			//去数据库获得数据
			rs = getResult(con,sql,args);
			Map<String, Field> fieldMap =CacheModelManager.cacheFieldsMap.get(group);
			Iterator<String> iterator=null;
			Field field=null;
			list = new ArrayList<T>();//此处创建对象是为了能让本地和远程缓存存空集合
			while(rs.next()){
				if(fieldMap==null) {
					//系统对象装载 string intiger date ..等
					list.add((T)rs.getObject(1));
				}else {
					T o = clas.newInstance();
					iterator = fieldMap.keySet().iterator();
					while(iterator.hasNext()) {
						field = fieldMap.get(iterator.next());
						field.set(o, rs.getObject(field.getName()));
					}
					list.add(o);
				}
			}
			//存放查询结果
			if(list!=null&&cacheModel!=null&&cacheModel.isQueryCache()) {
				ct.put(cacheModel, cid, list);
			}
			//执行后清理指定组缓存
			try {
				if(cacheModel!=null&&cacheModel.getFlushOnExecute()!=null) {
					List l = cacheModel.getFlushOnExecute();
					for (int i = 0; i < l.size(); i++) {
						Object o = l.get(i);
						if(o instanceof Class) {
							cacheManager.remove(((Class) o).getName());
						}else {
							cacheManager.remove(o.toString());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		} catch (Exception e) {
			throw new JpaRuntimeException(e);
		}finally {
			closeAll(con,null, rs);
		}
	}
	/**
	 * 数据更新(增删改)
	 * @param group 数据模型组
	 * @param args 赋值的参数集合
	 * @return 执行状态
	 */
	public Object execute(String group, Object... args) {
		return execute(group,null,args);
	}
	/**
	 * 数据更新(增删改)
	 * @param group 数据模型组
	 * @param map sql增强部分替换
	 * @param args 赋值的参数集合
	 * @return 执行状态
	 */
	public Object execute(String group,Map<String,String> map, Object... args) {
		DataModel cm = CacheModelManager.getDataModelByGroup(group);
		if(cm==null) {
			throw new JpaRuntimeException("请配置数据模型[execute]，可能你忘了加@DataMapper注解，group:"+group);
		}
		String sql=SQLParsing.replaceSQL(cm.getSql(),map);
		return execute(SQLParsing.getDSName(cm.getDsClass(),cm.getResultClass(),sql),sql,cm,args);
	}
	/**
	 * 数据更新(增删改)
	 * @param dsname 数据源名
	 * @param sql 数据模型组
	 * @param cm 数据模型
	 * @param args 赋值的参数集合
	 * @return 执行状态
	 */
	private Object execute(String dsname,String sql,DataModel cm, Object... args) {
		if(ZxFrameConfig.showsql) {
			logger.info(sql+" args "+JsonUtil.obj2Json(args));
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 1.打开连接
			con = DataSourceManager.getCurrentWConnection(dsname);
			if(con==null) {
				throw new JpaRuntimeException("未开启事务，请使用service层操作数据，方法名前缀请使用配置值");
			}
			// 2.获取语句对象
			ps = con.prepareStatement(sql);
			// 3.赋值：
			setValues(ps, args);
			// 4.执行更新(向数据库发送指令)
			int count= ps.executeUpdate();
			//执行后清理指定组缓存
			try {
				if(cm!=null&&cm.getFlushOnExecute()!=null) {
					List l = cm.getFlushOnExecute();
					for (int i = 0; i < l.size(); i++) {
						Object o = l.get(i);
						if(o instanceof Class) {
							cacheManager.remove(((Class) o).getName());
						}else {
							cacheManager.remove(o.toString());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return count;
		}catch (Exception e) {
			throw new JpaRuntimeException(e);
		} finally {
			// 5.关闭连接
			closeAll(null, ps, rs);
		}
		
	}
	/**
	 * 通有的查询方法
	 * 
	 * @param sql
	 *            要执行的SQL语句
	 * @param args
	 *            参数列表
	 * @return 结果集对象(Result可以在断开连接的情况下操作数据)
	 * @throws Exception
	 */
	private ResultSet getResult(Connection con,String sql, Object... args) {
		if(ZxFrameConfig.showsql) {
			logger.info("query:"+sql.toString()+" args "+JsonUtil.obj2Json(args));
		}
		PreparedStatement ps = null;
		try {
			// 2.获取语句对象
			ps = con.prepareStatement(sql);
			// 3.赋值：
			setValues(ps, args);
			// 4.执行更新(向数据库发送指令)
			return ps.executeQuery();
		} catch (Exception e) {
			throw new JpaRuntimeException(e);
		}
		// Result(断开式连接)和ResultSet(只有在连接状态下才能操作内部数据)的区别:
	}

	/**
	 * 该方法完成通用的赋值操作
	 * 
	 * @param ps
	 * @param args
	 * @throws Exception
	 */
	private void setValues(PreparedStatement ps, Object... args){
		// 赋值(由于在CreateReadUpdateDelete每一种操作都会涉及到赋值)
		// 为了保证代码严禁，所有先做非空检查
		if (ps != null && args != null) {
			int count=args.length;
			// 根据集合长度确定循环次数：
			for (int i = 0; i < count; i++) {
				// 对于各种不同数据类型统一有setObject赋值，其内部完成自动转换
				try {
					ps.setObject(i + 1, args[i]);
				} catch (SQLException e) {
					throw new JpaRuntimeException(e);
				}
			}
		}
	}

	private void closeAll(Connection con, PreparedStatement ps,ResultSet rs){
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			throw new JpaRuntimeException(e);
		}
		try {
			if (ps != null) {
				ps.close();
				ps = null;
			}
		} catch (SQLException e) {
			throw new JpaRuntimeException(e);
		}
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			throw new JpaRuntimeException(e);
		}
	}
}
