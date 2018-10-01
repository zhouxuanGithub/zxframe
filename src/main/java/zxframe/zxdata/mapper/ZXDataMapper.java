package zxframe.zxdata.mapper;

import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.model.DataModel;
import zxframe.properties.model.Properties;
import zxframe.zxdata.model.ZXData;

@DataModelScanning
public class ZXDataMapper {
	public static final String insert=ZXData.class.getName()+"-insert";
	public static final String updateById=ZXData.class.getName()+"-updateById";
	public static final String deleteById=ZXData.class.getName()+"-deleteById";
	public static final String selectById=ZXData.class.getName()+"-selectById";
	public static final String selectByGroup=ZXData.class.getName()+"-selectByGroup";
	public static final String initZxdata=ZXData.class.getName()+"-initZxdata";
	public static final String initZxdataInfo=ZXData.class.getName()+"-initZxdataInfo";
	public DataModel initInsert() {
		DataModel cm =new DataModel();
		cm.setSql("insert into @table@ (id,group,value,createTime,eTime) values(?,?,?,?,?)");
		cm.setGroup(insert);
		return cm;
	}
	public DataModel initUpdateById() {
		DataModel cm =new DataModel();
		cm.setSql("update @table@ set value=? where id=? and version=?");
		cm.setGroup(updateById);
		return cm;
	}
	public DataModel initDeleteById() {
		DataModel cm =new DataModel();
		cm.setSql("delete from @table@ where id=?");
		cm.setGroup(deleteById);
		return cm;
	}
	public DataModel initSelectById() {
		DataModel cm =new DataModel();
		cm.setSql("select * from @table@ where id=? and etime > now()");
		cm.setGroup(selectById);
		cm.setResultClass(ZXData.class);
		return cm;
	}
	public DataModel initSelectByGroup() {
		DataModel cm =new DataModel();
		cm.setSql("select * from @table@ where group=? and etime > now()");
		cm.setGroup(selectByGroup);
		cm.setResultClass(ZXData.class);
		return cm;
	}
	
	public DataModel initZxdata() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS `zxdata` (" + 
				"  `key` int(11) NOT NULL," + 
				"  `value` int(11) DEFAULT NULL," + 
				"  PRIMARY KEY (`key`)" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		cm.setGroup(initZxdata);
		cm.setDsClass(Properties.class);
		return cm;
	}
	public DataModel initZxdataInfo() {
		DataModel cm =new DataModel();
		cm.setSql("insert  into `zxdata`(`key`,`value`) values (-2,0),(-1,0);");
		cm.setGroup(initZxdataInfo);
		cm.setDsClass(Properties.class);
		return cm;
	}
}

