package com.meelin.core.model;

import com.meelin.core.base.model.BaseModel;

/**
 * (SUserRole)模型对象
 * 
 * @version 1.0
 */
public class SUserRole extends BaseModel<SUserRole> {
	//======================字段列表========================
	
	private static final long serialVersionUID = 1L;
	
	/** ID */
	private Integer	id;

	/** 用户ID */
	private Integer	userId;

	/** 角色ID */
	private Integer	roleId;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("\n");
		builder.append("\tid(ID):").append(id);
		builder.append("\tuserId(用户ID):").append(userId);
		builder.append("\troleId(角色ID):").append(roleId);
		return builder.toString();
	}
	//==================定制内容开始======================

	//==================定制内容结束======================
}
