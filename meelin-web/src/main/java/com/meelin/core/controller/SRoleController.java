package com.meelin.core.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meelin.core.base.controller.BaseController;
import com.meelin.core.base.intercept.AvoidDuplicateSubmission;
import com.meelin.core.model.SRole;
import com.meelin.core.model.SRoleResource;
import com.meelin.core.service.ISResourceService;
import com.meelin.core.service.ISRoleResourceService;
import com.meelin.core.service.ISRoleService;
import com.meelin.core.util.JsonUtil;
import com.meelin.core.util.ResponseUtils;
import com.meelin.core.util.TreeNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Scope("prototype")
@RequestMapping("/admin/role")
public class SRoleController extends BaseController<SRole> {
	//==================定制内容开始======================
//在这里修改代码
	@RequestMapping("/roleList")
    public String resList() {
        return "admin/common/role_list";
    }
	@RequestMapping("/list")
    public void listMap(SRole model, HttpServletResponse response,HttpServletRequest request) throws Exception {
		sRoleService.listMap(model);
		ResponseUtils.renderJson(response, JsonUtil.toJson(model));
    }
	@RequestMapping("/add")
	@AvoidDuplicateSubmission(needSaveToken= true)
    public String add(SRole model) throws Exception {
		return "admin/common/role_edit";
    }
	@RequestMapping("/edit")
	@AvoidDuplicateSubmission(needSaveToken= true)
    public String edit(SRole model, Model viewModel) throws Exception {
		model= sRoleService.getRecord(model);
		viewModel.addAttribute("role", model);
		return "admin/common/role_edit";
    }
	@RequestMapping("/save")
	@AvoidDuplicateSubmission(needRemoveToken= true)
    public String saveOrUpdate(SRole model, RedirectAttributes rect) throws Exception {
		Integer result= 0;
		if(null!= model.getRoleId()){
			result= sRoleService.updateRecord(model);
		}else{
			result= sRoleService.saveRecord(model);
		}
		ResponseUtils.addRectMsg(rect, result);
		return "redirect:/admin/role/roleList";
    }
	@RequestMapping("/authority")
    public String authority(SRole model, Model viewModel) throws IOException {
		viewModel.addAttribute("roleId", model.getRoleId());
		return "admin/common/authority";
    }
	@RequestMapping("/getAuthority")
    public void getAuthority(SRole model, HttpServletResponse response,HttpServletRequest request) throws Exception {
		TreeNode treNode= sResourceService.listQueryTreeCheck(model.getRoleId());
		ResponseUtils.renderJson(response, JsonUtil.toJson(treNode));
    }
	@RequestMapping("/updateRoleResc")
    public void updateRoleResc(SRole model, HttpServletResponse response,HttpServletRequest request) throws Exception {
		List<SRoleResource> listModel= new ArrayList<SRoleResource>();
		SRoleResource roleRes= null;
		for(Integer id: model.getIntList()){
			roleRes= new SRoleResource();
			roleRes.setRsId(id);
			roleRes.setRoleId(model.getRoleId());
			listModel.add(roleRes);
		}
		roleRes= new SRoleResource();
		roleRes.setRoleId(model.getRoleId());
		sRoleResourceService.saveBatch(listModel, roleRes);
		ResponseUtils.addAjaxMsg(response, 1);
    }
	
	
	@Resource
	ISResourceService sResourceService;
	@Resource
	ISRoleResourceService sRoleResourceService;
	@Resource
	ISRoleService sRoleService;
	//==================定制内容结束======================


	
	
}
