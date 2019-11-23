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
package zxframe.data.mgr;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.config.ZxFrameConfig;
import zxframe.data.model.ZXData;
import zxframe.jpa.dao.MysqlTemplate;
import zxframe.jpa.ex.DataExpiredException;

@Service
public class ZXDataTemplate extends MysqlTemplate{

	@Resource
	private MysqlTemplate mysqlTemplate;
	
	public void put(String key, String value) {
		put("zxdata",key,value);
	}
	public void put(String mark,String key, String value) {
		if(!ZxFrameConfig.useZXData) {
			return;
		}
		String[] tc = getTableCode(key);
		String sql="insert into "+mark+tc[0]+".data"+tc[1]+" (`key`,`value`,`version`) values(?,?,0)";
		mysqlTemplate.executeBySql(mark+tc[0], sql, key,value);
	}
	
	public int update(String key, String value) {
		return update("zxdata",key,value);
	}
	public int update(String mark,String key, String value) {
		return update(mark,key,value,null);
	}
	public int update(String key, String value,Integer version) {
		return update("zxdata",key,value,version);
	}
	public int update(String mark,String key, String value,Integer version) {
		if(!ZxFrameConfig.useZXData) {
			return 0;
		}
		int rcount= 0;
		String[] tc = getTableCode(key);
		if(version==null) {
			String sql="update "+mark+tc[0]+".data"+tc[1]+" set `value`=? where `key`=?";
			rcount= (int)mysqlTemplate.executeBySql(mark+tc[0], sql, value,key);
		}else {
			String sql="update "+mark+tc[0]+".data"+tc[1]+" set `value`=? where `key`=? and `version`=?";
			rcount= (int)mysqlTemplate.executeBySql(mark+tc[0], sql, value,key,version);
			if(rcount<1) {
				//版本控制出现问题
				throw new DataExpiredException("数据 version 已过期。");
			}
		}
		return rcount;
	}
	public void remove(String key) {
		remove("zxdata",key);
	}
	public void remove(String mark,String key) {
		if(!ZxFrameConfig.useZXData) {
			return;
		}
		String[] tc = getTableCode(key);
		String sql="delete from "+mark+tc[0]+".data"+tc[1]+" where `key`=?";
		mysqlTemplate.executeBySql(mark+tc[0], sql, key);
	}
	public String get(String key) {
		return get("zxdata",key);
	}
	public String get(String mark,String key) {
		ZXData zxData = getZXData(mark,key);
		if(zxData==null) {
			return null;
		}
		return zxData.getValue();
	}
	public ZXData getZXData(String key) {
		return getZXData("zxdata",key);
	}
	public ZXData getZXData(String mark,String key) {
		String[] tc = getTableCode(key);
		String sql="select * from "+mark+tc[0]+".data"+tc[1]+" where `key`=? ";
		List<ZXData> zxdatas = mysqlTemplate.getListBySql(mark+tc[0], ZXData.class, sql, key);
		if(zxdatas==null||zxdatas.size()==0) {
			return null;
		}
		return zxdatas.get(0);
	}
	public String[] getTableCode(String key) {
		String hashCode = String.valueOf(key.hashCode());
		int length = hashCode.length();
		if(length<3) {
			hashCode="000"+hashCode;
			length=length+3;
		}
		String[] r={hashCode.substring(length-1,length),hashCode.substring(length-3,length-1)};
		return r;
	}
}