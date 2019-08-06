/**
 * ZxFrame Java Library
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
package zxframe.cache.transaction;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import zxframe.aop.ServiceAspect;
import zxframe.cache.mgr.CacheManager;
import zxframe.cache.mgr.CacheModelManager;
import zxframe.jpa.model.DataModel;

/**
 * 缓存事务管理
 * @author zx
 *
 */
@Component
public class CacheTransaction {
	private Logger logger = LoggerFactory.getLogger(CacheTransaction.class);  
	//事务：模型组：ID键：值
	public ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Object>>> transactionMap=new ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, Object>>>();
	@Resource
	CacheManager cmm;
	/**
	 * 预提交缓存
	 * @param transactionId
	 * @param group
	 * @param id
	 * @param value
	 */
	public void put(DataModel cacheModel,String id,Object value) {
		try {
			String transactionId = Thread.currentThread().getName();
			if(transactionId.startsWith(ServiceAspect.THREADNAMESTARTS)) {
				if(value!=null&&CacheModelManager.checkDataModel(cacheModel)) {
					getGroupMap(transactionId, cacheModel.getGroup()).put(id, value);
				}
			}else {
				logger.error("cache put失败，未进入事务中。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 执行删除不在事务颗粒度中
	 * @param group
	 * @param id
	 */
	public void remove(String group,String id) {
		try {
			//直接删除缓存数据，不做回滚
			cmm.remove(group, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除预存数据
	 * @param group
	 * @param id
	 */
	public void removePSData(String group,String id) {
		try {
			String transactionId = Thread.currentThread().getName();
			if(transactionId.startsWith(ServiceAspect.THREADNAMESTARTS)) {
				if(id!=null) {
					getGroupMap(transactionId, group).remove(id);
				}else {
					getGroupMap(transactionId, group).clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获得事务中的值
	 * @param transactionId
	 * @param group
	 * @param key
	 * @return
	 */
	public Object get(String group,String id) {
		try {
			String transactionId = Thread.currentThread().getName();
			Object object = getGroupMap(transactionId, group).get(id);
			if(object == null) {
				//调用缓存manager尝试获取
				return cmm.get(group, id);
			}else {
				return object;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 事务开始
	 */
	public void begin() {
		String transactionId = Thread.currentThread().getName();
		transactionMap.put(transactionId, new ConcurrentHashMap<String, ConcurrentHashMap<String, Object>>());
	}
	/**
	 * 提交事务
	 */
	public void commit() {
		try {
			String transactionId = Thread.currentThread().getName();
			if(transactionId.startsWith(ServiceAspect.THREADNAMESTARTS)) {
				//调用缓存manager存值
				ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> ctransactionMap = transactionMap.get(transactionId);
				Enumeration<String> gourps = ctransactionMap.keys();
				while(gourps.hasMoreElements()) {
					String group = gourps.nextElement();
					ConcurrentHashMap<String, Object> kvMap = ctransactionMap.get(group);
					Enumeration<String> keys = kvMap.keys();
					while(keys.hasMoreElements()) {
						String id = keys.nextElement();
						Object value = kvMap.get(id);
						cmm.put(group, id, value);
					}
				}
			}else {
				logger.error("cache commit失败，未进入事务中。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 回滚事务
	 */
	public void rollback() {
	}
	/**
	 * 清理事务
	 */
	public void clear() {
		try {
			String transactionId = Thread.currentThread().getName();
			transactionMap.remove(transactionId);
//			if(ZxFrameConfig.showlog) {
//				logger.info("cache transaction clear:"+transactionId+" cacheTransactionSize:"+transactionMap.size());
//			}
			cmm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取组map
	 * @param transactionId
	 * @param group
	 * @return
	 * @throws Exception
	 */
	private ConcurrentHashMap<String, Object> getGroupMap(String transactionId,String group){
		ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> ctransactionMap = transactionMap.get(transactionId);
		if(ctransactionMap==null) {
			logger.error("缓存事务未创建，请在service层使用缓存相关功能");
			return null;
		}
		ConcurrentHashMap<String, Object> groupMap = ctransactionMap.get(group);
		if(groupMap==null) {
			groupMap=new ConcurrentHashMap<String, Object>();
			ctransactionMap.put(group, groupMap);
		}
		return groupMap;
	}
}
