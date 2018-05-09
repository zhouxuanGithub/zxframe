package zxframe.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtils {
	public static byte[] serialize(Object obj) {
		if(obj==null)return null;
		
		ByteArrayOutputStream baos=null;
		ObjectOutputStream oos=null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(baos!=null){
					baos.close();
				}
				if(oos!=null){
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Object deSerialize(byte[] bytes) {
		if(bytes==null)return null;
		
		ByteArrayInputStream bais=null;
		ObjectInputStream ois =null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(bais!=null){
					bais.close();
				}
				if(ois!=null){
					ois.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
