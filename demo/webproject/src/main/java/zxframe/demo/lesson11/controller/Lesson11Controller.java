package zxframe.demo.lesson11.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson11.service.Lesson11Service;
import zxframe.log.annotation.FnLog;
@FnLog //记录此类所有方法的入参和返回的日志
@RestController
@RequestMapping("lesson11")
public class Lesson11Controller {
	@Resource
	Lesson11Service lesson11Service;
	@RequestMapping("test")
	public Object test() {
		return lesson11Service.doTest();
	}
	@RequestMapping("test2")
	public Object test2() {
		return lesson11Service.doTest2();
	}
}
