package zxframe.demo.lesson06.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.demo.lesson06.model.User;
import zxframe.jpa.dao.MysqlTemplate;
import zxframe.util.MathUtil;

@Service
public class Lesson06Service {
	@Resource
	private MysqlTemplate mysqlTemplate;

	public Object doTest() {
		//对象添加
//		User u =new User();
//		u.setId("UUID.randomUUID().toString()");
//		u.setName("隔壁"+MathUtil.nextInt(1000)+"哥");
//		u.setAge(MathUtil.nextInt(100));
//		mysqlTemplate.save(u);
		//根据id查询
//		User u2 =mysqlTemplate.get(User.class, "79cbc702-9092-4042-b790-5e9523661b8c");
//		if(u2!=null) {
//			System.out.println(u2);
//			u2.setAge(MathUtil.nextInt(100));
//			//对象更新
//			mysqlTemplate.update(u2);
//			//删除
//			//mysqlTemplate.delete(User.class, id);
//		}
		//更新，不传参
//		mysqlTemplate.execute("lesson06MapperUpdate");
		//更新，?传参
//		mysqlTemplate.execute("lesson06MapperUpdateV2",20,30);
		//更新，map传参
//		Map map =new HashMap<>();
//		map.put("age",19);
//		mysqlTemplate.execute("lesson06MapperUpdateV3",map);
		return mysqlTemplate.getList("lesson06MapperSelect");
	}
}
