/**
 * ZxFrame Java Library
 * https://github.com/zhouxuanGithub/zxframe
 *
 * Copyright (c) 2019 zhouxuan
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package zxframe.jpa.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import zxframe.cache.mgr.CacheModelManager;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.annotation.Model;
import zxframe.jpa.ex.JpaRuntimeException;
import zxframe.jpa.inf.ISQLParse;
import zxframe.jpa.model.DataModel;

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
	public static String replaceSQL(DataModel dm,Map map) {
		String sql=dm.getSql();
		//标签解析
		if(map!=null) {
			Map<String, Object> diyDataMap = dm.getDiyDataMap();
			ExpressionParser parser = null;
			EvaluationContext context = null;
			List<String> includeId = (List<String>) diyDataMap.get("mapper-include-id");
			if(includeId!=null) {
				for (int i = 0; i < includeId.size(); i++) {
					String refid = includeId.get(i);
					DataModel rdm = CacheModelManager.getDataModelByGroup(refid);
					if(rdm==null) {
						throw new JpaRuntimeException("mapper id not found:"+refid);
					}
					if(rdm.getDiyDataMap().containsKey("mapper-iftest-testList")) {
						if(parser==null) {
							parser = new SpelExpressionParser();
							context = new StandardEvaluationContext(map);
						}
					}
					sql=sql.replace("${mapper-include-"+refid+"}",getMapperIfIcdSQL(rdm,rdm.getSql(),parser,context));
				}
			}
			
			if(diyDataMap.containsKey("mapper-iftest-testList")) {
				if(parser==null) {
					parser = new SpelExpressionParser();
					context = new StandardEvaluationContext(map);
				}
				sql=getMapperIfIcdSQL(dm,sql,parser,context);
			}
		}
		//解析替换字符
		if(map!=null) {
			if(sql.indexOf("${")!=-1) {
				Iterator<String> iterator = map.keySet().iterator();
				while(iterator.hasNext()) {
					String key = iterator.next();
					if(sql.indexOf("${")!=-1) {
						sql=sql.replace("${"+key+"}",String.valueOf(map.get(key)));
					}else {
						break;
					}
				}
			}
			if(sql.indexOf("#{")!=-1) {
				Iterator<String> iterator = map.keySet().iterator();
				while(iterator.hasNext()) {
					String key = iterator.next();
					if(sql.indexOf("#{")!=-1) {
						sql=sql.replace("#{"+key+"}",escapeSQLString(map.get(key)));
					}else {
						break;
					}
				}
			}
		}
		if(ZxFrameConfig.sqlParselist!=null) {
			for (int i = 0; i < ZxFrameConfig.sqlParselist.size(); i++) {
				ISQLParse isqlParse = ZxFrameConfig.sqlParselist.get(i);
				sql=isqlParse.sqlParsing(sql);
			}
		}
		return sql;
	}
	private static String getMapperIfIcdSQL(DataModel dm,String sql,ExpressionParser parser,EvaluationContext context) {
		List<String> testList = (List<String>) dm.getDiyDataMap().get("mapper-iftest-testList");
		if(testList!=null) {
			Map<String, String> textMap = (Map<String, String>) dm.getDiyDataMap().get("mapper-iftest-textMap");
			for (int i = 0; i < testList.size(); i++) {
				boolean b=(boolean)parser.parseExpression(testList.get(i)).getValue(context);
				if(b) {
					sql=sql.replace("${mapper-if-"+i+"}",textMap.get("${mapper-if-"+i+"}"));
				}else {
					sql=sql.replace("${mapper-if-"+i+"}","");
				}
			}
		}
		return sql;
	}
	//获得表名
	public static String getTBName(Class cls) {
		Model model = CacheModelManager.cacheModelAnnotation.get(cls.getName());
		if(model.tbname().equals("")) {
			return cls.getSimpleName().toLowerCase();
		}else {
			return model.tbname();
		}
	}
	//防止SQL注入替换
	public static String escapeSQLString(Object pm) {
		if(pm==null){
			return "null";
		}
		String sql=String.valueOf(pm);
		Class clas = pm.getClass();
		if(clas == int.class||clas == Integer.class || clas == float.class||clas == Float.class || clas == double.class||clas == Double.class ||clas == long.class||clas == Long.class) {
			return  sql;
		}
		StringBuilder buf = new StringBuilder();
		buf.append('\'');
		for (int i = 0; i < sql.length(); ++i) {
            char c = sql.charAt(i);
        		switch (c) {
                case 0: /* Must be escaped for 'mysql' */
                    buf.append('\\');
                    buf.append('0');
                    break;
                case '\n': /* Must be escaped for logs */
                    buf.append('\\');
                    buf.append('n');
                    break;
                case '\r':
                    buf.append('\\');
                    buf.append('r');
                    break;
                case '\\':
                    buf.append('\\');
                    buf.append('\\');
                    break;
                case '\'':
                    buf.append('\\');
                    buf.append('\'');
                    break;
                case '"': 
                    buf.append('"');
                    break;
                case '%':
                	buf.append("\\%");
                    break;
                case '_':
                	buf.append("\\_");
                    break;
                case '\032':
                    buf.append('\\');
                    buf.append('Z');
                    break;
                default:
                    buf.append(c);
            }
        }
		buf.append('\'');
		return buf.toString();
	}
}
