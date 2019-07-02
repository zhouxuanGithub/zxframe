package zxframe.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zxframe.config.ZxFrameConfig;

/**
 * 缓存模型
 * @author zx
 *
 */
public class DataModel implements Serializable{

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
	 * 单模型的group值为clazz.getName()
	 */
	private String group="default";
	/**
	 * 远程缓存缓存时长，单位秒
	 */
	private int rcETime=1200;
	/**
	 * 是否严格读写
	 * 严格读写，存在缓存组删除就得开启
	 * 非严格读写(默认)，不存在缓存组删除就可以关闭，提升效率
	 */
	private boolean strictRW=false;
	/**
	 * 是否支持查询缓存
	 */
	private boolean queryCache=true;
	/**
	 * 查询缓存SQL
	 */
	private String sql;
	/**
	 * 执行此数据模型，则删除对应的组
	 * 可传Class对象，具体组String
	 */
	private List<Object> flushOnExecute;
	/**
	 * 返回结果class
	 */
	private Class resultClass;
	/**
	 * 使用的数据源class
	 * 使用数据源的优先顺序1.dsClass 2.resultClass 3.默认数据源
	 */
	private Class dsClass;
	/**
	 * 本地缓存里的数据每次都是克隆取出，一份新的数据
	 */
	private boolean lcCacheDataClone=false;
	
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
	
	public List<Object> getFlushOnExecute() {
		return flushOnExecute;
	}
	public void addFlushOnExecute(Object group) {
		if(this.flushOnExecute==null) {
			this.flushOnExecute=new ArrayList<Object>();
		}
		this.flushOnExecute.add(group);
	}
	public Class getResultClass() {
		return resultClass;
	}
	public void setResultClass(Class resultClass) {
		this.resultClass = resultClass;
	}
	
	public Class getDsClass() {
		return dsClass;
	}
	public void setDsClass(Class dsClass) {
		this.dsClass = dsClass;
	}
	public boolean isLcCacheDataClone() {
		return lcCacheDataClone;
	}
	public void setLcCacheDataClone(boolean lcCacheDataClone) {
		this.lcCacheDataClone = lcCacheDataClone;
	}
	@Override
	public String toString() {
		return " [lcCache=" + lcCache + ", rcCache=" + rcCache + ", group=" + group + ", rcETime=" + rcETime
				+ ", strictRW=" + strictRW + ", queryCache=" + queryCache + ", flushOnExecute=" + flushOnExecute
				+ ", resultClass=" + resultClass + ", dsClass=" + dsClass + "]";
	}
	
}
