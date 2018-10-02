package zxframe.zxdata.mapper;

import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.model.DataModel;
import zxframe.zxdata.model.ZXData;

@DataModelScanning
public class ZXDataMapper {
	public static final String insert=ZXData.class.getName()+"-insert";
	public static final String updateById=ZXData.class.getName()+"-updateById";
	public static final String updateByIdNoLock=ZXData.class.getName()+"-updateByIdNoLock";
	public static final String deleteById=ZXData.class.getName()+"-deleteById";
	public static final String selectById=ZXData.class.getName()+"-selectById";
	public static final String selectByGroup=ZXData.class.getName()+"-selectByGroup";
	public static final String initZxdata=ZXData.class.getName()+"-initZxdata";
	public static final String initZxdataInfo=ZXData.class.getName()+"-initZxdataInfo";
	public static final String initZxdatax=ZXData.class.getName()+"-initZxdatax";
	public static final String initZxdataxBak=ZXData.class.getName()+"-initZxdataxBak";
	public static final String selectByG2T=ZXData.class.getName()+"-selectByG2T";
	public static final String autuUpdateG2T=ZXData.class.getName()+"-autuUpdateG2T";
	public static final String insertG2T=ZXData.class.getName()+"-insertG2T";
	public DataModel initInsert() {
		DataModel cm =new DataModel();
		cm.setSql("insert into @table@ (id,g,v,createTime,eTime) values(?,?,?,?,?)");
		cm.setGroup(insert);
		return cm;
	}
	public DataModel initUpdateById() {
		DataModel cm =new DataModel();
		cm.setSql("update @table@ set v=? , `version`=`version`+1 where id=? and version=?");
		cm.setGroup(updateById);
		return cm;
	}
	public DataModel updateByIdNoLock() {
		DataModel cm =new DataModel();
		cm.setSql("update @table@ set v=? where id=?");
		cm.setGroup(updateByIdNoLock);
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
		cm.setSql("select * from @table@ where id=? and (etime is null or etime > now())");
		cm.setGroup(selectById);
		cm.setResultClass(ZXData.class);
		return cm;
	}
	public DataModel initSelectByGroup() {
		DataModel cm =new DataModel();
		cm.setSql("select * from @table@ where g=? and (etime is null or etime > now())");
		cm.setGroup(selectByGroup);
		cm.setResultClass(ZXData.class);
		return cm;
	}
	
	public DataModel initZxdata() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS zxdatag2t (" + 
				"  `k` int(11) NOT NULL," + 
				"  `v` int(11) DEFAULT NULL," + 
				"  PRIMARY KEY (`k`)" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8 \r\n" +
				"  PARTITION BY RANGE(`k`) (\r\n" + 
				"  PARTITION g2t1 VALUES LESS THAN (1000000),\r\n" + 
				"  PARTITION g2t2 VALUES LESS THAN (2000000),\r\n" + 
				"  PARTITION g2t3 VALUES LESS THAN (3000000),\r\n" + 
				"  PARTITION g2t4 VALUES LESS THAN (4000000),\r\n" + 
				"  PARTITION g2t5 VALUES LESS THAN (5000000),\r\n" + 
				"  PARTITION g2t6 VALUES LESS THAN (6000000),\r\n" + 
				"  PARTITION g2t7 VALUES LESS THAN (7000000),\r\n" + 
				"  PARTITION g2t8 VALUES LESS THAN (8000000),\r\n" + 
				"  PARTITION g2t9 VALUES LESS THAN (9000000),\r\n" + 
				"  PARTITION g2tmax VALUES LESS THAN MAXVALUE\r\n" + 
				"  );");
		cm.setGroup(initZxdata);
		return cm;
	}
	public DataModel initZxdataInfo() {
		DataModel cm =new DataModel();
		cm.setSql("insert  into zxdatag2t(`k`,`v`) values (-1,0);");
		cm.setGroup(initZxdataInfo);
		return cm;
	}
	public DataModel initZxdatax() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS zxdata@code@ (\r\n" + 
				"  `id` char(36) NOT NULL,\r\n" + 
				"  `g` varchar(255) NOT NULL,\r\n" + 
				"  `v` text NOT NULL,\r\n" + 
				"  `createTime` datetime NOT NULL,\r\n" + 
				"  `eTime` datetime DEFAULT NULL,\r\n" + 
				"  `version` int(11) NOT NULL DEFAULT '0',\r\n" + 
				"  PRIMARY KEY (`id`),\r\n" + 
				"  KEY `NewIndex1` (`g`),\r\n" + 
				"  KEY `NewIndex2` (`eTime`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8 \r\n"+ 
				"partition by key(g)\r\n" + 
				"partitions 10;");
		cm.setGroup(initZxdatax);
		return cm;
	}
	public DataModel initZxdataxBak() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS zxdatabak (\r\n" + 
				"  `id` char(36) NOT NULL,\r\n" + 
				"  `g` varchar(255) NOT NULL,\r\n" + 
				"  `v` text NOT NULL,\r\n" + 
				"  `createTime` datetime NOT NULL,\r\n" + 
				"  `eTime` datetime DEFAULT NULL,\r\n" + 
				"  `version` int(11) NOT NULL DEFAULT '0',\r\n" + 
				"  KEY `NewIndex1` (`id`),\r\n" + 
				"  KEY `NewIndex2` (`g`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8 \r\n"+ 
				"partition by key(g)\r\n" + 
				"partitions 10;");
		cm.setGroup(initZxdataxBak);
		return cm;
	}
	public DataModel selectByG2T() {
		DataModel cm =new DataModel();
		cm.setSql("select v from zxdatag2t where k=?");
		cm.setGroup(selectByG2T);
		cm.setResultClass(Integer.class);
		return cm;
	}
	public DataModel autuUpdateG2T() {
		DataModel cm =new DataModel();
		cm.setSql("update zxdatag2t set `v`=`v`+1 where `k`=-1");
		cm.setGroup(autuUpdateG2T);
		return cm;
	}
	public DataModel insertG2T() {
		DataModel cm =new DataModel();
		cm.setSql("insert  into zxdatag2t(`k`,`v`) values (?,?)");
		cm.setGroup(insertG2T);
		return cm;
	}
	
}

