package zxframe.properties.service;




import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.jpa.dao.BaseDao;
import zxframe.properties.model.Properties;

@Service
public class PropertiesService{
	@Resource
	private BaseDao baseDao;
	
	public List<Properties> getList() {
		return baseDao.getList(Properties.class,"select * from Properties");
	}
	
	public String getListVersion() {
		return baseDao.get(String.class,"select value from Properties where `key`= ?","system-version");
	}
}
