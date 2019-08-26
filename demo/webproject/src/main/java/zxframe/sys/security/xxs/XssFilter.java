package zxframe.sys.security.xxs;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(asyncSupported = true, filterName = "/XssFilter", urlPatterns = { "/*" })
public class XssFilter implements Filter {
	Logger logger = LoggerFactory.getLogger(XssFilter.class);

	@Override
	public void destroy() {
		logger.info("{} init...", getClassName());
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		arg2.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) arg0), arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("{} init...", getClassName());
	}

	private String getClassName() {
		return this.getClass().getSimpleName();
	}

}
