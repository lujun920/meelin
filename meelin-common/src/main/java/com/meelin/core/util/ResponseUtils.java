package com.meelin.core.util;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>类文件: com.meelin.core.util.ResponseUtils.java
 * <p/>类功能描述: HttpServletResponse帮助类
 *
 * @作者: luj
 * @时间: 2016/12/20 15:00
 */
public final class ResponseUtils {
	/**
	 *
	 * @方法功能说明：发送文本,使用UTF-8编码
	 * @参数：@param response	HttpServletResponse
	 * @参数：@param text 		发送的字符串
	 * @修改者：
	 * @修改日期：
	 * @修改说明：
	 * @作者：lujun
	 * @返回值：void
	 * @throws
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, String text) {
		render(response, "application/json;charset=UTF-8", text);
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType,
			String text) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 普通的request.setAttribute方式返回提示信息
	 * @param request
	 * @param text
	 */
	public static void addMsg(HttpServletRequest request, String text) {
		addMsg(request, text, true);
	}
	/**
	 * 普通的request.setAttribute方式返回提示信息
	 * @param request
	 * @param text
	 * @param boole
	 */
	public static void addMsg(HttpServletRequest request, String text, Boolean boole) {
		request.setAttribute("state", boole);
		request.setAttribute("msg", text);
	}
	/**
	 * 根据返回值判断操作是否成功返回提示内容
	 * 重定向参数传递,RedirectAttributes这个原理是把传递数据存入session,
	 * 在返回到页面后重新请求后清除该session的值所以刷新后这个值会消失
	 * @param rect
	 * @param result
	 */
	public static void addRectMsg(RedirectAttributes rect, Integer result) {
		if(result>0){
			addRectMsg(rect, "操作成功!", true);
		}else{
			addRectMsg(rect, "操作失败", false);
		}
		
	}
	/**
	 * 重定向参数传递,RedirectAttributes这个原理是把传递数据存入session,
	 * 在返回到页面后重新请求后清除该session的值所以刷新后这个值会消失
	 * @param rect
	 * @param text
	 * @param boole
	 */
	public static void addRectMsg(RedirectAttributes rect, String text, Boolean boole) {
		rect.addFlashAttribute("state", boole);
		rect.addFlashAttribute("msg", text);
	}
	/**
	 * Ajax方式响应参数,result大于0为成功,小于1为失败
	 * @param response
	 * @param result
	 */
	public static void addAjaxMsg(HttpServletResponse response, Integer result){
		if(result>0){
			addAjaxMsg(response, "操作成功!", result);
		}else{
			addAjaxMsg(response, "操作失败!", result);
		}
	}
	/**
	 * Ajax方式响应参数,自定义返回提示信息,result大于0为成功,小于1为失败
	 * @param response
	 * @param text
	 * @param result
	 */
	public static void addAjaxMsg(HttpServletResponse response, String text, Integer result){
		Map<String, Object> map= new HashMap<String, Object>();
		if(result>0){
			map.put("state", true);
		}else{
			map.put("state", false);
		}
		map.put("msg", text);
		renderJson(response,JsonUtil.toJson(map));
	}
}
