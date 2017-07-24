package com.meelin.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * <p/>类文件: com.meelin.core.util.CookieUtils.java
 * <p/>类功能描述: Cookie 辅助类
 *
 * @作者: luj
 * @时间: 2017/1/16 17:08
 */
public class CookieUtils {
	
	/**
	 * <p/>方法功能描述: 获得cookie
	 * 
	 * @作者: luj
	 * @时间: 2017/1/16 17:08
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * <p/>方法功能描述: 根据部署路径，将cookie保存在根目录
	 * 
	 * @作者: luj
	 * @时间: 2017/1/16 17:09
	 */
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		if (expiry != null) {
			cookie.setMaxAge(expiry);
		}
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		response.addCookie(cookie);
		return cookie;
	}
	
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer expiry, String domain,String path) {
		Cookie cookie = new Cookie(name, value);
		if (expiry != null) {
			cookie.setMaxAge(expiry);
		}
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		cookie.setPath(path);
		response.addCookie(cookie);
		return cookie;
	}

	/**
	 * <p/>方法功能描述: 取消cookie
	 * 
	 * @作者: luj
	 * @时间: 2017/1/16 17:10
	 */
	public static void cancleCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String domain) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}
}
