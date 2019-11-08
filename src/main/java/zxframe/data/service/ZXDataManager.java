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
package zxframe.data.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.cache.annotation.FnCache;
import zxframe.config.ZxFrameConfig;
import zxframe.jpa.dao.MysqlTemplate;
import zxframe.jpa.ex.DataExpiredException;
import zxframe.util.DateUtil;
import zxframe.util.LockStringUtil;
import zxframe.data.mapper.ZXDataMapper;
import zxframe.data.model.ZXData;

@Service
public class ZXDataManager {
	//一个表内多少个槽
	public final static int grooveCount=50000;
	//槽内数据数量
	private static int grooveCNum=200;
	//总槽数
	private static int grooveLength=Integer.MAX_VALUE/grooveCNum+1;
	//槽和表对应关系
	private static Integer[] groove2table=new Integer[grooveLength];
	//表创建记录
	private static Integer[] tableHas=new Integer[300];
	@Resource
	private MysqlTemplate baseDao;
	public void put(String key, String value) {
		put(key,value,-1);
	}
	public void put(String key, String value, int seconds) {
		if(!ZxFrameConfig.useZXData) {
			return;
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "zxdata"+getTableCode(key,true));
		baseDao.execute(ZXDataMapper.insert,map,key,value,new Date(),seconds>0?DateUtil.addSecond(new Date(), seconds):null);
	}
	public int update(ZXData o) {
		return update(o.getKey(), o.getValue(),o.getVersion());
	}
	public int update(String key, String value) {
		return update(key,value,null);
	}
	public int update(String key, String value,Integer version) {
		if(!ZxFrameConfig.useZXData) {
			return 0;
		}
		int rcount= 0;
		ZXData o = get(key);
		if(o!=null) {
			int tableCode = getTableCode(key,false);
			if(tableCode<0) {
				return 0;
			}
			Map<String,String> map=new HashMap<String,String>();
			map.put("table", "zxdata"+tableCode);
			if(version==null) {
				rcount= (int) baseDao.execute(ZXDataMapper.updateNoLock,map,value,key);
			}else {
				rcount= (int) baseDao.execute(ZXDataMapper.update,map,value,key,version);
				if(rcount<1) {
					//版本控制出现问题
					throw new DataExpiredException("数据 version 已过期。");
				}
			}
		}
		return rcount;
	}
	public void remove(ZXData o) {
		remove(o.getKey());
	}
	public void remove(String key) {
		if(!ZxFrameConfig.useZXData) {
			return;
		}
		int tableCode = getTableCode(key,false);
		if(tableCode<0) {
			return;
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "zxdata"+tableCode);
		baseDao.execute(ZXDataMapper.delete,map,key);
	}

	public ZXData get(String key) {
		if(!ZxFrameConfig.useZXData) {
			return null;
		}
		Map<String,String> map=new HashMap<String,String>();
		int tableCode = getTableCode(key,false);
		if(tableCode<0) {
			return null;
		}
		map.put("table", "zxdata"+tableCode);
		List list = baseDao.getList(ZXDataMapper.select, map,key);
		if(list.size()>0) {
			return (ZXData) list.get(0);
		}
		return null;
	}
	@FnCache
	public ZXData getDataFromCache(String key) {
		return get(key);
	}
	public void initDB() {
		baseDao.execute(ZXDataMapper.initData);
		baseDao.execute(ZXDataMapper.initDataxBak);
		try {
			baseDao.execute(ZXDataMapper.initDataInfo);
		} catch (Exception e) {
		}
	}
	public Integer getMaxTableCode() {
		Integer t = (Integer)baseDao.get(ZXDataMapper.selectByG2T,-1);
		t = t/grooveCount;
		return t;
	}
	//获得表code，创建必要的表
	private int getTableCode(String key,boolean newGroove) {
		int groove = Math.abs(key.hashCode())/grooveCNum;
		if(groove2table[groove]==null) {
			Integer t = (Integer)baseDao.get(ZXDataMapper.selectByG2T,groove);
			if(t==null) {
				if(newGroove) {
					//synchronized (LockStringUtil.getLock(String.valueOf(groove))) {
						//t = (Integer)baseDao.get(ZXDataMapper.selectByG2T,groove);
						//if(t==null) {
							//新槽保存
							baseDao.execute(ZXDataMapper.autuUpdateG2T);
							t=getMaxTableCode();
							try {
								baseDao.execute(ZXDataMapper.insertG2T, groove,t);
							} catch (Exception e) {
								try {
									Thread.sleep(200);
								} catch (InterruptedException e1) {
								}
								//主键重复报错，说明可以重新去获取
								t = (Integer) baseDao.get(ZXDataMapper.selectByG2T,groove);
								if(t==null) {
									throw e;
								}
							}
							//表创建
							if(tableHas[t]==null) {
								try {
									Map<String,String> map=new HashMap<String,String>();
									map.put("code", t.toString());
									baseDao.execute(ZXDataMapper.initDatax,map);
									baseDao.execute(ZXDataMapper.initDataxtru,map);
								} catch (Exception e) {
								}
							}
						//}
					//}
				}else {
					return -1;
				}
			}
			tableHas[t]=1;
			groove2table[groove]=t;
		}
		return groove2table[groove];
	}
	public void deleteByETime(String table) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", table);
		baseDao.execute(ZXDataMapper.deleteByETime, map);
	}
}