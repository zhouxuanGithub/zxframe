package zxframe.properties.service;




import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import zxframe.jpa.dao.BaseDao;
import zxframe.properties.mapper.PropertiesMapper;
import zxframe.properties.model.Properties;

@Service
public class PropertiesService{
	@Resource
	private BaseDao baseDao;
	
	public List<Properties> getList() {
		return baseDao.getList(PropertiesMapper.propertiesAll);
	}
	
	public String getListVersion() {
		return (String) baseDao.get(PropertiesMapper.propertiesByKey,"system-version");
	}
}
