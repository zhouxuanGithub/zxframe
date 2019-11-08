package zxframe.demo.lesson15.controller;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.data.model.ZXData;
import zxframe.data.service.ZXDataManager;
import zxframe.log.annotation.FnLog;
@FnLog //记录此类所有方法的入参和返回的日志，使用INFO级别才会记录
@RestController
@RequestMapping("lesson15")
public class Lesson15Controller {
	@Resource
	private ZXDataManager m;
	@RequestMapping("test")
	public Object test() {
		//插入数据
//		for (int i = 0; i < 10; i++) {
//			m.put(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 60*5);
//		}
		//查询数据
		ZXData z = m.get("c51dd50a-d5c2-49a0-b06c-d5ac82ec000f");
		//改数据
//		z.setValue("ok");
//		m.update(z);
		//删除数据
//		m.remove(z.getKey());
		return z==null?"null":z.toString();
	}
}
