package com.meelin.core.service.impl;
import com.meelin.core.base.cnd.Cnd;
import com.meelin.core.base.service.BaseServiceImpl;
import com.meelin.core.model.SRoleResource;
import com.meelin.core.service.ISRoleResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("sRoleResourceService")
public class SRoleResourceServiceImpl extends BaseServiceImpl<SRoleResource> implements ISRoleResourceService {
	//==================定制内容开始======================
//在这里修改代码
	/**
	 * <p/>方法功能描述: 删除角色关联 资源关联
	 *
	 * @作者: luj
	 * @时间: 2016/12/20 16:51
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer remove(SRoleResource model) throws Exception{
		Cnd cnd= new Cnd();
		if(null != model.getRoleId()){
			cnd.andEquals("ROLE_ID", "roleId");
		}
		if(null!= model.getRsId()){
			cnd.andEquals("RS_ID", "rsId");
		}
		model.setCondition(cnd);
		return this.getMyBatisDaoManager().exeDeleteCommand(model, "removeSomeRoleResource");
	}
	/**
	 * <p/>方法功能描述: 批量保存角色资源关联
	 *
	 * @作者: luj
	 * @时间: 2016/12/20 16:51
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveBatch(List<SRoleResource> listModel, SRoleResource model) throws Exception {
		//先删除角色关联资源
		Cnd cnd= new Cnd();
		cnd.andEquals("ROLE_ID", "roleId");
		model.setCondition(cnd);
		this.getMyBatisDaoManager().exeDeleteCommand(model, "removeSomeRoleResource");
		this.getMyBatisDaoManager().saveBatch(listModel, "saveBatchRoleResource");
	}
	//==================定制内容结束======================
}
