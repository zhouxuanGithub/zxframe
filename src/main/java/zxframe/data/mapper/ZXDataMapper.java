/**
 * ZxFrame Java Library
 * https://github.com/zhouxuanGithub/zxframe
 *
 * Copyright (c) 2019 zhouxuan
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package zxframe.data.mapper;

import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.model.DataModel;
import zxframe.data.model.ZXData;

@DataModelScanning
public class ZXDataMapper {
	public static final String insert=ZXDataMapper.class.getName()+"-insert";
	public static final String update=ZXDataMapper.class.getName()+"-update";
	public static final String updateNoLock=ZXDataMapper.class.getName()+"-updateNoLock";
	public static final String delete=ZXDataMapper.class.getName()+"-delete";
	public static final String select=ZXDataMapper.class.getName()+"-select";
	public static final String deleteByETime=ZXDataMapper.class.getName()+"-deleteByETime";
	public static final String initData=ZXDataMapper.class.getName()+"-initData";
	public static final String initDataInfo=ZXDataMapper.class.getName()+"-initDataInfo";
	public static final String initDatax=ZXDataMapper.class.getName()+"-initDatax";
	public static final String initDataxtru=ZXDataMapper.class.getName()+"-initDataxtru";
	public static final String initDataxBak=ZXDataMapper.class.getName()+"-initDataxBak";
	public static final String selectByG2T=ZXDataMapper.class.getName()+"-selectByG2T";
	public static final String autuUpdateG2T=ZXDataMapper.class.getName()+"-autuUpdateG2T";
	public static final String insertG2T=ZXDataMapper.class.getName()+"-insertG2T";
	public DataModel initInsert() {
		DataModel cm =new DataModel();
		cm.setSql("insert into ${table} (`key`,`value`,createtime,etime) values(?,?,?,?)");
		cm.setGroup(insert);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel initUpdate() {
		DataModel cm =new DataModel();
		cm.setSql("update ${table} set `value`=? , `version`=`version`+1 where `key`=? and `version`=?");
		cm.setGroup(update);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel updateNoLock() {
		DataModel cm =new DataModel();
		cm.setSql("update ${table} set `value`=? where `key`=?");
		cm.setGroup(updateNoLock);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel initDelete() {
		DataModel cm =new DataModel();
		cm.setSql("delete from ${table} where `key`=?");
		cm.setGroup(delete);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel initSelect() {
		DataModel cm =new DataModel();
		cm.setSql("select * from ${table} where `key`=? and (etime is null or etime > now())");
		cm.setGroup(select);
		cm.setResultClass(ZXData.class);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel deleteByETime() {
		DataModel cm =new DataModel();
		cm.setSql("delete from ${table} where etime < now()");
		cm.setGroup(deleteByETime);
		cm.setResultClass(ZXData.class);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel initData() {
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
		cm.setGroup(initData);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel initDataInfo() {
		DataModel cm =new DataModel();
		cm.setSql("insert  into zxdatag2t(`k`,`v`) values (-1,0);");
		cm.setGroup(initDataInfo);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel initDatax() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS zxdata${code} (\r\n" + 
				"  `key` char(36) NOT NULL,\r\n" + 
				"  `value` text NOT NULL,\r\n" + 
				"  `createtime` datetime NOT NULL,\r\n" + 
				"  `etime` datetime DEFAULT NULL,\r\n" + 
				"  `version` int(11) NOT NULL DEFAULT '0',\r\n" + 
				"  PRIMARY KEY (`key`),\r\n" + 
				"  KEY `eTimeKEY` (`etime`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8 \r\n"+ 
				"partition by key(`key`)\r\n" + 
				"partitions 100;");
		cm.setGroup(initDatax);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel initDataxtru() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TRIGGER tru_res_zxdata${code} AFTER DELETE ON zxdata${code} FOR EACH ROW\r\n" + 
				"BEGIN\r\n" + 
				"	INSERT INTO zxdatabak SET\r\n" + 
				"		`key` = OLD.key,\r\n" + 
				"		`value` = OLD.value,\r\n" + 
				"		createtime = OLD.createtime,\r\n" + 
				"		etime = OLD.etime,\r\n" + 
				"		version = OLD.version;\r\n" + 
				"END;");
		cm.setGroup(initDataxtru);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel initDataxBak() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS zxdatabak (\r\n" + 
				"  `key` char(36) NOT NULL,\r\n" + 
				"  `value` text NOT NULL,\r\n" + 
				"  `createtime` datetime NOT NULL,\r\n" + 
				"  `etime` datetime DEFAULT NULL,\r\n" + 
				"  `version` int(11) NOT NULL DEFAULT '0',\r\n" + 
				"  KEY (`key`)\r\n" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8 \r\n"+ 
				"partition by key(`key`)\r\n" + 
				"partitions 1000;");
		cm.setGroup(initDataxBak);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel selectByG2T() {
		DataModel cm =new DataModel();
		cm.setSql("select v from zxdatag2t where k=?");
		cm.setGroup(selectByG2T);
		cm.setResultClass(Integer.class);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel autuUpdateG2T() {
		DataModel cm =new DataModel();
		cm.setSql("update zxdatag2t set `v`=`v`+1 where `k`=-1");
		cm.setGroup(autuUpdateG2T);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	public DataModel insertG2T() {
		DataModel cm =new DataModel();
		cm.setSql("insert  into zxdatag2t(`k`,`v`) values (?,?)");
		cm.setGroup(insertG2T);
		cm.setDsClass(ZXData.class);
		return cm;
	}
	
}

