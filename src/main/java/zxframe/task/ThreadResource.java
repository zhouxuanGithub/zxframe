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
