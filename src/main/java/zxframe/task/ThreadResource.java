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
package zxframe.task;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @preserve
 * @author zhoux
 */
public final class ThreadResource {
	/**
	 * 线程池
	 */
	private static ExecutorService threadPool = Executors.newCachedThreadPool(); 
	/**
	 * 任务池
	 */
	private static TaskPool taskPool = new TaskPool();
	
	static{
		threadPool.execute(taskPool);
	}
	
	/**
	 * 获得系统线程池
	 * @preserve
	 */
	public static ExecutorService getThreadPool() {
		return threadPool;
	}
	/**
	 * 获得任务池
	 * @preserve
	 */
	public static TaskPool getTaskPool(){
		return taskPool;
	}
}
