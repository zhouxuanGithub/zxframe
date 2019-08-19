package zxframe.sys.webhandle.result;

public class SimpleResult<T> extends BaseResult {
	private static final long serialVersionUID = -5387467521198159143L;
	private T data;
	
	public SimpleResult() {}
	
	public SimpleResult(int resultCode,String resultMsg, T data) {
		super(resultCode,resultMsg);
		this.data = data;
	}
	
	public SimpleResult(T data){
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
