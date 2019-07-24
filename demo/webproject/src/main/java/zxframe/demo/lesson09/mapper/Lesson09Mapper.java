package zxframe.demo.lesson09.mapper;

import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.model.DataModel;

@DataModelScanning
public class Lesson09Mapper {
	public DataModel init09Mapper1() {
		DataModel cm =new DataModel();
		cm.setGroup("lesson09Mapper");
		cm.setRcCache(true);//是否使用远程缓存
		cm.setRcETime(1200);//远程缓存时长 秒
		cm.setLcCache(true);//是否使用本地缓存
		return cm;
	}
}
