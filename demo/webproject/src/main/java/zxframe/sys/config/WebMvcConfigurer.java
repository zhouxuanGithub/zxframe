package zxframe.sys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import zxframe.sys.interceptor.WebInterceptor;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter{
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
