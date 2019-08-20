package zxframe.sys.webhandle;



import java.io.IOException;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import zxframe.sys.webhandle.result.BaseResult;
import zxframe.sys.webhandle.result.ResultCode;
import zxframe.sys.webhandle.result.SimpleResult;
import zxframe.util.WebResultUtil;

@ControllerAdvice
public class WebResponseBody implements ResponseBodyAdvice<Object>{
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }


	@Override
	public Object beforeBodyWrite(Object result, MethodParameter returnType, MediaType arg2,
			Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest request, ServerHttpResponse response) {
		try {
			if(result!=null) {
				if(!(result instanceof BaseResult)) {
					result= new SimpleResult(ResultCode.Success,"",result);
				}
			}
			else {
				result= new BaseResult(ResultCode.Success);
			}
			WebResultUtil.print(null,null, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
