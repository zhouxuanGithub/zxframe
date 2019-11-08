package zxframe.demo.lesson12.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.log.annotation.FnLog;
import zxframe.properties.PropertiesCache;
@FnLog //记录此类所有方法的入参和返回的日志
@RestController
@RequestMapping("lesson12")
public class Lesson12Controller {
	@RequestMapping("test")
	public Object test() {
		return PropertiesCache.get("zxframe-test-pps");
	}
}
