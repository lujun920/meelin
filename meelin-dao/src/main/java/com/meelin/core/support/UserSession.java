package com.meelin.core.support;


import com.meelin.core.model.SUser;
import com.meelin.core.util.TreeNode;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * <p/>类文件: com.meelin.core.support.UserSession.java
 * <p/>类功能描述: 保存登录用户信息，如ip、操作系统、浏览器等信息
 *
 * @作者: luj
 * @时间: 2017/1/19 11:13
 */
public class UserSession implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String SESSIONNAME = "USER_SESSION_TOKEN";
	
	private String os;
	private String ip;
	private String browser;
	
	private SUser user= null;
	
	private TreeNode menu= null;
	
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public SUser getUser() {
		return user;
	}
	public void setUser(SUser user) {
		this.user = user;
	}
	public TreeNode getMenu() {
		return menu;
	}
	public void setMenu(TreeNode menu) {
		this.menu = menu;
	}
	/**
	 * 从web层Session中获取登录用户持有的外包类。
	 * 
	 * <p>
	 * 如不存在，则创建一个新的外包类；但新创建的外包类中的具体用户信息为<code>null</code>.
	 */
	public static UserSession getInstance(HttpServletRequest request) {
		UserSession instance = (UserSession) WebUtils.getSessionAttribute(
				request, SESSIONNAME);
		if (instance == null) {
			instance = new UserSession();
			WebUtils.setSessionAttribute(request, SESSIONNAME, instance);
		}
		return instance;
	}
	
	/**
	 * 从web层Session中获取登录用户持有的外包类。
	 * @return
	 */
	public static UserSession getInstance() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return (UserSession) WebUtils.getSessionAttribute(request, SESSIONNAME);
	}
	
	/**
	 * 销毁当前登录用户所持有的web层Session.
	 */
	public static void remove(HttpServletRequest request) {
		WebUtils.setSessionAttribute(request, SESSIONNAME, null);
	}
}
