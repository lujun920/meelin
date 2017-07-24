package com.meelin.core.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meelin.core.base.cnd.ISqlExp;
import com.meelin.core.base.controller.BaseController;
import com.meelin.core.base.intercept.AvoidDuplicateSubmission;
import com.meelin.core.model.SUser;
import com.meelin.core.service.ISUserService;
import com.meelin.core.util.JsonUtil;
import com.meelin.core.util.ResponseUtils;
import com.meelin.core.util.StrUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Scope("prototype")
@RequestMapping("/admin/user")
public class SUserController extends BaseController<SUser> {
	//==================定制内容开始======================
	// 在这里修改代码
	@RequestMapping("/userList")
	public String listUser() {
		return "admin/common/user_list";
	}

	/**
	 * 用户列表
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:view")
	public void listUserMap(SUser model, HttpServletResponse response, HttpServletRequest request) throws Exception {
		sUserService.listUserMap(model);
		ResponseUtils.renderJson(response, JsonUtil.toJson(model));
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @param model
	 * @param viewModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/add")
	@RequiresPermissions("sys:user:add")
	@AvoidDuplicateSubmission(needSaveToken = true)
	public String add(SUser model, Model viewModel) throws Exception {
		return "admin/common/user_edit";
	}
	
	/**
	 * 跳转到修改页面
	 * 
	 * @param model
	 * @param viewModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edit")
	@RequiresPermissions("sys:user:edit")
	@AvoidDuplicateSubmission(needSaveToken = true)
	public String edit(SUser model, Model viewModel) throws Exception {
		viewModel.addAttribute("user", sUserService.getRecord(model));
//		userInfo.setInfoUserId(model.getUserId());
//		viewModel.addAttribute("userInfo", userInfoService.getuserId(userInfo));
		return "admin/common/user_edit";
	}

	/**
	 * 添加或者修改用户
	 * 
	 * @param model
	 * @param rect
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	@AvoidDuplicateSubmission(needRemoveToken = true)
	public String saveOrUpdate(SUser model, RedirectAttributes rect)
			throws Exception {
		Integer result = 0;
		if (null == model.getUserId()) {
//			result = userService.saveUser(model,userInfo);
		} else {
//			result = userService.updateUser(model,userInfo);
		}
		ResponseUtils.addRectMsg(rect, result);
		return "redirect:/admin/user/userList";
	}

	/**
	 * 批量启用用户
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateStatusToEnabled")
	public void updateStatusToEnabled(SUser model, HttpServletResponse response)
			throws Exception {
		ResponseUtils.addAjaxMsg(response,
				sUserService.updateStatusToEnabled(model));
	}

	/**
	 * 批量禁用用户
	 * 
	 * @param model
	 * @param rect
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateStatusToDisabled")
	public void updateStatusToDisabled(SUser model, HttpServletResponse response)
			throws Exception {
		ResponseUtils.addAjaxMsg(response, sUserService.updateStatusToDisabled(model));
	}
	
	@RequestMapping("/listUser")
	public void listUser(SUser model, HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		ResponseUtils.renderJson(response, JsonUtil.toJson(sUserService.listUser(model)));
	} 
	
	/**
	 * 检查用户账户是否存在
	 * 
	 * @param model
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "/checkByUserName", method = RequestMethod.POST)
	public void checkUserAccount(SUser model, HttpServletResponse response) throws Exception {
		if (StrUtils.isNullOrEmpty(model.getUsername())) {
			ResponseUtils.addAjaxMsg(response, "用户非法操作", 0);
			return;
		}
		model = sUserService.selectOneUserByName(model);
		// 已存在重复
		if (null != model && model.getUserId() > 0) {
			if ("1".equals(model.getEnabled())) {
				ResponseUtils.addAjaxMsg(response, "用户名存在", 1);
			} else {
				ResponseUtils.addAjaxMsg(response, "用户账户已被禁用", 0);
			}
		} else {
			ResponseUtils.addAjaxMsg(response, "用户名不存在", 0);
		}
	}

	@Resource
	private ISUserService sUserService;
	//==================定制内容结束======================
}
