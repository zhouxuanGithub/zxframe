package zxframe.demo.lesson12.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zxframe.properties.PropertiesCache;

@RestController
@RequestMapping("lesson12")
public class Lesson12Controller {
	@RequestMapping("test")
	public Object test() {
		return PropertiesCache.get("zxframe-test-pps");
	}
}
