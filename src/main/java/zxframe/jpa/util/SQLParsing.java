package zxframe.jpa.util;

import zxframe.cache.mgr.CacheModelManager;
import zxframe.jpa.annotation.Model;

public class SQLParsing {
	//解析sql，获取数据源名
	public static String getDSName(Class clas,String sql) {
		Model model = CacheModelManager.cacheModelAnnotation.get(clas.getName());
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
}
