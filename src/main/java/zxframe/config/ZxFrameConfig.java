/**
 * ZxFrame Java Library
 * https://github.com/zhouxuanGithub/zxframe
 *
 * Copyright (c) 2019 zhouxuan
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package zxframe.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import zxframe.cache.mgr.CacheModelManager;
import zxframe.jpa.inf.ISQLParse;
import zxframe.jpa.model.DataModel;
import zxframe.util.JsonUtil;
import zxframe.util.ServiceLocator;  
 
public class ZxFrameConfig {
	//是否输出普通日志
	public static boolean showlog=false;
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
	public static List<ISQLParse> sqlParselist;
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
            if(root.getElementsByTagName("useDBProperties").getLength()>0) {
            	useDBProperties=root.getElementsByTagName("useDBProperties").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            }
            if(root.getElementsByTagName("useZXTask").getLength()>0) {
            	useZXTask=root.getElementsByTagName("useZXTask").item(0).getFirstChild().getNodeValue().equals("true")?true:false;
            }
            if(root.getElementsByTagName("druid").getLength()>0 || root.getElementsByTagName("db").getLength()>0) {//druid保留，兼容历史版本
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
                if(root.getElementsByTagName("sqlParse").getLength()>0) {
                	sqlParselist=new ArrayList<>();
                	Element sqlParse = (Element)root.getElementsByTagName("sqlParse").item(0);
                    NodeList sqlParseNL = sqlParse.getChildNodes();
                    for(int i=0;i<sqlParseNL.getLength();i++ ) {
                    	Node node = sqlParseNL.item(i);
                    	if(node.getNodeType() == Node.ELEMENT_NODE){  
                    		Element child = (Element) node;  
                    		sqlParselist.add((ISQLParse)ServiceLocator.getSpringBean(child.getTextContent().trim()));
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
	public static void loadZxMapperConfig() {
		String filePath="";
		try {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] files = resolver.getResources("classpath*:mapper/*.xml");
			if(files!=null && files.length>0) {
				for(int i = 0;i<files.length;i++){
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder builder = factory.newDocumentBuilder();  
					Document document = builder.parse(files[i].getInputStream());
					filePath=document.getDocumentURI();
					Element root = document.getDocumentElement();
					String namespace = root.getAttribute("namespace");
					if(namespace==null||namespace.length()==0) {
						namespace="";
					}else {
						namespace=namespace+".";
					}
					NodeList datasourceNodeList = root.getElementsByTagName("sql");
	                for(int j=0;j<datasourceNodeList.getLength();j++ ) {
	                	if(datasourceNodeList.item(j).getNodeType() != Node.ELEMENT_NODE){
	                		continue;
	                	}
	                	Element item = (Element)datasourceNodeList.item(j);
	                	NamedNodeMap attributes = item.getAttributes();
	                	DataModel dm=new DataModel();
	                	dm.setGroup(namespace+attributes.getNamedItem("id").getNodeValue());
	                	dm.setLcCache(attributes.getNamedItem("lcCache")==null?false:attributes.getNamedItem("lcCache").getNodeValue().equals("true"));
	                	dm.setRcCache(attributes.getNamedItem("rcCache")==null?false:attributes.getNamedItem("rcCache").getNodeValue().equals("true"));
	                	dm.setStrictRW(attributes.getNamedItem("strictRW")==null?false:attributes.getNamedItem("strictRW").getNodeValue().equals("true"));
	                	dm.setLcCacheDataClone(attributes.getNamedItem("lcCacheDataClone")==null?false:attributes.getNamedItem("lcCacheDataClone").getNodeValue().equals("true"));
	                	if(attributes.getNamedItem("rcETime")!=null) {
	                		dm.setRcETime(Integer.parseInt(attributes.getNamedItem("rcETime").getNodeValue()));
	                	}
	                	if(attributes.getNamedItem("resultClass")!=null) {
	                		String nodeValue = attributes.getNamedItem("resultClass").getNodeValue();
	                		if(nodeValue==null) {
	                			nodeValue="map";
	                		}
	                		nodeValue=nodeValue.trim();
	                		if(nodeValue.toLowerCase().equals("int") || nodeValue.toLowerCase().equals("integer") ) {
	                			nodeValue="java.lang.Integer";
	                		}else if(nodeValue.toLowerCase().equals("float") ) {
	                			nodeValue="java.lang.Float";
	                		}else if(nodeValue.toLowerCase().equals("double") ) {
	                			nodeValue="java.lang.Double";
	                		}else if(nodeValue.toLowerCase().equals("string")) {
	                			nodeValue="java.lang.String";
	                		}else if(nodeValue.toLowerCase().equals("map") ) {
	                			nodeValue="java.util.HashMap";
	                		}else if(nodeValue.toLowerCase().equals("date") ) {
	                			nodeValue="java.util.Date";
	                		}
	                		dm.setResultClass(Class.forName(nodeValue));
	                	}
	                	if(attributes.getNamedItem("dsClass")!=null) {
	                		dm.setResultClass(Class.forName(attributes.getNamedItem("dsClass").getNodeValue()));
	                	}
	                	if(attributes.getNamedItem("flushOnExecute")!=null) {
	                		String nodeValue = attributes.getNamedItem("flushOnExecute").getNodeValue();
	                		String[] split = nodeValue.split(",");
	                		for (int k = 0; k < split.length; k++) {
	                			dm.addFlushOnExecute(split[k]);
							}
	                	}
	                	//标签的支持
	                	Map<String, Object> diyDataMap = dm.getDiyDataMap();
	                	NodeList includelist = item.getElementsByTagName("include");
	                	List<String> includeId=new ArrayList<>();
	                	for (int k = 0; k < includelist.getLength(); k++) {
	                		Element iitm = (Element) includelist.item(k);
	                		String refid = iitm.getAttribute("refid");
	                		if(namespace.length()>0) {
	                			if(!refid.startsWith(namespace)) {
	                				if(refid.indexOf(".")==-1) {
	                					refid=namespace+refid;
	                				}
	                			}
	                		}
	                		includeId.add(refid);
	                		Element tl=document.createElement("temp");
	                		tl.setTextContent("${mapper-include-"+refid+"}");
	                		item.replaceChild(tl, iitm);
	                	}
	                	if(includeId.size()>0) {
	                		diyDataMap.put("mapper-include-id", includeId);
	                	}
	                	
	                	List<String> testList=new ArrayList<>();
	                	Map<String, String> textMap =new ConcurrentHashMap<>();
	                	NodeList iflist = item.getElementsByTagName("if");
	                	for (int k = 0; k < iflist.getLength(); k++) {
	                		Element ifitm = (Element) iflist.item(k);
	                		String test = ifitm.getAttribute("test");
	                		test=test.replaceAll("\\$", "get");
	                		test=test.replaceAll("\\#", "get");
	                		
	                		test=test.replaceAll("\\{     ", "(\"");
	                		test=test.replaceAll("\\     }", "\")");
	                		test=test.replaceAll("\\{    ", "(\"");
	                		test=test.replaceAll("\\    }", "\")");
	                		test=test.replaceAll("\\{   ", "(\"");
	                		test=test.replaceAll("\\   }", "\")");
	                		test=test.replaceAll("\\{  ", "(\"");
	                		test=test.replaceAll("\\  }", "\")");
	                		test=test.replaceAll("\\{ ", "(\"");
	                		test=test.replaceAll("\\ }", "\")");
	                		test=test.replaceAll("\\{", "(\"");
	                		test=test.replaceAll("\\}", "\")");
	                		
	                		test=test.replaceAll(" and ", " && ");
	                		test=test.replaceAll(" or ", " || ");
	                		testList.add(test);
	                		String key="${mapper-if-"+k+"}";
	                		textMap.put(key,ifitm.getTextContent());
	                		Element tl=document.createElement("temp");
	                		tl.setTextContent(key);
	                		item.replaceChild(tl, ifitm);
						}
	                	if(testList.size()>0) {
	                		diyDataMap.put("mapper-iftest-testList", testList);
		                	diyDataMap.put("mapper-iftest-textMap", textMap);
	                	}
	                	dm.setSql(item.getTextContent());
	                	CacheModelManager.addQueryDataModel(dm);
	                }
				}
			}
		} catch (Exception e) {
			System.err.println(filePath+"配置存在错误，请检查");
			e.printStackTrace();
		}
	}
}
