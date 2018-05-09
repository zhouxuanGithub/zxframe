package zxframe.jpa.ex;

public class JpaRuntimeException extends RuntimeException{
	public JpaRuntimeException(String message)
	{
       super(message);
 	}
	public JpaRuntimeException(Exception e)
	{
       super(e);
 	}
}
