package com.meelin.core.base.intercept;

import com.meelin.core.exception.ControllerException;
import com.meelin.core.util.ResponseUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防重复提交拦截器 在新建页面方法上，设置needSaveToken()为true，此时拦截器会在Session中保存一个token，
 * 同时需要在新建的页面中添加 <input type="hidden" name="token" value="${token}"> <br/>
 * 保存方法需要验证重复提交的，设置needRemoveToken为true 此时会在拦截器中验证是否重复提交
 * 
 * @author lujun
 * @date 2015年10月20日
 *
 */
public class AvoidDuplicateSubmissionInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			AvoidDuplicateSubmission annotation = method.getAnnotation(AvoidDuplicateSubmission.class);
			if (annotation != null) {
				boolean needSaveSession = annotation.needSaveToken();
				if (needSaveSession) {
					request.getSession(true).setAttribute("token",	Tokenprocessor.getInstance().generateToken(request));
				}
				boolean needRemoveSession = annotation.needRemoveToken();
				if (needRemoveSession) {
					if (isRepeatSubmit(request, response)) {
						return false;
					}
					request.getSession(false).removeAttribute("token");
				}
			}
		}
		return true;
	}

	private boolean isRepeatSubmit(HttpServletRequest request, HttpServletResponse response) {
		String serverToken = (String) request.getSession(true).getAttribute("token");
		if (serverToken == null) {
			ResponseUtils.renderText(response, "表单重复提交");
			throw new ControllerException("表单重复提交");
		}
		String clinetToken = request.getParameter("token");
		if (clinetToken == null) {
			ResponseUtils.renderText(response, "表单重复提交");
			throw new ControllerException("表单重复提交"); 
		}
		if (!serverToken.equals(clinetToken)) {
			ResponseUtils.renderText(response, "表单重复提交");
			throw new ControllerException("表单重复提交"); 
		}
		return false;
	}
}
