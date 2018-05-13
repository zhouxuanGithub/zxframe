package zxframe.jpa.transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import zxframe.config.ZxFrameConfig;
import zxframe.jpa.datasource.DataSourceManager;
import zxframe.jpa.ex.JpaRuntimeException;

@Component
public class DataTransaction {
	private Logger logger = LoggerFactory.getLogger(DataTransaction.class);
	/**
	 * 事务开始
	 * @param joinPoint 
	 */
	public void begin(JoinPoint joinPoint) {
		if(ZxFrameConfig.showlog) {
			String transactionId = Thread.currentThread().getName();
			logger.info("db transaction start:"+transactionId);
		}
	}
	/**
	 * 提交事务
	 * @param joinPoint 
	 */
	public void commit(JoinPoint joinPoint) {
		String transactionId = Thread.currentThread().getName();
		ConcurrentMap<String, Connection> cmap = DataSourceManager.uwwcMap.get(transactionId);
		if(cmap!=null) {
			Iterator<String> iterator = cmap.keySet().iterator();
			while(iterator.hasNext()) {
				try {
					String dsname = iterator.next();
					Connection connection = cmap.get(dsname);
					if(connection!=null) {
						connection.commit();//提交JDBC事务   
						if(ZxFrameConfig.showlog) {
							logger.info("db transaction commit:"+transactionId+" dsname:"+dsname+" dataTransactionSize:"+DataSourceManager.uwwcMap.size());
						}
					}
				} catch (SQLException e) {
					//提交失败，记录日志？
					logger.error("事务提交失败",e);
				}
			}
		}
	}
	/**
	 * 回滚事务
	 * @param joinPoint 
	 */
	public void rollback(JoinPoint joinPoint) {
		String transactionId = Thread.currentThread().getName();
		ConcurrentMap<String, Connection> cmap = DataSourceManager.uwwcMap.get(transactionId);
		if(cmap!=null) {
			Iterator<String> iterator = cmap.keySet().iterator();
			while(iterator.hasNext()) {
				String dsname = iterator.next();
				Connection connection = cmap.get(dsname);
				if(connection!=null) {
					try {
						connection.rollback();
						if(ZxFrameConfig.showlog) {
							logger.info("db transaction rollback:"+transactionId+" dsname:"+dsname+" dataTransactionSize:"+DataSourceManager.uwwcMap.size());
						}
					} catch (Exception e) {
						//回滚失败，记录日志？
						logger.error("事务回滚失败",e);
					}
				}
			}
		}
	}
	/**
	 * 清理事务数据
	 * @param joinPoint 
	 * @param transactionId
	 */
	public void clear(JoinPoint joinPoint) {
		String transactionId = Thread.currentThread().getName();
		ConcurrentMap<String, Connection> cmap = DataSourceManager.uwwcMap.get(transactionId);
		if(cmap!=null) {
			Iterator<String> iterator = cmap.keySet().iterator();
			while(iterator.hasNext()) {
				String dsname = iterator.next();
				Connection connection = cmap.get(dsname);
				if(connection!=null) {
					try {
						connection.close();
						if(ZxFrameConfig.showlog) {
							logger.info("db transaction close:"+transactionId+" dsname:"+dsname);
						}
					} catch (SQLException e) {
						//close失败，记录日志？
						logger.error("事务close失败",e);
					}
				}
			}
			DataSourceManager.uwwcMap.remove(transactionId);
		}
		if(ZxFrameConfig.showlog) {
			logger.info("db transaction clear:"+transactionId+" dataTransactionSize:"+DataSourceManager.uwwcMap.size());
		}
	}
}
