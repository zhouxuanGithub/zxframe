package zxframe.demo.lesson09.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson09.service.Lesson09Service;
import zxframe.log.annotation.FnLog;

@FnLog //记录此类所有方法的入参和返回的日志，使用INFO级别才会记录
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
