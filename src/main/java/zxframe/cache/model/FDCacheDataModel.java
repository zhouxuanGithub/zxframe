package zxframe.cache.model;

import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.model.DataModel;

@DataModelScanning
public class FDCacheDataModel {
	public static String zxframeFdcacheDefault="zxframe-fdcache-default";
	public DataModel initZxframeFdcacheDefault() {
		DataModel cm =new DataModel();
		cm.setLcCacheDataClone(false);
		cm.setLcCache(true);
		cm.setRcCache(true);
		return cm;
	}
}
