package com.meelin.core.model;


import com.meelin.core.base.model.BaseModel;

/**
 * (SRoleResource)模型对象
 * 
 * @version 1.0
 */
public class SRoleResource extends BaseModel<SRoleResource> {
	//======================字段列表========================
	
	private static final long serialVersionUID = 1L;
	
	/** ID */
	private Integer	id;

	/** 角色ID */
	private Integer	roleId;

	/** 资源ID */
	private Integer	rsId;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRsId() {
		return this.rsId;
	}

	public void setRsId(Integer rsId) {
		this.rsId = rsId;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("\n");
		builder.append("\tid(ID):").append(id);
		builder.append("\troleId(角色ID):").append(roleId);
		builder.append("\trsId(资源ID):").append(rsId);
		return builder.toString();
	}
	//==================定制内容开始======================

	//==================定制内容结束======================
}
