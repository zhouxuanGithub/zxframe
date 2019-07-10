package zxframe.demo.lesson11.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.cache.mgr.CacheManager;
import zxframe.demo.lesson11.model.User11;
import zxframe.jpa.dao.MysqlTemplate;

@Service
public class Lesson11Service {
	@Resource
	private MysqlTemplate mysqlTemplate;
	@Resource
	private CacheManager cacheManager;
	public Object doTest() {
		String id="79cbc702-9092-4042-b790-5e9523661b8c";
		User11 user11 =null;
		user11 = mysqlTemplate.get(User11.class, id);//会添加or更新库数据和对应缓存
		//mysqlTemplate.delete(clas, id);//会删除库数据和对应缓存
		//mysqlTemplate.update(obj);//会添加or更新库数据和对应缓存
		//mysqlTemplate.save(obj)//会添加or更新库数据和对应缓存
		//cacheManager.remove(User11.class.getName(),id);//清理缓存
		return user11;
	}
	public Object doTest2() {
		//cacheManager.removeQueryCache("lesson11MapperSelect");//清理查询缓存
		return mysqlTemplate.getList("lesson11MapperSelect");//结果会加入缓存
	}
}
