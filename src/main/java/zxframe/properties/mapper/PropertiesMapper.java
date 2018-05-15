package zxframe.properties.mapper;


import zxframe.jpa.model.DataModel;
import zxframe.jpa.annotation.DataMapper;
import zxframe.properties.model.Properties;

@DataMapper
public class PropertiesMapper {
	//查询所有配置
	public static final String propertiesAll=Properties.class.getName()+"-propertiesAll";
	//根据key查询
	public static final String propertiesByKey=Properties.class.getName()+"-propertiesByKey";
	public DataModel initPropertiesAll() {
		DataModel cm =new DataModel();
		cm.setSql("select * from Properties");
		cm.setGroup(propertiesAll);
		return cm;
	}
	public DataModel initPropertiesByKey() {
		DataModel cm =new DataModel();
		cm.setSql("select value from Properties where `key`= ?");
		cm.setGroup(propertiesByKey);
		return cm;
	}
}
