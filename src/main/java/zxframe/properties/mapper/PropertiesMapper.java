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
package zxframe.properties.mapper;


import zxframe.jpa.model.DataModel;
import zxframe.jpa.annotation.DataModelScanning;
import zxframe.properties.model.Properties;

@DataModelScanning
public class PropertiesMapper {
	//查询所有配置
	public static final String propertiesAll=Properties.class.getName()+"-propertiesAll";
	//根据key查询
	public static final String propertiesByKey=Properties.class.getName()+"-propertiesByKey";
	//根据key更新value
	public static final String propertiesUpdateValue=Properties.class.getName()+"-propertiesUpdateValue";
	//版本递增
	public static final String propertiesVersionAdd=Properties.class.getName()+"-propertiesVersionAdd";
	public static final String initPropertiesTable=Properties.class.getName()+"-initPropertiesTable";
	public static final String initPropertiesTableInfo=Properties.class.getName()+"-initPropertiesTableInfo";
	public DataModel initPropertiesAll() {
		DataModel cm =new DataModel();
		cm.setSql("select * from properties");
		cm.setGroup(propertiesAll);
		cm.setResultClass(Properties.class);
		return cm;
	}
	public DataModel initPropertiesByKey() {
		DataModel cm =new DataModel();
		cm.setSql("select value from properties where `key`= ?");
		cm.setGroup(propertiesByKey);
		cm.setDsClass(Properties.class);
		cm.setResultClass(String.class);
		return cm;
	}
	public DataModel initPropertiesUpdateValue() {
		DataModel cm =new DataModel();
		cm.setGroup(propertiesUpdateValue);
		cm.setSql("update properties set `value`=? where `key`=?");
		cm.setDsClass(Properties.class);
		return cm;
	}
	public DataModel initpropertiesVersionAdd() {
		DataModel cm =new DataModel();
		cm.setGroup(propertiesVersionAdd);
		cm.setSql("update properties set `value`=`value`+1 where `key`='system-version'");
		cm.setDsClass(Properties.class);
		return cm;
	}
	public DataModel initPropertiesTable() {
		DataModel cm =new DataModel();
		cm.setSql("CREATE TABLE IF NOT EXISTS `properties` (" + 
				"  `key` varchar(255) NOT NULL," + 
				"  `value` varchar(255) NOT NULL," + 
				"  `description` varchar(255) DEFAULT NULL," + 
				"  PRIMARY KEY (`key`)" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		cm.setGroup(initPropertiesTable);
		cm.setDsClass(Properties.class);
		return cm;
	}
	public DataModel initPropertiesTableInfo() {
		DataModel cm =new DataModel();
		cm.setSql("insert  into `properties`(`key`,`value`,`description`) values ('system-isTest','false','系统是否是测试模式'),('system-version','0','系统版本号，更改后1分钟内所有服务器使用最新properties配置');");
		cm.setGroup(initPropertiesTableInfo);
		cm.setDsClass(Properties.class);
		return cm;
	}
}
