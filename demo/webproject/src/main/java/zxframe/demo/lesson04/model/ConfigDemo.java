package zxframe.demo.lesson04.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config-attributes")
public class ConfigDemo {
	private String value;
    private String[] valueArray;
    private List<String> valueList;
    private HashMap<String, String> valueMap;
    private List<Map<String, String>> valueMapList;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String[] getValueArray() {
		return valueArray;
	}
	public void setValueArray(String[] valueArray) {
		this.valueArray = valueArray;
	}
	public List<String> getValueList() {
		return valueList;
	}
	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}
	public HashMap<String, String> getValueMap() {
		return valueMap;
	}
	public void setValueMap(HashMap<String, String> valueMap) {
		this.valueMap = valueMap;
	}
	public List<Map<String, String>> getValueMapList() {
		return valueMapList;
	}
	public void setValueMapList(List<Map<String, String>> valueMapList) {
		this.valueMapList = valueMapList;
	}
	@Override
	public String toString() {
		return "ConfigDemo [value=" + value + ", valueArray=" + Arrays.toString(valueArray) + ", valueList=" + valueList
				+ ", valueMap=" + valueMap + ", valueMapList=" + valueMapList + "]";
	}
}
