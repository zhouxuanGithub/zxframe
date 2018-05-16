package zxframe.jpa.util;

import java.util.Iterator;
import java.util.Map;

import zxframe.cache.mgr.CacheModelManager;
import zxframe.jpa.annotation.Model;

public class SQLParsing {
	//解析类，获取数据源名
	public static String getDSName(Class dsClass,Class resultClass) {
		Model model = null;
		if(dsClass!=null) {
			model = CacheModelManager.cacheModelAnnotation.get(dsClass.getName());
		}
		else if(resultClass!=null) {
			model = CacheModelManager.cacheModelAnnotation.get(resultClass.getName());
		}
		if(model==null) {
			return "default";
		}else {
			return model.dsname();
		}
	}
	//解析sql，获取数据源名
	public static String getDSName(Class dsClass,Class resultClass,String sql) {
		Model model = null;
		if(dsClass!=null) {
			model = CacheModelManager.cacheModelAnnotation.get(dsClass.getName());
		}
		else if(resultClass!=null) {
			model = CacheModelManager.cacheModelAnnotation.get(resultClass.getName());
		}
		if(model==null) {
			if(sql.indexOf("where")!=-1) {
				sql=sql.substring(sql.indexOf("from ")+5, sql.indexOf(" where"));
			}else {
				sql=sql.substring(sql.indexOf("from ")+5, sql.length());
			}
			String modelName=sql.trim().toLowerCase();
			return CacheModelManager.cacheModelJAnnotation.get(modelName).dsname();
		}else {
			return model.dsname();
		}
	}
	//sql参数部分增强
	public static String replaceSQL(String sql, Map<String, String> map) {
		if(map!=null) {
			Iterator<String> iterator = map.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				sql=sql.replaceAll("@"+key+"@",map.get(key));
			}
		}
		return sql;
	}
}
