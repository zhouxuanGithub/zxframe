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
	public static final String initZxdatax=ZXData.class.getName()+"-initZxdatax";
	public static final String initZxdataxBak=ZXData.class.getName()+"-initZxdataxBak";
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
		cm.setSql("CREATE TABLE IF NOT EXISTS zxdatag2t (" + 
				"  `key` int(11) NOT NULL," + 
				"  `value` int(11) DEFAULT NULL," + 
				"  PRIMARY KEY (`key`)" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		cm.setGroup(initZxdata);
		return cm;
	}
	public DataModel initZxdataInfo() {
		DataModel cm =new DataModel();
		cm.setSql("insert  into zxdatag2t(`key`,`value`) values (-2,0),(-1,0);");
		cm.setGroup(initZxdataInfo);
		return cm;
	}
	public DataModel initZxdatax() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS zxdata@code@ (\r\n" + 
				"  `id` char(36) NOT NULL,\r\n" + 
				"  `group` varchar(255) NOT NULL,\r\n" + 
				"  `value` text NOT NULL,\r\n" + 
				"  `createTime` datetime NOT NULL,\r\n" + 
				"  `eTime` datetime DEFAULT NULL,\r\n" + 
				"  `varsion` int(11) NOT NULL DEFAULT '0',\r\n" + 
				"  PRIMARY KEY (`id`),\r\n" + 
				"  KEY `NewIndex1` (`group`),\r\n" + 
				"  KEY `NewIndex2` (`eTime`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		cm.setGroup(initZxdatax);
		return cm;
	}
	public DataModel initZxdataxBak() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS zxdatabak@code@ (\r\n" + 
				"  `id` char(36) NOT NULL,\r\n" + 
				"  `group` varchar(255) NOT NULL,\r\n" + 
				"  `value` text NOT NULL,\r\n" + 
				"  `createTime` datetime NOT NULL,\r\n" + 
				"  `eTime` datetime DEFAULT NULL,\r\n" + 
				"  `varsion` int(11) NOT NULL DEFAULT '0',\r\n" + 
				"  KEY `NewIndex1` (`id`),\r\n" + 
				"  KEY `NewIndex2` (`group`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		cm.setGroup(initZxdataxBak);
		return cm;
	}
}

