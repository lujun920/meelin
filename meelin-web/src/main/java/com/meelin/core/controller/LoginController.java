package com.meelin.core.controller;

import com.meelin.core.util.Constants;
import com.meelin.core.util.RadomCodeUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	/**
	 * 指向登录页面
	 */
	@RequestMapping(value = "/admin/login", method = RequestMethod.POST)
	public String login() {
		if (!SecurityUtils.getSubject().isAuthenticated()) {
			return "/admin/login";
		}
		return "loginpage";
	}

	/**
	 * 指定无访问权限页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/403")
	public String getDeniedPage() {
//		logger.debug("Received request to show denied page");
		logger.info("403");
		return "403";
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		logger.info("UserAgent: {}", request.getHeader("user-agent"));
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return "redirect:/admin/login";
	}
	/**
	 * 获取验证码图片和文本(验证码文本会保存在HttpSession中)
	 */
	@RequestMapping("/genCaptcha")
	public void genCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//设置页面不缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String verifyCode = RadomCodeUtil.generateTextCode(RadomCodeUtil.TYPE_NUM_UPPER, 4, null);
		//将验证码放到HttpSession里面
		request.getSession().setAttribute(Constants.VALIDATE_CODE, verifyCode);
		logger.info("本次生成的验证码为[{}],已存放到HttpSession中", verifyCode);
		//设置输出的内容的类型为JPEG图像
		response.setContentType("image/jpeg");
		BufferedImage bufferedImage = RadomCodeUtil.generateImageCode(verifyCode, 90, 30, 5, true, Color.WHITE, null, null);
		ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
	}
}