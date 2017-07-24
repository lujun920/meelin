package com.meelin.core.service;

import com.meelin.core.base.service.BaseService;
import com.meelin.core.model.SResource;
import com.meelin.core.util.TreeNode;
import java.util.List;
import java.util.Map;

public interface ISResourceService extends BaseService<SResource> {
	//==================定制内容开始======================
	/**
	 * <p/>方法功能描述: 根据父节点ID获取资源对象
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 15:55
	 */
	public SResource getResourceByParentId(SResource model) throws Exception;
	/**
	 * <p/>方法功能描述: 加载所有的资源菜单
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 15:55
	 */
	public List<SResource> findAll(SResource model);
	
	/**
	 * <p/>方法功能描述: 列表查询
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 15:55
	 */
	public List<Map<String, Object>> listMap(SResource model) throws Exception;
	
	/**
	 * <p/>方法功能描述: 菜单权限配置Tree
	 * @return
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 15:55
	 */
    public TreeNode listQueryTree(SResource model) throws Exception;
    
   /**
    * <p/>方法功能描述: 角色菜单权限配置Tree
    * 
    * @作者: luj
    * @时间: 2016/12/20 15:55
    */
    public TreeNode listQueryTreeCheck(Integer roleId) throws Exception;
    
    /**
     * <p/>方法功能描述: 用户角色菜单权限配置Tree
     * 
     * @作者: luj
     * @时间: 2016/12/20 15:55
     */
    public TreeNode listQueryTreeResource(Integer userId) throws Exception;
    /**
     * <p/>方法功能描述: 查询是否存在子节点
     * 
     * @作者: luj
     * @时间: 2016/12/20 15:56
     */
    public boolean chekIsExistsChildren(SResource model) throws Exception;
    /**
     * <p/>方法功能描述: 检查是否存在相同KEY
     * 
     * @作者: luj
     * @时间: 2016/12/20 15:56
     */
    public boolean checkIsExistsKey(SResource model) throws Exception;
    /**
     * <p/>方法功能描述: 删除资源,关联角色资源也删除
     * 
     * @作者: luj
     * @时间: 2016/12/20 15:56
     */
    public Integer remove(SResource model) throws Exception;
    /**
     * <p/>方法功能描述: 获取用户资源权限KEY
     * 
     * @作者: luj
     * @时间: 2016/12/20 16:26
     */
    public List<Map<String, Object>> listQueryByUserId(Integer roleId) throws Exception;
	//==================定制内容结束======================
}
