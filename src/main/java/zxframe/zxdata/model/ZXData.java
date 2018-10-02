package zxframe.zxdata.model;

import java.util.Date;

import zxframe.jpa.annotation.Model;

@Model(dsname="")
public class ZXData {
	private String id;//长度36，可自动生成和指定值
	private String g;//组
	private String v;//值
	private Date createTime;//创建时间
	private Date eTime;//过期时间
	private int version;//版本号
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getG() {
		return g;
	}
	public void setG(String g) {
		this.g = g;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	@Override
	public String toString() {
		return "ZXData [id=" + id + ", g=" + g + ", v=" + v + ", createTime=" + createTime + ", eTime=" + eTime
				+ ", version=" + version + "]";
	}
}
