package com.meelin.core.exception;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class MyExceptionHandler implements HandlerExceptionResolver {
	private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ex", ex);
		ex.printStackTrace();
		// 根据不同错误转向不同页面
		if (ex instanceof ControllerException) {
			return new ModelAndView("ControllerException", model);
		} else if (ex instanceof ServiceException) {
			return new ModelAndView("ServiceException", model);
		} else if (ex instanceof DaoException) {
			return new ModelAndView("DaoException", model);
		}else if (ex instanceof UnauthorizedException) {
			logger.info("UnauthorizedException 403");
			return new ModelAndView("redirect:/403");
		}else if (ex instanceof UnauthenticatedException) {
			logger.info("UnauthenticatedException 403");
			return new ModelAndView("redirect:/403");
		}else {
			return new ModelAndView("error", model);
		}
	}
}
