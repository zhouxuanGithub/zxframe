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


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;



/**
 * 任务池
 * @author zhoux
 */
public class TaskPool implements Runnable{
	private List<TaskModel> tasks=new CopyOnWriteArrayList<TaskModel>();
	public void run() {
		TaskModel taskModel;
		int i;
		while(true)
		{
			try {
				for (i = 0; i <tasks.size() ; i++) {
					taskModel=tasks.get(i);
					if(taskModel.getRunTime()<=System.currentTimeMillis()){
						if(taskModel.getType()==1) {
							tasks.remove(taskModel);
						}else {
							taskModel.calcTime(taskModel.getDelay());
						}
						ThreadResource.getThreadPool().execute(taskModel);
					}
				}
				if(tasks.size()==0) {
					Thread.sleep(1000);
				}else {
					Thread.sleep(100);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 循环执行
	 * @param task 任务
	 * @param initialDelay 第一次执行的时间
	 * @param delay 间隔多长时间执行一次
	 * @param timeUnit
	 * @return
	 * @preserve
	 */
	public synchronized TaskModel scheduleWithFixedDelay(TaskRunnable task,long initialDelay,long delay,TimeUnit timeUnit)
	{
		TaskModel taskModel=new TaskModel();
		taskModel.setTaskRunnable(task);
		taskModel.setDelay(delay);
		taskModel.setTimeUnit(timeUnit);
		taskModel.setType(2);
		taskModel.calcTime(initialDelay);
		tasks.add(taskModel);
		return taskModel;
	}
	/**
	 * 执行任务
	 * @param task 任务
	 * @param seconds 间隔时间
	 * @param timeUnit
	 * @return
	 * @preserve
	 */
	public synchronized TaskModel schedule(TaskRunnable task, long time,TimeUnit timeUnit) {
		TaskModel taskModel=new TaskModel();
		taskModel.setTaskRunnable(task);
		taskModel.setDelay(time);
		taskModel.setTimeUnit(timeUnit);
		taskModel.setType(1);
		taskModel.calcTime(time);
		tasks.add(taskModel);
		return taskModel;
	}
	public List<TaskModel> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskModel> tasks) {
		this.tasks = tasks;
	}
}
