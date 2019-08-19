package zxframe.sys.webhandle.result;

import java.io.Serializable;

public class BaseResult implements Serializable {
	
		private static final long serialVersionUID = 7247723548850330908L;
		private int resultCode;
		private String resultMsg;
		
		public BaseResult() {
			
		}

		public BaseResult(int resultCode) {
			this.resultCode = resultCode;
		}
		
		public BaseResult(int resultCode,String resultMsg) {
			this.resultCode = resultCode;
			this.resultMsg = resultMsg;
		}
		
		public int getResultCode() {
			return resultCode;
		}

		public void setResultCode(int resultCode) {
			this.resultCode = resultCode;
		}

		public String getResultMsg() {
			return resultMsg==null?"":resultMsg;
		}

		public void setResultMsg(String resultMsg) {
			this.resultMsg = resultMsg;
		}
}
