package com.meelin.core.model;

import com.meelin.core.base.model.BaseModel;

/**
 * (SResource)模型对象
 * 
 * @version 1.0
 */
public class SResource extends BaseModel<SResource> {
	//======================字段列表========================
	
	private static final long serialVersionUID = 1L;
	
	/** 资源ID */
	private Integer	rsId;
	/** 资源KEY(操作是否显示) */
	private String	rsKey;
	/** 权限许可Key(权限拦截控制) */
	private String	permitsKey;
	/** 父节点 */
	private Integer	rspId;

	/** 资源名称 */
	private String	name;

	/** 资源URL */
	private String	uri;

	/** 资源类型 */
	private String	type;

	/** 排序号 */
	private Integer	sort;

	/** 状态 */
	private String	enabled;

	public Integer getRsId() {
		return this.rsId;
	}

	public void setRsId(Integer rsId) {
		this.rsId = rsId;
	}

	public Integer getRspId() {
		return this.rspId;
	}

	public void setRspId(Integer rspId) {
		this.rspId = rspId;
	}

	public String getRsKey() {
		return rsKey;
	}

	public void setRsKey(String rsKey) {
		this.rsKey = rsKey;
	}

	public String getPermitsKey() {
		return permitsKey;
	}

	public void setPermitsKey(String permitsKey) {
		this.permitsKey = permitsKey;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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
		builder.append("\trsId(资源ID):").append(rsId);
		builder.append("\trspId(父节点):").append(rspId);
		builder.append("\trsKey(资源KEY):").append(rsKey);
		builder.append("\tpermitsKey(权限KEY):").append(permitsKey);
		builder.append("\tname(资源名称):").append(name);
		builder.append("\n");
		builder.append("\turl(资源URI):").append(uri);
		builder.append("\ttype(资源类型):").append(type);
		builder.append("\tsort(排序号):").append(sort);
		builder.append("\tenabled(状态):").append(enabled);
		return builder.toString();
	}
	//==================定制内容开始======================
	/**
	 * 启用
	 */
	public static final String ENABLED_1= "1";
	/**
	 * 禁用
	 */
	public static final String ENABLED_2= "2";
	//==================定制内容结束======================
}
