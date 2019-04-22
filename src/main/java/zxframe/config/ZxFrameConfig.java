package zxframe.config;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;  
 
public class ZxFrameConfig {
	//是否输出普通日志
	public static boolean showlog=false;
	//控制台输出缓存信息
	public static boolean showcache=false;
	//控制台输出sql
	public static boolean showsql=false;
	//是否使用数据库配置properties
	public static boolean useDBProperties=false;
	//是否使用zxTask
	public static boolean useZXTask=false;
	//数据源公共配置
	public static ConcurrentMap<String, String> common=new ConcurrentHashMap<String, String>();
	//数据源具体配置
	public static ConcurrentMap<String, ArrayList<ConcurrentHashMap<String, String>>> datasources=new ConcurrentHashMap<String, ArrayList<ConcurrentHashMap<String, String>>>();
	//redis open
	public static boolean ropen=false;
	//redis password
	public static String rPassword;
	//redis key前缀
	public static String rKeyPrefix="default";
	//redis clusters;
	public static String rClusters;
	public static void loadZxFrameConfig() {
		 //1.获取jaxp工厂  
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {  
            //2.获取解析器  
            DocumentBuilder builder = factory.newDocumentBuilder();  
            //3.用解析器加载xml文档--->Document  
            Document document = builder.parse(new ClassPathResource("zxframe.xml").getInputStream());  
            //4.获得zxframe
            Element root = document.getDocumentElement();
            if(root.getElementsByTagName("showlog").getLength()>0) {
            	showlog=root.getElementsByTagName("showlog").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            }
            if(root.getElementsByTagName("showsql").getLength()>0) {
            	showsql=root.getElementsByTagName("showsql").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            }
            if(root.getElementsByTagName("showcache").getLength()>0) {
            	showcache=root.getElementsByTagName("showcache").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            }
            if(root.getElementsByTagName("useDBProperties").getLength()>0) {
            	useDBProperties=root.getElementsByTagName("useDBProperties").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            }
            if(root.getElementsByTagName("useZXTask").getLength()>0) {
            	useZXTask=root.getElementsByTagName("useZXTask").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            }
            if(root.getElementsByTagName("druid").getLength()>0) {
            	Element commonItem = (Element)root.getElementsByTagName("common").item(0);
                NodeList commonNL = commonItem.getChildNodes();
                for(int i=0;i<commonNL.getLength();i++ ) {
                	Node node = commonNL.item(i);
                	if(node.getNodeType() == Node.ELEMENT_NODE){  
                		Element child = (Element) node;  
                		common.put(child.getNodeName(), child.getTextContent());
                	}
                }
                NodeList datasourceNodeList = root.getElementsByTagName("datasource");
                for(int j=0;j<datasourceNodeList.getLength();j++ ) {
                	if(datasourceNodeList.item(j).getNodeType() != Node.ELEMENT_NODE){
                		continue;
                	}
                	Element datasourceItem = (Element)datasourceNodeList.item(j);
                	String key=datasourceItem.getElementsByTagName("dsname").item(0).getFirstChild().getNodeValue();
                	ArrayList<ConcurrentHashMap<String, String>> list = datasources.get(key);
            		if(list==null) {
            			list=new ArrayList<ConcurrentHashMap<String, String>>();
            			datasources.put(key, list);
            		}
            		ConcurrentHashMap concurrentHashMap=new ConcurrentHashMap<String, String>();
            		list.add(concurrentHashMap);
            		NodeList datasourceNL = datasourceItem.getChildNodes();
                    for(int i=0;i<datasourceNL.getLength();i++ ) {
                    	Node node = datasourceNL.item(i);
                    	if(node.getNodeType() == Node.ELEMENT_NODE){  
                    		Element child = (Element) node;  
                    		concurrentHashMap.put(child.getNodeName(), child.getTextContent());
                    	}
                    }
                }
            }
//            eopen=root.getElementsByTagName("ehcache").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            if(root.getElementsByTagName("redis").getLength()>0) {
            	NodeList childNodes = root.getElementsByTagName("redis").item(0).getChildNodes();
                for(int i=0;i<childNodes.getLength();i++ ) {
                	Node node = childNodes.item(i);
                	if(node.getNodeType() == Node.ELEMENT_NODE){  
                		Element child = (Element) node;
                		if(child.getNodeName().equals("open")) {
                			ropen=child.getTextContent().equals("true")?true:false;
                		}else if(child.getNodeName().equals("clusters")) {
                			rClusters=child.getTextContent();
                		}else if(child.getNodeName().equals("password")) {
                			rPassword=child.getTextContent();
                		}else if(child.getNodeName().equals("keyPrefix")) {
                			rKeyPrefix=child.getTextContent();
                		}
                	}
                }
            }
        } catch (Exception e) {  
            e.printStackTrace();  
            System.err.println("zxframe.xml文件读取失败！");
        }  
	}
}
