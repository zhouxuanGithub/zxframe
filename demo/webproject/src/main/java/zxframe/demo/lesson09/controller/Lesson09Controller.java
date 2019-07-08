package zxframe.demo.lesson09.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson09.service.Lesson09Service;

@RestController
@RequestMapping("lesson09")
public class Lesson09Controller {
	@Resource
	Lesson09Service lesson09Service;
	@RequestMapping("test")
	public Object test() {
		return lesson09Service.doTest();
	}
}
