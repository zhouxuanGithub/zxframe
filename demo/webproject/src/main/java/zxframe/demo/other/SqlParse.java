package zxframe.demo.other;

import zxframe.jpa.inf.ISQLParse;

//QL在执行前，会将执行SQL传给此类，可自行再加工或者分析
public class SqlParse implements ISQLParse{

	@Override
	public String sqlParsing(String sql) {
		System.out.println("加工SQL："+sql);
		return sql;
	}
	
}
