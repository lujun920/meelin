package com.meelin.core.model;

import com.meelin.core.base.model.BaseModel;

/**
 * (SUser)模型对象
 * 
 * @version 1.0
 */
public class SUser extends BaseModel<SUser> {
	//======================字段列表========================
	
	private static final long serialVersionUID = 1L;
	
	/** 用户ID */
	private Integer	userId;

	/** 用户名 */
	private String	username;

	/** 密码 */
	private String	password;

	/** 状态 */
	private String	enabled;

	/** 描述 */
	private String	description;
	/** salt */
	private String	salt;
	/** 登录次数 */
	private Integer	loginCount;
	/** 错误次数 */
	private Integer	errorCount;
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("\n");
		builder.append("\tuserId(用户ID):").append(userId);
		builder.append("\tusername(用户名):").append(username);
		builder.append("\tpassword(密码):").append(password);
		builder.append("\tenabled(状态):").append(enabled);
		builder.append("\n");
		builder.append("\tdescription(描述):").append(description);
		return builder.toString();
	}
	//==================定制内容开始======================

	
	//==================定制内容结束======================
}
