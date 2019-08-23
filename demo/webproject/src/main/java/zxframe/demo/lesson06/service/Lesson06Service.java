package zxframe.demo.lesson06.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.demo.lesson06.model.User06;
import zxframe.jpa.dao.MysqlTemplate;

@Service
public class Lesson06Service {
	@Resource
	private MysqlTemplate mysqlTemplate;

	public Object doTest() {
		//对象添加
		//主键自分配，不自动增长
//		User06 u =new User06();
//		u.setId(UUID.randomUUID().toString());
//		u.setName("06隔壁"+MathUtil.nextInt(1000)+"哥");
//		u.setAge(MathUtil.nextInt(100));
//		u.setBirthday(new Date());
//		u.setUpdatetime(new Timestamp(new Date().getTime()));
//		u.setAlias("王二哥");
//		mysqlTemplate.save(u);
//		System.out.println(u);
//		//新增数据后获得自增长主键
//		TestId tid= new TestId();
//		tid.setValue(UUID.randomUUID().toString());
//		Serializable cid = mysqlTemplate.save(tid);//返回主键
//		System.out.println(cid);
//		System.out.println(tid);
		//根据id查询
		User06 u2 =mysqlTemplate.get(User06.class, "79cbc702-9092-4042-b790-5e9523661b8c");
		if(u2!=null) {
			System.out.println(u2);
//			//对象更新
//			u2.setAge(MathUtil.nextInt(100));
//			u2.setBirthday(new Date());
//			u2.setUpdatetime(new Timestamp(new Date().getTime()));
//			mysqlTemplate.update(u2);
//			//删除
//			mysqlTemplate.delete(User.class, "68237481-3c0d-461a-bc19-e59f694ba76a");
		}
		//更新，不传参
//		mysqlTemplate.execute("lesson06Mapper.update");
		//更新，?传参
//		mysqlTemplate.execute("lesson06Mapper.updateV2",20,30);
		//更新，map传参
//		Map map =new HashMap<>();
//		map.put("age",33);
//		mysqlTemplate.execute("lesson06Mapper.updateV3",map);//${..}符号占位，纯粹的字符串替换
//		mysqlTemplate.execute("lesson06Mapper.updateV4",map);//#{..}符号占位，参数替换，可防SQL注入
		//更新，对象转Map传参
//		User u3 =new User();
//		u3.setAge(100);
//		mysqlTemplate.execute("lesson06Mapper.updateV3",JsonUtil.obj2Map(u3));
		//批sql执行
//		mysqlTemplate.executeBatch("lesson06Mapper.executeBatchSQL");
		//查询list
//		return mysqlTemplate.getList("lesson06Mapper.select");//返回对象 list
		return mysqlTemplate.getList("lesson06Mapper.selectV2");//返回map list
		//支持<if test> <include refid=""/>标签
//		Map map2 =new HashMap<>();
//		map2.put("age",33);
//		map2.put("name","testname");
//		return mysqlTemplate.getList("lesson06Mapper.selectV3",map2);
	}
}
