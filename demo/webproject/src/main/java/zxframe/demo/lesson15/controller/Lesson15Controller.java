package zxframe.demo.lesson15.controller;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.data.mgr.ZXDataTemplate;
import zxframe.log.annotation.FnLog;
@FnLog //记录此类所有方法的入参和返回的日志，使用INFO级别才会记录
@RestController
@RequestMapping("lesson15")
public class Lesson15Controller {
	@Resource
	private ZXDataTemplate m;
	@RequestMapping("test")
	public Object test() {
		//插入数据
		for (int i = 0; i < 10; i++) {
			m.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		}
		//查询数据
		String key="c51dd50a-d5c2-49a0-b06c-d5ac82ec000f";
		String value = m.get(key);
		//改数据
//		m.update(key,"123");
		//删除数据
//		m.remove(key);
		return value==null?"null":value;
	}
}
