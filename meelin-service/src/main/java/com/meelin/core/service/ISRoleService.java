package com.meelin.core.service;


import com.meelin.core.base.service.BaseService;
import com.meelin.core.model.SRole;

import java.util.List;
import java.util.Map;

public interface ISRoleService extends BaseService<SRole> {
	//==================定制内容开始======================
	/**
	 * <p/>方法功能描述: 删除角色,关联删除,包括角色资源管理, 用户角色关联删除
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 16:20
	 */
	public Integer remove(SRole model) throws Exception;

	public List<Map<String, Object>> listMap(SRole model) throws Exception;
	//==================定制内容结束======================
}
