package zxframe.sys.webhandle;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import zxframe.sys.security.seesion.WebSessionInterceptor;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter{
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new WebSessionInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
