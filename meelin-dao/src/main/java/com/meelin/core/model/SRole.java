package com.meelin.core.model;

import com.meelin.core.base.model.BaseModel;

/**
 * (SRole)模型对象
 * 
 * @version 1.0
 */
public class SRole extends BaseModel<SRole> {
	//======================字段列表========================
	
	private static final long serialVersionUID = 1L;
	
	/** 角色ID */
	private Integer	roleId;

	/** 角色名称 */
	private String	name;

	/** 状态 */
	private String	enabled;

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("\n");
		builder.append("\troleId(角色ID):").append(roleId);
		builder.append("\tname(角色名称):").append(name);
		builder.append("\tenabled(状态):").append(enabled);
		return builder.toString();
	}
	//==================定制内容开始======================

	//==================定制内容结束======================
}
