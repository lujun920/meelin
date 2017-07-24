package com.meelin.core.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.meelin.core.base.controller.BaseController;
import com.meelin.core.base.intercept.AvoidDuplicateSubmission;
import com.meelin.core.model.SResource;
import com.meelin.core.service.ISLoggerService;
import com.meelin.core.service.ISResourceService;
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
@RequestMapping("/admin/resource")
public class SResourceController extends BaseController<SResource> {
	//==================定制内容开始======================
//在这里修改代码
	@RequestMapping("/resList")
    public String resList() {
        return "admin/common/resource_list";
    }
	@RequestMapping("/resJson")
    public void json(HttpServletResponse response, HttpServletRequest request, SResource model) throws Exception {
		TreeNode treNode= sResourceService.listQueryTree(model);
		ResponseUtils.renderJson(response, JsonUtil.toJson(treNode));
    }
	
	@RequestMapping("/getResById")
    public void listMap(SResource model, HttpServletResponse response,HttpServletRequest request) throws Exception {
		sResourceService.listMap(model);
		ResponseUtils.renderJson(response, JsonUtil.toJson(model));
    }
	
	@RequestMapping("/add")
	@AvoidDuplicateSubmission(needSaveToken= true)
    public String add(SResource model, Model viewModel) throws Exception {
		model= sResourceService.getRecord(model);
		SResource parentModel= new SResource();
		parentModel.setName(model.getName());
		parentModel.setRsId(model.getRsId());
//		viewModel.addAttribute("res", null);
		viewModel.addAttribute("parentRes", parentModel);
		return "admin/common/resource_edit";
    }
	@RequestMapping("/edit")
	@AvoidDuplicateSubmission(needSaveToken= true)
    public String edit(SResource model, Model viewModel) throws Exception {
		//先获取选中节点
		model= sResourceService.getRecord(model);
		//再获取父节点
		SResource parentModel= sResourceService.getResourceByParentId(model);
		viewModel.addAttribute("parentRes", parentModel);
		viewModel.addAttribute("res", model);
		return "admin/common/resource_edit";
    }
	@RequestMapping("/save")
	@AvoidDuplicateSubmission(needRemoveToken= true)
    public String saveOrUpdate(SResource model, RedirectAttributes rect) throws Exception {
		Integer result= 0;
		if(null!= model.getRsId()){
			result= sResourceService.updateRecord(model);
//			loggerService.saveLogger("更改资源数据节点名称:"+model.getName(), SystemTypeEnum.Logger.UPDATE.getValue());
		}else{
			result= sResourceService.saveRecord(model);
//			loggerService.saveLogger("新增资源数据节点名称:"+model.getName(), SystemTypeEnum.Logger.ADD.getValue());
		}
		ResponseUtils.addRectMsg(rect, result);
		return "redirect:/admin/resource/resList";
    }
	@RequestMapping("/remove")
    public void remove(SResource model, HttpServletResponse response) throws Exception {
		model= sResourceService.getRecord(model);
		if(sResourceService.chekIsExistsChildren(model)){
			ResponseUtils.addAjaxMsg(response, "存在子节点,不能直接删除!", -1);
		}else{
			int result= sResourceService.remove(model);
//			loggerService.saveLogger("删除资源节点:"+model.getRsId()+"节点名称:"+model.getName(), SystemTypeEnum.Logger.DELETE.getValue());
			ResponseUtils.addAjaxMsg(response, result);
		}
    }
	@Resource
	ISLoggerService sLoggerService;
	@Resource
	ISResourceService sResourceService;
	//==================定制内容结束======================
}
