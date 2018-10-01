package zxframe.zxdata.model;

import java.util.Date;

import zxframe.jpa.annotation.Model;

@Model(dsname="")
public class ZXData {
	private String id;//长度36，可自动生成和指定值
	private String group;//组
	private String value;//值
	private Date createTime;//创建时间
	private Date eTime;//过期时间
	private int version;//版本号
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
}
