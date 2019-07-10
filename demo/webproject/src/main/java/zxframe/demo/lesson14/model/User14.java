package zxframe.demo.lesson14.model;

import java.io.Serializable;

import zxframe.cache.annotation.Cache;
import zxframe.jpa.annotation.Id;
import zxframe.jpa.annotation.Model;
import zxframe.jpa.annotation.Version;

@Cache(rcCache=true,lcCache=false)
@Model
public class User14 implements Serializable{
	@Id
	private String id;
	private String name;
	private int age;
	@Version
	private int version;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
