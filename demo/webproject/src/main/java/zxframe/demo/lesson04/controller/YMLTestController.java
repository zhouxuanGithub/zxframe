package zxframe.demo.lesson04.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.demo.lesson04.model.ConfigDemo;

@RestController
@RequestMapping("lesson04")
public class YMLTestController {
	@Value("${ymltest}")
	private String ymltest;
	@Resource
	private ConfigDemo cd;
	@RequestMapping("test")
	public String test() {
		return ymltest+" "+cd.toString();
	}
	
}
