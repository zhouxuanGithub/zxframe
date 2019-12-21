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
package zxframe.jpa.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.jpa.dao.MysqlTemplate;

@Service
public class BaseService{
	@Resource
	MysqlTemplate mysqlTemplate;
	public Serializable save(Object obj) {
		return mysqlTemplate.save(obj);
	}
	public <T> T get(Class<T> clas, Serializable id) {
		return mysqlTemplate.get(clas, id);
	}
	public Object get(String group,Object... args) {
		return mysqlTemplate.get(group, args);
	}
	public Object get(String group,Map map,Object... args) {
		return mysqlTemplate.get(group, map, args);
	}
	public void delete(Class clas, Serializable id) {
		mysqlTemplate.delete(clas, id);
	}
	public void update(Object obj,String... fields) {
		mysqlTemplate.update(obj, fields);
	}
	public void updateNoLock(Object obj,String... fields) {
		mysqlTemplate.updateNoLock(obj, fields);
	}
	public List getList(String group, Object... args) {
		return mysqlTemplate.getList(group, args);
	}
	public List getList(String group,Map map, Object... args) {
		return mysqlTemplate.getList(group, map, args);
	}
	public <T> List<T> getListBySql(String dsname,Class<T> clas,String sql, Object... args) {
		return mysqlTemplate.getListBySql(dsname, clas, sql, args);
	}
	public Object execute(String group, Object... args) {
		return mysqlTemplate.execute(group, args);
	}
	public Object execute(String group,Map map, Object... args) {
		return mysqlTemplate.execute(group, map, args);
	}
	public Object executeBySql(String dsname,String sql, Object... args) {
		return mysqlTemplate.executeBySql(dsname, sql, args);
	}
	public Object executeBatch(String group) {
		return mysqlTemplate.executeBatch(group);
	}
	public Object executeBatch(String group,Map map) {
		return mysqlTemplate.executeBatch(group, map);
	}
	public Object executeBatchBySqlList(String dsname,List<String> sqls) {
		return mysqlTemplate.executeBatchBySqlList(dsname, sqls);
	}
}
