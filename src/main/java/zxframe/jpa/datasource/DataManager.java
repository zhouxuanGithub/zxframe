package zxframe.jpa.datasource;

import zxframe.config.ZxFrameConfig;
import zxframe.properties.service.PropertiesService;
import zxframe.util.ServiceLocator;

public class DataManager {
	//初始化数据
	public static void init() {
		if(ZxFrameConfig.useDBProperties) {
			((PropertiesService)ServiceLocator.getSpringBean("propertiesService")).initPropertiesTable();
		}
	}
}
