package zxframe.cache.model;

import java.io.Serializable;
import java.util.List;

import zxframe.config.ZxFrameConfig;

/**
 * 缓存模型
 * @author zx
 *
 */
public class CacheModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 使用本地缓存
	 */
	private	boolean lcCache=false;
	/**
	 * 使用远程缓存
	 */
	private boolean rcCache=false;
	/**
	 * group 缓存组，用于主动删除缓存使用
	 */
	private String group="default";
	/**
	 * 远程缓存缓存时长，单位秒，默认2小时
	 */
	private int rcETime=7200;
	/**
	 * 是否严格读写
	 * 严格读写，存在缓存组删除就得开启
	 * 非严格读写(默认)，不存在缓存组删除就可以关闭，提升效率
	 */
	private boolean strictRW=false;
//	/**
//	 * 读写数据类型
//	 */
//	private Class rwClass=List.class;
	/**
	 * 是否支持查询缓存
	 */
	private boolean queryCache=true;
	/**
	 * 查询缓存SQL
	 */
	private String sql;
	
	public boolean isLcCache() {
		return lcCache;
	}
	public void setLcCache(boolean lcCache) {
		this.lcCache = lcCache;
	}
	public boolean isRcCache() {
		if(ZxFrameConfig.ropen) {
			return rcCache;
		}else {
			return false;
		}
	}
	public void setRcCache(boolean rcCache) {
		this.rcCache = rcCache;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public int getRcETime() {
		return rcETime;
	}
	public void setRcETime(int rcETime) {
		this.rcETime = rcETime;
	}
	public boolean isStrictRW() {
		return strictRW;
	}
	public void setStrictRW(boolean strictRW) {
		this.strictRW = strictRW;
	}
//	public Class getRwClass() {
//		return rwClass;
//	}
//	public void setRwClass(Class rwClass) {
//		this.rwClass = rwClass;
//	}
	public boolean isQueryCache() {
		return queryCache;
	}
	public void setQueryCache(boolean queryCache) {
		this.queryCache = queryCache;
	}
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	@Override
	public String toString() {
		return "[strictRW=" + strictRW + ", lcCache=" + lcCache + ", rcCache=" + rcCache + ", rcETime=" + rcETime  + "]";
	}
}
