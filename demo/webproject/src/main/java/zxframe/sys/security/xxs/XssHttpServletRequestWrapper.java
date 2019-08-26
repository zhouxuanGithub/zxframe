package zxframe.sys.security.xxs;



import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null)
			return null;

		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = cleanXSS(values[i]);
		}

		return encodedValues;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramMap = super.getParameterMap();
		Set<String> keySet = paramMap.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String[] str = paramMap.get(key);
			for (int i = 0; i < str.length; i++) {
				str[i] = cleanXSS(str[i]);
			}
		}
		return paramMap;
	}

	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null)
			return null;

		return cleanXSS(value);
	}

	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null)
			return null;

		return cleanXSS(value);
	}

	public String getQueryString() {
		String value = super.getQueryString();
		if (value == null)
			return null;

		return cleanXSS(value);
	}

	private String cleanXSS(String s) {
		if (s == null || s.isEmpty()) {  
            return s;  
        }
		/*else{  
            s = stripXSSAndSql(s);  
        }  */
        StringBuilder sb = new StringBuilder(s.length() + 16);  
        for (int i = 0; i < s.length(); i++) {  
            char c = s.charAt(i);  
            switch (c) {  
            case '>':  
                sb.append("＞");// 转义大于号  
                break;  
            case '<':  
                sb.append("＜");// 转义小于号  
                break;  
            case 10:
            case 13:
                break;
            case '\'':  
                sb.append("＇");// 转义单引号  
                break;  
            case '\"':  
                sb.append("＂");// 转义双引号  
                break;  
            case '&':  
                sb.append("＆");// 转义&  
                break;  
            case '#':  
                sb.append("＃");// 转义#  
                break; 
            default:  
                sb.append(c);  
                break;  
            }  
        }  
        return sb.toString();  
	}
}
