package com.meelin.core.service;
import com.meelin.core.base.service.BaseService;
import com.meelin.core.model.SUser;

import java.util.List;
import java.util.Map;

public interface ISUserService extends BaseService<SUser> {
	//==================定制内容开始======================

	/**
	 * <p/>方法功能描述: 通过用户名查询用户信息
	 *
	 * @作者: luj
	 * @时间: 2016/12/20 16:22
	 */
	public SUser selectOneUserByName(SUser model) throws Exception;

	/**
	 * <p/>方法功能描述: 取得用户的权限
	 *
	 * @作者: luj
	 * @时间: 2016/12/20 16:22
	 */
	public List<Map<String,Object>> findResourceByUserName(SUser model) throws Exception;

	public List<Map<String, Object>> listUserMap(SUser model) throws Exception;

	public Integer saveUser(SUser model) throws Exception;

	public Integer updateUser(SUser model) throws Exception;

	/**
	 * <p/>方法功能描述: 批量启用用户
	 *
	 * @作者: luj
	 * @时间: 2016/12/20 16:23
	 */
	public Integer updateStatusToEnabled(SUser model) throws Exception;

	/**
	 * <p/>方法功能描述: 批量禁用用户
	 *
	 * @作者: luj
	 * @时间: 2016/12/20 16:23
	 */
	public Integer updateStatusToDisabled(SUser model) throws Exception;
	
	/**
	 * <p/>方法功能描述: 用户列表(不分页)
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 16:23
	 */
	public List<Map<String, Object>> listUser(SUser model) throws Exception;

	//==================定制内容结束======================
}
