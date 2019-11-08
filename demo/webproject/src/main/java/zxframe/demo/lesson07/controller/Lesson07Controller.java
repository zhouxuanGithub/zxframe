package zxframe.demo.lesson07.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson07.service.Lesson07Service;
import zxframe.log.annotation.FnLog;

@FnLog //记录此类所有方法的入参和返回的日志
@RestController
@RequestMapping("lesson07")
public class Lesson07Controller {
	@Resource
	Lesson07Service lesson07Service;
	@RequestMapping("test")
	public Object test() {
		return lesson07Service.doTest();
	}
}
