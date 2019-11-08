package zxframe.demo.lesson10.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson10.service.Lesson10Service;
import zxframe.log.annotation.FnLog;

@FnLog //记录此类所有方法的入参和返回的日志
@RestController
@RequestMapping("lesson10")
public class Lesson10Controller {
	@Resource
	Lesson10Service lesson10Service;
	
	@RequestMapping("test1")
	public Object test1(String name,String time) {
		return lesson10Service.test1(name,time);
	}
	@RequestMapping("test2")
	public Object test2(String name,String time) {
		return lesson10Service.test2(name,time);
	}
	@RequestMapping("test3")
	public Object test3() {
		return lesson10Service.test3();
	}
	@RequestMapping("test4")
	public Object test4() {
		return lesson10Service.test4();
	}
}
