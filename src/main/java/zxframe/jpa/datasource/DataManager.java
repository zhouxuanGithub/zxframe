package zxframe.jpa.datasource;

import zxframe.config.ZxFrameConfig;
import zxframe.properties.service.PropertiesService;
import zxframe.util.ServiceLocator;
import zxframe.zxdata.service.ZXDataService;

public class DataManager {
	//初始化数据
	public static void init() {
		if(ZxFrameConfig.useDBProperties) {
			((PropertiesService)ServiceLocator.getSpringBean("propertiesService")).initPropertiesTable();
		}
		if(ZxFrameConfig.useZXData) {
			((ZXDataService)ServiceLocator.getSpringBean("ZXDataService")).initDB();
		}
	}
}
