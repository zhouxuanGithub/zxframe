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
package zxframe.properties.service;




import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.jpa.dao.MysqlTemplate;
import zxframe.properties.mapper.PropertiesMapper;
import zxframe.properties.model.Properties;

@Service
public class PropertiesService{
	@Resource
	private MysqlTemplate baseDao;
	
	public List<Properties> getList() {
		return baseDao.getList(PropertiesMapper.propertiesAll);
	}
	
	public String getListVersion() {
		return (String) baseDao.get(PropertiesMapper.propertiesByKey,"system-version");
	}
	
	public void updateProperties(String key,String value){
		baseDao.execute(PropertiesMapper.propertiesUpdateValue,value,key);
		baseDao.execute(PropertiesMapper.propertiesVersionAdd);
	}
	public void initPropertiesTable() {
		baseDao.execute(PropertiesMapper.initPropertiesTable);
		try {
			baseDao.execute(PropertiesMapper.initPropertiesTableInfo);
		} catch (Exception e) {
		}
	}
}
