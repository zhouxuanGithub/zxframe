package zxframe.sys.security.seesion;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zxframe.util.MD5Utils;
import zxframe.util.RC4;

public class WebSession {
	public static void createSession(String username,HttpServletResponse response) throws Exception {
    	long time=System.currentTimeMillis();
    	String key = username+"~Qa"+time+"@xWSxNHY^&UJM<KI*";
    	String enc = enc(key,username);
    	Cookie un = new Cookie("username",username);
    	un.setMaxAge(Integer.MAX_VALUE);
    	un.setPath("/");
    	Cookie cd = new Cookie("code",time+"-"+enc);
    	cd.setMaxAge(Integer.MAX_VALUE);
    	cd.setPath("/");
    	response.addCookie(un);
    	response.addCookie(cd);
    }
	public static String getUserName(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("username")) {
					return cookie.getValue();
				}
	        }
		}
		return null;
	}
	public static String getCode(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("code")) {
					return cookie.getValue();
				}
	        }
		}
		return null;
	}
	public static boolean validitySession(HttpServletRequest request) throws Exception {
		String username = getUserName(request);
		String code = getCode(request);
		if(username==null || code==null) {
			return false;
		}
		String[] cs = code.split("-");
		String key = username+"~Qa"+cs[0]+"@xWSxNHY^&UJM<KI*";
		String enc = enc(key,username);
		if(enc.equals(cs[1])) {
			return true;
		}else {
			return false;
		}
	}
	private static String enc(String key,String value) throws Exception {
		return MD5Utils.getMD5String(RC4.RunRC4(value, key)).toUpperCase();
	}
}
