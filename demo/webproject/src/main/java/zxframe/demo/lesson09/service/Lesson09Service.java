package zxframe.demo.lesson09.service;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.cache.mgr.CacheManager;
import zxframe.demo.lesson06.model.User;
import zxframe.demo.lesson09.model.User09;
import zxframe.util.MathUtil;

@Service
public class Lesson09Service {
	@Resource
	CacheManager cacheManager;
	public Object doTest() {
		User09 user=(User09) cacheManager.get("lesson09Mapper","lesson09Cache");
		System.out.println(user);
		if(user==null) {
			user=new User09();
		}
		user.setId(UUID.randomUUID().toString());
		user.setName("09隔壁"+MathUtil.nextInt(1000)+"哥");
		user.setAge(MathUtil.nextInt(100));
		cacheManager.put("lesson09Mapper", "lesson09Cache", user);
		//cacheManager.remove("lesson09Mapper");
		return user;
	}
}
