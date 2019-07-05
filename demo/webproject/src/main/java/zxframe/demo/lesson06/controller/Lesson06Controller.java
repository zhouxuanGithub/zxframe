package zxframe.demo.lesson06.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson06.service.Lesson06Service;

@RestController
@RequestMapping("lesson06")
public class Lesson06Controller {
	@Resource
	Lesson06Service lesson06Service;
	@RequestMapping("test")
	public Object test() {
		return lesson06Service.doTest();
	}
}
