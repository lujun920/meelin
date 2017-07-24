package com.meelin.core.service.impl;

import com.meelin.core.base.cnd.Cnd;
import com.meelin.core.base.cnd.ISqlExp;
import com.meelin.core.base.service.BaseServiceImpl;
import com.meelin.core.model.SResource;
import com.meelin.core.model.SRoleResource;
import com.meelin.core.service.ISResourceService;
import com.meelin.core.service.ISRoleResourceService;
import com.meelin.core.util.TreeNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("sResourceService")
public class SResourceServiceImpl extends BaseServiceImpl<SResource> implements ISResourceService {
	//==================定制内容开始======================
//在这里修改代码
	/**
	 * <p/>方法功能描述: 根据父节点ID获取资源对象
	 *
	 * @作者: luj
	 * @时间: 2016/12/20 16:44
	 */
	public SResource getResourceByParentId(SResource model) throws Exception {
		Cnd cnd= new Cnd();
		cnd.andEquals("RS_ID", "rspId");
		model.setCondition(cnd);
		return this.getMyBatisDaoManager().selectOne(model);
	}
	@Override
	public List<SResource> findAll(SResource model) {
		return this.getMyBatisDaoManager().selectModelList(model);
	}
	@Override
	public List<Map<String, Object>> listMap(SResource model) throws Exception {
		Cnd cnd= new Cnd();
		if(null!= model.getRspId()){
			cnd.andEquals("rspId");
		}
		model.setCondition(cnd);
		return this.getMyBatisDaoManager().selectMapList(model);
	}
	
	/**
	 * <p/>方法功能描述: 菜单权限配置Tree
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 16:44
	 */
	@Override
    public TreeNode listQueryTree(SResource model) throws Exception {
    	Cnd cnd= new Cnd();
    	model.setEnabled(SResource.ENABLED_1);
    	cnd.andEquals("enabled");
    	cnd.orderBy("RSP_ID", ISqlExp.ORDER.ASC);
    	cnd.orderBy("sort", ISqlExp.ORDER.ASC);
    	model.setCondition(cnd);
    	model.setIsPage(false);
    	List<Map<String, Object>> mList= this.getMyBatisDaoManager().selectMapList(model);
    	TreeNode root= null;
    	for(int i= 0; i< mList.size(); i++){
    		if(i==0){
    			root= new TreeNode(mList.get(i).get("rsId").toString(), mList.get(i).get("rspId").toString(),
    					mList.get(i).get("name").toString(), "", true);
    		}else{
    			TreeNode node = new TreeNode(mList.get(i).get("rsId").toString(), mList.get(i).get("rspId").toString(),
    					mList.get(i).get("name").toString(), "", true);
    			root.add(node);
    		}
    	}
    	return root;
    }
    
    /**
     * <p/>方法功能描述: 角色菜单权限配置Tree 权限分配树
     *
     * @作者: luj
     * @时间: 2016/12/20 16:45
     */
	@Override
    public TreeNode listQueryTreeCheck(Integer roleId) throws Exception {
		SResource model = new SResource();
		model.setEnabled(SResource.ENABLED_1);
		Cnd cnd = new Cnd();
		cnd.andEquals("enabled").orderBy("RSP_ID", ISqlExp.ORDER.ASC).orderBy("sort", ISqlExp.ORDER.ASC);
		model.setCondition(cnd);
		model.setIsPage(false);
		// 获取所有资源
		List<Map<String, Object>> mList = this.getMyBatisDaoManager().selectMapList(model);

		model.getIntMap().put("roleId", roleId);
		cnd.reset().andEquals("ROLE_ID", "intMap.roleId");
    	//获取角色所拥有的资源
    	List<Map<String, Object>> crList = this.getMyBatisDaoManager().exeSelectCommand(model, "selectResRoleMap");
    	TreeNode root= null;
    	for(int i= 0; i< mList.size(); i++){
    		//根节点
    		String rsId = mList.get(i).get("rsId").toString();
    		String rspId = mList.get(i).get("rspId").toString();
			String name = mList.get(i).get("name").toString();
			String uri = mList.get(i).get("uri").toString();
			
    		if(i==0){
    			for (int j = 0; j < crList.size(); j++) {
					if (String.valueOf(rsId).equals(crList.get(j).get("rsId").toString())) {
						root = new TreeNode(rsId, rspId, name, uri, true, true);
						crList.remove(j);
						break;
					}
				}
    			if(root== null){
    				root = new TreeNode(rsId, rspId, name, uri, true);
    			}
    		}else{
    			TreeNode node = null;
    			for (int k = 0; k < crList.size(); k++) {
					if (String.valueOf(rsId).toString().equals(crList.get(k).get("rsId").toString())) {
						node = new TreeNode(rsId, rspId, name, uri, true, true);
						break;
					}
				}
    			if(node== null){
    				node = new TreeNode(rsId, rspId, name, uri, true);
    			}
    			root.add(node);
    		}
    	}
    	return root;
    }
    
