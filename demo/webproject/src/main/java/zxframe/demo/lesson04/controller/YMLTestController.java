package zxframe.demo.lesson04.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson04.model.ConfigDemo;
import zxframe.log.annotation.FnLog;

@FnLog //记录此类所有方法的入参和返回的日志，使用INFO级别才会记录
@RestController
@RequestMapping("lesson04")
//@RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用
public class YMLTestController {
	@Value("${ymltest}")
	private String ymltest;
	@Resource
	private ConfigDemo cd;
	
	@RequestMapping("test")
	public String test() {
		//错误日志记录测试
//		int b = 100/0;
//		System.out.println(b);
		return ymltest+" "+cd.toString();
	}
	
}
