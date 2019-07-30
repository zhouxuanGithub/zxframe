package zxframe.demo.lesson07.service;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.demo.lesson07.model.User07;
import zxframe.jpa.dao.MysqlTemplate;
import zxframe.util.MathUtil;

@Service
public class Lesson07Service {
	@Resource
	private MysqlTemplate mysqlTemplate;

	public Object doTest() {
		//对象添加,走写库
//		User07 u =new User07();
//		u.setId(UUID.randomUUID().toString());
//		u.setName("07隔壁"+MathUtil.nextInt(1000)+"哥");
//		u.setAge(MathUtil.nextInt(100));
//		mysqlTemplate.save(u);
		//对象查询，走读库，读数据源内部随机选择读取，含异常熔断
		return mysqlTemplate.getList("lesson07MapperSelect");
	}
}
