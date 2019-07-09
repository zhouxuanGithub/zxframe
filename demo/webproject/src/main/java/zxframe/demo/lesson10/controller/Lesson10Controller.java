package zxframe.demo.lesson10.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson10.service.Lesson10Service;

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
}
