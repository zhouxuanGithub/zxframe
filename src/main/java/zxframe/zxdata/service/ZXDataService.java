package zxframe.zxdata.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.cache.annotation.FnCache;
import zxframe.jpa.dao.BaseDao;
import zxframe.jpa.ex.DataExpiredException;
import zxframe.util.DateUtil;
import zxframe.zxdata.mapper.ZXDataMapper;
import zxframe.zxdata.model.ZXData;

@Service
public class ZXDataService {
	//一个表内多少个槽
	public final static int grooveCount=50000;
	//槽内数据数量
	private static int grooveCNum=200;
	//总槽数
	private static int grooveLength=Integer.MAX_VALUE/grooveCNum+1;
	//槽和表对应关系
	private static Integer[] groove2table=new Integer[grooveLength];
	//表创建记录
	private static Integer[] tableHas=new Integer[215];
	@Resource
	private BaseDao baseDao;
	public void insert(String id, String group, String value, int seconds) {
		if(id==null||id.equals("")) {
			id=UUID.randomUUID().toString();
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(group,true));
		baseDao.execute(ZXDataMapper.insert,map,id,group,value,new Date(),seconds>0?DateUtil.addSecond(new Date(), seconds):null);
	}
	public int update(ZXData o) {
		return updateById(o.getId(), o.getG(), o.getV(),o.getVersion());
	}
	public int updateById(String id, String group, String value,Integer version) {
		int rcount= 0;
		ZXData o = selectById(id,group);
		if(o!=null) {
			Map<String,String> map=new HashMap<String,String>();
			map.put("table", "ZXData"+getTableCode(group,true));
			if(version==null) {
				rcount= (int) baseDao.execute(ZXDataMapper.updateByIdNoLock,map,value,id);
			}else {
				rcount= (int) baseDao.execute(ZXDataMapper.updateById,map,value,id,version);
				if(rcount<1) {
					//版本控制出现问题
					throw new DataExpiredException("数据 version 已过期。");
				}
			}
		}
		return rcount;
	}
	public void delete(ZXData o) {
		deleteById(o.getId(),o.getG());
	}
	public void deleteById(String id, String group) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(group,true));
		ZXData o = selectById(id,group);
		if(o!=null) {
			baseDao.execute(ZXDataMapper.deleteById,map,id);
		}
	}
	public void deleteByGroup(String group) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(group,true));
		List<ZXData> os = selectByGroup(group);
		if(os!=null) {
			for (int i = 0; i < os.size(); i++) {
				ZXData o = os.get(i);
				baseDao.execute(ZXDataMapper.deleteById,map,o.getId());
			}
		}
	}

	public ZXData selectById(String id, String group) {
		Map<String,String> map=new HashMap<String,String>();
		int tableCode = getTableCode(group,false);
		if(tableCode<0) {
			return null;
		}
		map.put("table", "ZXData"+tableCode);
		List list = baseDao.getList(ZXDataMapper.selectById, map,id );
		if(list.size()>0) {
			return (ZXData) list.get(0);
		}
		return null;
	}

	public List<ZXData> selectByGroup(String group) {
		Map<String,String> map=new HashMap<String,String>();
		int tableCode = getTableCode(group,false);
		if(tableCode<0) {
			return null;
		}
		map.put("table", "ZXData"+tableCode);
		return baseDao.getList(ZXDataMapper.selectByGroup, map,group);
	}
	@FnCache
	public ZXData selectCacheById(String id, String group) {
		return selectById(id,group);
	}
	@FnCache
	public List<ZXData> selectCacheByGroup(String group) {
		return selectCacheByGroup(group);
	}
	public void initDB() {
		baseDao.execute(ZXDataMapper.initZxdata);
		baseDao.execute(ZXDataMapper.initZxdataxBak);
		try {
			baseDao.execute(ZXDataMapper.initZxdataInfo);
		} catch (Exception e) {
		}
		//插入数据
//		for (int i = 0; i < 100; i++) {
//			insert(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), 60*5);
//		}
		//查询数据
//		ZXData z = selectById("094fc1e3-6b19-4c83-9b47-185a989f0f29", "c7414213-0aac-42e3-82b5-bddc3b7aadd5");
//		System.out.println(z);
//		//改数据
//		z.setV("ok");
//		updateById(z.getId(),z.getG(), z.getV(),null);
//		//删除数据
//		delete(z);
	}
	public Integer getMaxTableCode() {
		Integer t = (Integer)baseDao.get(ZXDataMapper.selectByG2T,-1);
		t = t/grooveCount;
		return t;
	}
	//获得表code，创建必要的表
	private int getTableCode(String group,boolean newGroove) {
		int groove = Math.abs(group.hashCode())/grooveCNum;
		if(groove2table[groove]==null) {
			Integer t = (Integer)baseDao.get(ZXDataMapper.selectByG2T,groove);
			if(t==null) {
				if(newGroove) {
					//新槽保存
					baseDao.execute(ZXDataMapper.autuUpdateG2T);
					t=getMaxTableCode();
					try {
						baseDao.execute(ZXDataMapper.insertG2T, groove,t);
					} catch (Exception e) {
						try {
							Thread.sleep(1000);
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
						tableHas[t]=1;
						try {
							Map<String,String> map=new HashMap<String,String>();
							map.put("code", t.toString());
							baseDao.execute(ZXDataMapper.initZxdatax,map);
							baseDao.execute(ZXDataMapper.initZxdataxtru,map);
						} catch (Exception e) {
						}
					}
				}else {
					return -1;
				}
			}
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
