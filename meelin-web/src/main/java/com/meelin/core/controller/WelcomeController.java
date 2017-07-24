package com.meelin.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WelcomeController {
	private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
	@RequestMapping(value = "/admin/login", method = RequestMethod.GET)
	public String login() {
		return "admin/login";
	}
	
	@RequestMapping(value = "/admin/index", method = RequestMethod.GET)
	public String index() {
		logger.info("login success redirect admin-decorator");
		return "admin-decorator";
	}
	
}
