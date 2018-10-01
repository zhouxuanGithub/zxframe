package zxframe.zxdata.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.jpa.dao.BaseDao;
import zxframe.jpa.ex.DataExpiredException;
import zxframe.util.DateUtil;
import zxframe.zxdata.mapper.ZXDataMapper;
import zxframe.zxdata.model.ZXData;

@Service
public class ZXDataService {
	@Resource
	private BaseDao baseDao;
	public void insert(String id, String group, String value, int seconds) {
		if(id==null||id.equals("")) {
			id=UUID.randomUUID().toString();
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(group));
		baseDao.execute(ZXDataMapper.insert,map,id,group,value,new Date(),seconds>0?DateUtil.addSecond(new Date(), seconds):null);
	}
	public void insertBak(String id, String group, String value,Date createTime, Date eTime) {
		if(id==null||id.equals("")) {
			id=UUID.randomUUID().toString();
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXDatabak"+getTableCode(group));
		baseDao.execute(ZXDataMapper.insert,map,id,group,value,createTime,eTime);
	}
	public int updateById(String id, String group, String value,String version) {
		int rcount= 0;
		ZXData o = selectById(id,group);
		if(o!=null) {
			Map<String,String> map=new HashMap<String,String>();
			map.put("table", "ZXData"+getTableCode(group));
			rcount= (int) baseDao.execute(ZXDataMapper.updateById,map,value,id,version);
			if(rcount<1) {
				//版本控制出现问题
				throw new DataExpiredException("数据 version 已过期。");
			}
		}
		return rcount;
	}
	public void delete(ZXData o) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(o.getGroup()));
		if(o!=null) {
			baseDao.execute(ZXDataMapper.deleteById,map,o.getId());
			insertBak(o.getId(), o.getGroup(), o.getValue(), o.getCreateTime(),o.geteTime());
		}
	}
	public void deleteById(String id, String group) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(group));
		ZXData o = selectById(id,group);
		if(o!=null) {
			baseDao.execute(ZXDataMapper.deleteById,map,id);
			insertBak(id, group, o.getValue(), o.getCreateTime(),o.geteTime());
		}
	}
	public void deleteByGroup(String group) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(group));
		List<ZXData> os = selectByGroup(group);
		if(os!=null) {
			for (int i = 0; i < os.size(); i++) {
				ZXData o = os.get(i);
				baseDao.execute(ZXDataMapper.deleteById,map,o.getId());
				insertBak(o.getId(), group, o.getValue(), o.getCreateTime(),o.geteTime());
			}
		}
	}

	public ZXData selectById(String id, String group) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(group));
		return (ZXData) baseDao.getList(ZXDataMapper.selectById, map,id );
	}

	public List<ZXData> selectByGroup(String group) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("table", "ZXData"+getTableCode(group));
		return baseDao.getList(ZXDataMapper.selectByGroup, map,group );
	}
	public void initDB() {
		baseDao.execute(ZXDataMapper.initZxdata);
		try {
			baseDao.execute(ZXDataMapper.initZxdataInfo);
		} catch (Exception e) {
		}
	}
	//获得表code，创建必要的表
	private int getTableCode(String group) {
		int hashCode = group.hashCode();
		return hashCode;
	}
}
