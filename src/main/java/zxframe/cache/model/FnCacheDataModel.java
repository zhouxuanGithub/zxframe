package zxframe.cache.model;

import zxframe.jpa.annotation.DataModelScanning;
import zxframe.jpa.model.DataModel;

@DataModelScanning
public class FnCacheDataModel {
	public static String zxframeFncacheDefault="zxframe-fncache-default";
	public DataModel initZxframeFdcacheDefault() {
		DataModel cm =new DataModel();
		cm.setGroup(zxframeFncacheDefault);
		cm.setLcCache(false);
		cm.setRcCache(true);
		return cm;
	}
}
