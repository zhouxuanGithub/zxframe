package zxframe.demo.lesson10.service;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.cache.annotation.FnCache;
import zxframe.jpa.dao.MysqlTemplate;

@Service
public class Lesson10Service {
	@Resource
	private MysqlTemplate mysqlTemplate;
	//方法传参未实际使用，只是为了验证缓存KEY拼接
	@FnCache
	public Object test1(String name,String time) {
		return mysqlTemplate.getList("lesson06Mapper.select");
	}
	@FnCache(key={"name"})
	public Object test2(String name,String time) {
		return mysqlTemplate.getList("lesson06Mapper.select");
	}
	@FnCache
	public Object test3() {
		mysqlTemplate.getList("lesson06Mapper.select");
		//空数据缓存30秒，防止用户反复用同一个参数暴力攻击
		return null;
	}
	@FnCache
	public Object test4() {
		mysqlTemplate.getList("lesson06Mapper.select");
		//空数据缓存30秒，防止用户反复用同一个参数暴力攻击
		return new ArrayList<>();
	}
}
