/**
 * ZxFrame Java Library
 * https://github.com/zhouxuanGithub/zxframe
 *
 * Copyright (c) 2020 zhouxuan
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
package zxframe.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 分布式ID生成
 * @author zx
 */
public class ZxObjectId {
	private long time=System.currentTimeMillis()/1000-1591782121l;
	private int workerId=0;
	private int clusterSequenceId=0;
	private long lastTimestamp = System.currentTimeMillis()/1000;
	/**
	 * 默认使用当前机器的IP后3位作为workerId
	 * @throws UnknownHostException 
	 */
	public ZxObjectId() throws UnknownHostException {
		String ip=InetAddress.getLocalHost().getHostAddress().replace(".","");
		workerId=Integer.parseInt(ip.substring(ip.length()-3));
	}
	/**
	 * 设置workerId(0~999)，不设置则用当前机器的IP后3位
	 * @param workerId
	 */
	public ZxObjectId(int workerId) {
		if(workerId>=0&&workerId<=999) {
			this.workerId=workerId;
		}
	}
	/**
	 * 获得分布式ID
	 * 参考snowflake，改进时间回拨问题，改进使用年限短的问题
	 * 可选设置workerId，不设置则用当前机器的IP后3位
	 * 历史累加时间戳10+机器线程3+计数器6，单机1秒可产生100万ID
	 * @return 分布式ID
	 */
	public synchronized long getObjectId()
	{
		if(++clusterSequenceId>=1000000)
		{
			lastTimestamp = tilNext();
			clusterSequenceId=0;
			time++;
		}
		StringBuilder sb=new StringBuilder();
		sb.append(time);
		sb.append(workerId);
		sb.append(clusterSequenceId);
		return Long.parseLong(sb.toString());
	}
	private long tilNext() {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }
	private long timeGen() {
        return System.currentTimeMillis()/1000;
    }
	public static void main(String[] args) throws UnknownHostException {
		System.out.println("start");
		ZxObjectId objectid=new ZxObjectId();
		long t=System.currentTimeMillis();
		long b=0;
		for (int i = 0; i < 5000000; i++) {
			long a =objectid.getObjectId();
			b+=a;
		}
		System.out.println("生成500W耗时："+(System.currentTimeMillis()-t)+"毫秒");
		System.out.println(b);
	}
}
