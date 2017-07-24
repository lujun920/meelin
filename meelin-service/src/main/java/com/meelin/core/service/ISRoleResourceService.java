package com.meelin.core.service;

import com.meelin.core.base.service.BaseService;
import com.meelin.core.model.SRoleResource;

import java.util.List;

public interface ISRoleResourceService extends BaseService<SRoleResource> {
	//==================定制内容开始======================
	/**
	 * <p/>方法功能描述: 删除角色关联 资源关联
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 16:00
	 */
	public Integer remove(SRoleResource model) throws Exception;
	/**
	 * <p/>方法功能描述: 批量保存角色,资源关联
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 16:39
	 */
	public void saveBatch(List<SRoleResource> listModel, SRoleResource model) throws Exception;
	//==================定制内容结束======================
}
