package zxframe.demo.other;

import org.springframework.stereotype.Component;

import zxframe.jpa.inf.ISQLParse;

//SQL在执行前，会将执行SQL传给此类，可自行再加工或者分析
//在zxframe.xml里配置此类的spring bean id
@Component
public class SqlParse implements ISQLParse{

	@Override
	public String sqlParsing(String sql) {
		//System.out.println("加工SQL："+sql);
		return sql;
	}
	
}
