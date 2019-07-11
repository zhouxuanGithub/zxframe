package zxframe.sys.interceptor;


import java.io.IOException;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import zxframe.util.WebResultUtil;

@ControllerAdvice
public class WebResponseBody implements ResponseBodyAdvice<Object>{
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }


	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType arg2,
			Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest request, ServerHttpResponse response) {
		try {
			WebResultUtil.print(null,null, body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
