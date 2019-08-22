package zxframe.sys.webhandle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import zxframe.sys.webhandle.result.BaseResult;
import zxframe.sys.webhandle.result.ResultCode;
import zxframe.util.WebResultUtil;

@ResponseBody
@ControllerAdvice
public class WebExceptionHandler {
	 @ExceptionHandler(value = Throwable.class)
	 public Object defaultErrorHandler(HttpServletRequest request,HttpServletResponse response, Throwable e) throws Throwable {
		 response.setStatus(500);
	     request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, 500);
		 WebResultUtil.print(request, response, new BaseResult(ResultCode.Failed,e.getMessage()));
		 throw e;
	 }
}