    /**
     * <p/>方法功能描述: 用户角色菜单权限配置Tree left导航菜单树
     *
     * @作者: luj
     * @时间: 2016/12/20 16:45
     */
	@Override
    public TreeNode listQueryTreeResource(Integer userId) throws Exception {
		SResource model= new SResource();
    	Cnd cnd= new Cnd();
    	TreeNode root= null;
    	model.getIntMap().put("userId", userId);
		cnd.reset().andEquals("ur.USER_ID", "intMap.userId");
    	model.setCondition(cnd);
    	model.setIsPage(false);
    	//获取角色所拥有的资源
    	List<Map<String, Object>> crList = this.getMyBatisDaoManager().exeSelectCommand(model, "selectResUserMap");
    	for(int i= 0; i< crList.size(); i++){
    		String rsId = crList.get(i).get("rsId").toString();
    		String rspId = crList.get(i).get("rspId").toString();
			String name = crList.get(i).get("name").toString();
			String url = crList.get(i).get("uri").toString();
			//根节点
    		if(i==0){
				root= new TreeNode(rsId, rspId,	name, url, true);
			}else{
				TreeNode node= null;
				node= new TreeNode(rsId, rspId,	name, url, true);
				root.add(node);
			}
    	}
    	return root;
    }
    /**
     * <p/>方法功能描述: 查询是否存在子节点
     *
     * @作者: luj
     * @时间: 2016/12/20 16:45
     */
	@Override
    public boolean chekIsExistsChildren(SResource model) throws Exception {
    	Cnd cnd= new Cnd();
		cnd.andEquals("rspId");
		model.setCondition(cnd);
		List<SResource> listModel= this.getMyBatisDaoManager().selectModelList(model);
		if(listModel.size()>0){
			return true;
		}
    	return false;
    }
    /**
     * <p/>方法功能描述: 检查是否存在相同KEY
     *
     * @作者: luj
     * @时间: 2016/12/20 16:45
     */
	@Override
    public boolean checkIsExistsKey(SResource model) throws Exception {
    	Cnd cnd= new Cnd();
		cnd.andEquals("uri");
		model.setCondition(cnd);
		List<SResource> listModel= this.getMyBatisDaoManager().selectModelList(model);
		if(listModel.isEmpty()){
			return true;
		}
    	return false;
    }
    
    /**
     * <p/>方法功能描述: 删除资源,关联角色资源也删除
     * 
     * @作者: luj
     * @时间: 2016/12/20 16:46
     */
	@Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Integer remove(SResource model) throws Exception {
    	SRoleResource roleRes= new SRoleResource();
    	roleRes.setRsId(model.getRsId());
		sRoleResourceService.remove(roleRes);
    	return this.getMyBatisDaoManager().remove(model);
    }
   /**
    * <p/>方法功能描述: 获取用户资源权限KEY
    * 
    * @作者: luj
    * @时间: 2016/12/20 16:46
    */
	@Override
    public List<Map<String, Object>> listQueryByUserId(Integer userId) throws Exception {
    	SResource model= new SResource();
    	Cnd cnd= new Cnd();
    	model.getIntMap().put("userId", userId);
		cnd.andEquals("ur.USER_ID", "intMap.userId");
		model.setCondition(cnd);
		model.setIsPage(false);
    	return this.getMyBatisDaoManager().exeSelectCommand(model, "selectUserResources");
    }
    
    @Resource
	ISRoleResourceService sRoleResourceService;
    
	//==================定制内容结束======================
}
