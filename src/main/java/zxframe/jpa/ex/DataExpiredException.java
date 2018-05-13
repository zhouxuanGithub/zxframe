package zxframe.jpa.ex;

/**
 * 数据过期异常，乐观锁
 * @author 周璇
 *
 */
public class DataExpiredException extends RuntimeException{
	public DataExpiredException(String message)
	{
       super(message);
 	}
	public DataExpiredException(Exception e)
	{
       super(e);
 	}
}
