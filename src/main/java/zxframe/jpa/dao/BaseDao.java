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
import zxframe.cache.model.CacheModel;
import zxframe.cache.transaction.CacheTransaction;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.annotation.Id;
import zxframe.jpa.datasource.DataSourceManager;
import zxframe.jpa.ex.JpaRuntimeException;
import zxframe.jpa.util.SQLParsing;
import zxframe.util.JsonUtil;


@Repository
public class BaseDao {
	private Logger logger = LoggerFactory.getLogger(BaseDao.class);  
	@Resource
	CacheTransaction ct;
	/**
	 * 增：保存对象
	 * @param obj 需要保存的对象
	 * @return id
	 */
	public Serializable save(Object obj) {
		Serializable id = null;
		try {
			String group=obj.getClass().getName();
			CacheModel cm = CacheModelManager.getCacheModelByClass(group);
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
			execute(CacheModelManager.cacheModelAnnotation.get(group).dsname(),sql.toString(),args);
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
		CacheModel cm = CacheModelManager.getCacheModelByClass(group);
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
	 * @param clas 对象class
	 * @param sql sql
	 * @param args sql参数
	 * @return
	 */
	public <T> T get(Class<T> clas, String sql,Object... args) {
		return get(clas,sql,null,args);
	}
	/**
	 * 查询对象
	 * @param clas 对象class
	 * @param cacheModel 缓存模型
	 * @param args sql参数
	 * @return
	 */
	public <T> T get(Class<T> clas, CacheModel cacheModel,Object... args) {
		return get(clas,cacheModel.getSql(),cacheModel,args);
	}
	private <T> T get(Class<T> clas, String sql,CacheModel cacheModel,Object... args) {
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
		CacheModel cm = CacheModelManager.getCacheModelByClass(group);
		String sql = "delete from "+clas.getSimpleName()+" where "+CacheModelManager.cacheIdFieldMap.get(group).getName()+" = ?";
		//执行删除
		execute(CacheModelManager.cacheModelAnnotation.get(group).dsname(),sql,id);
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
			CacheModel cm = CacheModelManager.getCacheModelByClass(group);
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
			int execute = (int) execute(CacheModelManager.cacheModelAnnotation.get(group).dsname(),sql.toString(),args);
			if(execute<1) {
				//版本控制出现问题
				logger.warn("StaleObjectStateException :"+JsonUtil.obj2Json(obj));
				ct.remove(group, id.toString());
				throw new JpaRuntimeException("数据 version 已过期。");
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
	 * @param clas class
	 * @param sql sql语句
	 * @param args 参数
	 */
	public <T> List<T> getList(Class<T> clas,String sql, Object... args) {
		return getList(clas, sql,null,args);
	}
	/**
	 * 查：根据sql查询对象集合
	 * @param clas class
	 * @param cacheModel 缓存模型
	 * @param args 参数
	 * @return 查询出的对象集合
	 */
	public <T> List<T> getList(Class<T> clas,CacheModel cacheModel, Object... args) {
		return getList(clas, cacheModel.getSql(),cacheModel,args);
	}
	/**
	 * 查：根据sql查询对象集合
	 * @param sql sql语句
	 * @param cacheModel 缓存模型 null则不存入缓存
	 * @param args 参数
	 * @return 查询出的对象集合
	 */
	private <T> List<T> getList(Class<T> clas,String sql,CacheModel cacheModel, Object... args) {
		Connection con =null;
		ResultSet rs=null;
		try {
			String cid = null;
			ArrayList<T> list = new ArrayList<T>();
			String group=clas.getName();
			if(cacheModel!=null&&cacheModel.isQueryCache()) {
				//查询
				cid = CacheManager.getQueryKey(sql, args);
				list=(ArrayList<T>) ct.get(cacheModel.getCacheGroup(), cid);
				if(list!=null) {
					return list;
				}
			}
			//计算数据源
			String dsname=SQLParsing.getDSName(clas,sql);
			//打开连接
			con = DataSourceManager.getRConnection(dsname);
			//去数据库获得数据
			rs = getResult(con,sql,args);
			Map<String, Field> fieldMap = CacheModelManager.cacheFieldsMap.get(group);
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
			return list;
		} catch (Exception e) {
			throw new JpaRuntimeException(e);
		}finally {
			closeAll(con,null, rs);
		}
	}
	/**
	 * 数据更新(增删改)
	 * @param dsname 数据源名
	 * @param sql 要执行的SQL语句
	 * @param args 赋值的参数集合
	 * @return 执行状态
	 */
	public Object execute(String dsname,String sql, Object... args) {
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
	public void setValues(PreparedStatement ps, Object... args){
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

	public void closeAll(Connection con, PreparedStatement ps,ResultSet rs){
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
