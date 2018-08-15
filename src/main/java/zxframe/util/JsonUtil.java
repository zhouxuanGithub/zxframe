package zxframe.util;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static HashMap obj2Map(Object object) {
		return byte2Obj(HashMap.class, obj2Byte(object));
	}
    public static String obj2Json(Object object) {
    	if(object==null) {
    		return "";
    	}
    	if(object instanceof String || object instanceof Integer || object instanceof Float|| object instanceof Double) {
    		return object.toString();
    	}
        String json = null;
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static <T> T json2Obj(Class<T> c,String json) {
        T t = null;
        try {
            t = mapper.readValue(json, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
    public static byte[] obj2Byte(Object object) {
    	byte[] json = null;
        try {
            json = mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
    public static <T> T byte2Obj(Class<T> c,byte[] json) {
        T t = null;
        try {
            t = mapper.readValue(json, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
}
