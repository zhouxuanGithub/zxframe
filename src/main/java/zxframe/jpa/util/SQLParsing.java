package zxframe.jpa.util;

import java.util.Iterator;
import java.util.Map;

import zxframe.cache.mgr.CacheModelManager;
import zxframe.jpa.annotation.Model;

public class SQLParsing {
	/**
	 * 获取数据源名
	 * 1.如果有dsClass则优先取此数据源
	 * 2.如果有resultClass则取此数据源
	 * 3.默认数据源
	 * @param dsClass
	 * @param resultClass
	 * @param sql
	 * @return
	 */
	public static String getDSName(Class dsClass,Class resultClass,String sql) {
		Model model = null;
		if(dsClass!=null) {
			model = CacheModelManager.cacheModelAnnotation.get(dsClass.getName());
		}
		if(model==null&&resultClass!=null) {
			model = CacheModelManager.cacheModelAnnotation.get(resultClass.getName());
		}
//		if(model==null) {
//			//增删改查语句解析
//			sql=sql.trim().toLowerCase();
//			String modelName=null;
//			if(sql.startsWith("insert")) {
//				modelName=sql.substring(sql.indexOf("into ")+5, sql.indexOf("("));
//			}else if(sql.startsWith("update")) {
//				modelName=sql.substring(7, sql.indexOf("set"));
//			}else if(sql.startsWith("select")||sql.startsWith("delete")) {
//				if(sql.indexOf("where")!=-1) {
//					modelName=sql.substring(sql.indexOf("from ")+5, sql.indexOf(" where"));
//				}else if(sql.indexOf("order by")!=-1) {
//					modelName=sql.substring(sql.indexOf("from ")+5, sql.indexOf(" order by"));
//				}else if(sql.indexOf("group by")!=-1) {
//					modelName=sql.substring(sql.indexOf("from ")+5, sql.indexOf(" group by"));
//				}else {
//					modelName=sql.substring(sql.indexOf("from ")+5, sql.length());
//				}
//			}else if(sql.startsWith("create table if not exists")){//表创建
//				modelName=sql.substring(26, sql.indexOf("("));
//			}else if(sql.startsWith("create trigger")){//触发器创建
//				modelName=sql.substring(sql.indexOf(" on ")+4, sql.indexOf(" for "));
//			}
//			if(modelName!=null) {
//				model = CacheModelManager.cacheModelJAnnotation.get(modelName.trim());
//			}
//		}
		if(model==null || model.dsname().length()==0) {
			return "default";
		}else {
			return model.dsname();
		}
	}
	//sql参数部分增强
	public static String replaceSQL(String sql, Map map) {
		if(map!=null) {
			Iterator<String> iterator = map.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				sql=sql.replaceAll("\\$"+key+"\\$",String.valueOf(map.get(key)));
			}
		}
		return sql;
	}
}
