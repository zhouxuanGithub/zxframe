package zxframe.demo.lesson06.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import zxframe.jpa.annotation.Id;
import zxframe.jpa.annotation.Model;
import zxframe.jpa.annotation.Transient;
import zxframe.jpa.annotation.Version;

@Model
public class User implements Serializable{
	@Id
	private String id;
	private String name;
	private int age;
	private Date birthday;//yyyy-MM-dd
	private Timestamp updatetime;//yyyy-MM-dd HH:mm:ss
	@Version //乐观锁
	private int version;
	@Transient//不参与数据库操作的字段注解
	private String alias;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", birthday=" + birthday + ", updatetime="
				+ updatetime + ", version=" + version + ", alias=" + alias + "]";
	}
}
