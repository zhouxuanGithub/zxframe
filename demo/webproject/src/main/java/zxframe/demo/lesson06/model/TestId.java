package zxframe.demo.lesson06.model;

import zxframe.jpa.annotation.Id;
import zxframe.jpa.annotation.Model;

//id自增长测试
@Model
public class TestId {
	@Id(auto=true)
	private int id;
	private String value;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "TestId [id=" + id + ", value=" + value + "]";
	}
}
