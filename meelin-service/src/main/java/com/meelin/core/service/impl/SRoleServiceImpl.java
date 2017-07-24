package com.meelin.core.service.impl;
import com.meelin.core.base.service.BaseServiceImpl;
import com.meelin.core.model.SRole;
import com.meelin.core.model.SRoleResource;
import com.meelin.core.service.ISRoleResourceService;
import com.meelin.core.service.ISRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("sRoleService")
public class SRoleServiceImpl extends BaseServiceImpl<SRole> implements ISRoleService {
	//==================定制内容开始======================
//在这里修改代码
	/***
	 * 删除角色,关联删除,包括角色资源管理, 用户角色关联删除
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer remove(SRole model) throws Exception {
		SRoleResource roleRes= new SRoleResource();
		roleRes.setRoleId(model.getRoleId());
		sRoleResourceService.remove(roleRes);
		
		return this.getMyBatisDaoManager().remove(model);
	}
	
	/**
	 * 列表查询
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listMap(SRole model) throws Exception {
		return this.getMyBatisDaoManager().selectMapList(model);
	}
	@Resource
	ISRoleResourceService sRoleResourceService;
	//==================定制内容结束======================
}
