package zxframe.demo.lesson11.model;

import java.io.Serializable;

import zxframe.cache.annotation.Cache;
import zxframe.jpa.annotation.Id;
import zxframe.jpa.annotation.Model;

@Cache(rcCache=true,lcCache=false)
@Model
public class User11 implements Serializable{
	@Id
	private String id;
	private String name;
	private int age;
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
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
