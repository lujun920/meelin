package com.meelin.core.service.impl;

import com.meelin.core.base.cnd.Cnd;
import com.meelin.core.base.cnd.UpdateSet;
import com.meelin.core.base.service.BaseServiceImpl;
import com.meelin.core.exception.ServiceException;
import com.meelin.core.model.SUser;
import com.meelin.core.service.ISUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("sUserService")
public class SUserServiceImpl extends BaseServiceImpl<SUser> implements ISUserService {

	// ==================定制内容开始======================
	// 在这里修改代码
	@Override
	public SUser selectOneUserByName(SUser model) throws Exception {
		Cnd cnd = new Cnd();
		if (!StringUtils.hasText(model.getUsername())) {
			throw new ServiceException("用户为空！");
		}
		cnd.andEquals("username");
		model.setCondition(cnd);
		return this.getMyBatisDaoManager().selectOne(model);
	}

	@Override
	public List<Map<String,Object>> findResourceByUserName(SUser model) throws Exception {
		Cnd cnd = new Cnd();
		cnd.andEquals("u.username", "username");
		model.setCondition(cnd);
		model.setIsPage(false);
		return this.getMyBatisDaoManager().exeSelectCommand(model, "findResourceByUserName");
	}

	@Override
	public List<Map<String, Object>> listUserMap(SUser model) throws Exception {
		Cnd cnd = new Cnd();
		if(StringUtils.hasText(model.getUsername())){
			model.getStrMap().put("username", new StringBuilder().append("%").append(model.getUsername()).append("%").toString());
			cnd.andLike("strMap.username");
		}
		model.setCondition(cnd);
		return this.getMyBatisDaoManager().selectMapList(model);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveUser(SUser model) throws Exception {
//		model.setPassword(hashedPassword);
		Integer result = this.getMyBatisDaoManager().save(model);
//		userInfo.setRegisterTime(new Date());
//		Cnd cnd= new Cnd();
//    	cnd.andEquals("username");
//    	model.setCondition(cnd);
//		List<User> listModel=  this.getMyBatisDaoManager().selectModelList(model);
//		Integer id= (Integer) listModel.get(0).getUserId();
//		userInfo.setInfoUserId(id);
//		this.getMyBatisDaoManager().save(userInfo);
		return result;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateUser(SUser model) throws Exception {
//		this.getMyBatisDaoManager().update(userInfo);
		return this.getMyBatisDaoManager().update(model);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateStatusToEnabled(SUser model) throws Exception {
		Cnd cnd = new Cnd();
		model.getStrList().add("2");
		cnd.andExpandOR("userId", "intList", model.getIntList()).andExpandOR(
				"enabled", "strList", model.getStrList());
		model.setCondition(cnd);
		UpdateSet updateSet = new UpdateSet().add("enabled");
		model.setEnabled("1");
		model.setUpdateSet(updateSet);
		return this.getMyBatisDaoManager().updateSome(model);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer updateStatusToDisabled(SUser model) throws Exception {
		Cnd cnd = new Cnd();
		model.getStrList().add("1");
		cnd.andExpandOR("userId", "intList", model.getIntList()).andExpandOR(
				"enabled", "strList", model.getStrList());
		model.setCondition(cnd);
		UpdateSet updateSet = new UpdateSet().add("enabled");
		model.setEnabled("2");
		model.setUpdateSet(updateSet);
		return this.getMyBatisDaoManager().updateSome(model);
	}
	
	@Override
	public List<Map<String, Object>> listUser(SUser model) throws Exception {
		Cnd cnd = new Cnd();
		if(StringUtils.hasText(model.getUsername())){
			model.getStrMap().put("username", new StringBuilder().append("%").append(model.getUsername()).append("%").toString());
			cnd.andLike("USERNAME","strMap.username");
		}
		model.setCondition(cnd);
		model.setIsPage(false);
		return this.getMyBatisDaoManager().selectMapList(model);
	}
	// ==================定制内容结束======================

	

}
