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
//		m.put("qq-1164429680", UUID.randomUUID().toString());
//		m.put("qq-404557187", UUID.randomUUID().toString());
//		m.put("qq-303810790", UUID.randomUUID().toString());
		//查询数据
		String key="qq-1164429680";
		//改数据
//		m.update(key,"123456");
		//删除数据
//		m.remove(key);
		String value = m.get(key);
		return value==null?"null":value;
	}
}
