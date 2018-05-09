package zxframe.config;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import zxframe.aop.ServiceAspect;  
 

public class ZxFrameConfig {
	//是否输出系统日志
	public static boolean showlog=false;
	//控制台输出sql
	public static boolean showsql=false;
	//传播试事务方法前缀
	public static String txAdvice;
	//数据源公共配置
	public static ConcurrentMap<String, String> common=new ConcurrentHashMap<String, String>();
	//数据源具体配置
	public static ConcurrentMap<String, ArrayList<ConcurrentHashMap<String, String>>> datasources=new ConcurrentHashMap<String, ArrayList<ConcurrentHashMap<String, String>>>();
	//ehcache open
//	public static boolean eopen=false;
	//redis open
	public static boolean ropen=false;
	//redis maxIdle
	public static int rMaxIdle;
	//redis maxTotal
	public static int rMaxTotal;
	//redis servers;
	public static ArrayList<String> rList=new ArrayList<String>();
	public static void loadZxFrameConfig() {
		 //1.获取jaxp工厂  
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {  
            //2.获取解析器  
            DocumentBuilder builder = factory.newDocumentBuilder();  
            //3.用解析器加载xml文档--->Document  
            Document document = builder.parse(new File(ClassLoader.getSystemResource("zxframe.xml").getPath()));  
            //4.获得zxframe
            Element root = document.getDocumentElement();
            showsql=root.getElementsByTagName("showsql").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            txAdvice=root.getElementsByTagName("txAdvice").item(0).getFirstChild().getNodeValue();
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
//            eopen=root.getElementsByTagName("ehcache").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            NodeList childNodes = root.getElementsByTagName("redis").item(0).getChildNodes();
            for(int i=0;i<childNodes.getLength();i++ ) {
            	Node node = childNodes.item(i);
            	if(node.getNodeType() == Node.ELEMENT_NODE){  
            		Element child = (Element) node;
            		if(child.getNodeName().equals("open")) {
            			ropen=child.getTextContent().equals("true")?true:false;
            		}else if(child.getNodeName().equals("servers")) {
            			NodeList childNodes2 = child.getChildNodes();
            			for(int j=0;j<childNodes2.getLength();j++ ) {
            				Node node2 = childNodes2.item(j);
                        	if(node2.getNodeType() == Node.ELEMENT_NODE){  
                        		Element child2 = (Element) node2;
                        		rList.add(child2.getTextContent());
                        	}
            			}
            		}else if(child.getNodeName().equals("maxIdle")) {
            			rMaxIdle=Integer.parseInt(child.getTextContent());
            		}else if(child.getNodeName().equals("maxTotal")) {
            			rMaxTotal=Integer.parseInt(child.getTextContent());
            		}
            		
            	}
            }
            ServiceAspect.requiredTx=txAdvice.split(",");
            ServiceAspect.rl=ServiceAspect.requiredTx.length;
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
}
