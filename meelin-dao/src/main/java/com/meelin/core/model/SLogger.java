package com.meelin.core.model;

import com.meelin.core.base.model.BaseModel;

import java.util.Date;

/**
 * (SLogger)模型对象
 * 
 * @version 1.0
 */
public class SLogger extends BaseModel<SLogger> {
	//======================字段列表========================
	
	private static final long serialVersionUID = 1L;
	
	/** ID */
	private String	logId;

	/** 操作系统 */
	private String	logOs;

	/** 浏览器 */
	private String	logBrowser;

	/** IP地址 */
	private String	logIp;

	/** 操作类型 */
	private String	logOperateType;

	/** 内容描述 */
	private String	logContext;

	/** 操作时间 */
	private Date	logTime;

	/** 操作者 */
	private String	logOperator;

	/** 访问URL */
	private String	logUrl;
	public SLogger(){}
	public SLogger(String logOs, String logBrowser, String logIp, String logOperateType, String logContext, String logOperator){
		this.logOs= logOs;
		this.logBrowser= logBrowser;
		this.logIp= logIp;
		this.logOperateType= logOperateType;
		this.logContext= logContext;
		this.logOperator= logOperator;
	}

	public String getLogId() {
		return this.logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getLogOs() {
		return this.logOs;
	}

	public void setLogOs(String logOs) {
		this.logOs = logOs;
	}

	public String getLogBrowser() {
		return this.logBrowser;
	}

	public void setLogBrowser(String logBrowser) {
		this.logBrowser = logBrowser;
	}

	public String getLogIp() {
		return this.logIp;
	}

	public void setLogIp(String logIp) {
		this.logIp = logIp;
	}

	public String getLogOperateType() {
		return this.logOperateType;
	}

	public void setLogOperateType(String logOperateType) {
		this.logOperateType = logOperateType;
	}

	public String getLogContext() {
		return this.logContext;
	}

	public void setLogContext(String logContext) {
		this.logContext = logContext;
	}

	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getLogOperator() {
		return this.logOperator;
	}

	public void setLogOperator(String logOperator) {
		this.logOperator = logOperator;
	}

	public String getLogUrl() {
		return this.logUrl;
	}

	public void setLogUrl(String logUrl) {
		this.logUrl = logUrl;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("\n");
		builder.append("\tlogId(ID):").append(logId);
		builder.append("\tlogOs(操作系统):").append(logOs);
		builder.append("\tlogBrowser(浏览器):").append(logBrowser);
		builder.append("\tlogIp(IP地址):").append(logIp);
		builder.append("\n");
		builder.append("\tlogOperateType(操作类型):").append(logOperateType);
		builder.append("\tlogContext(内容描述):").append(logContext);
		builder.append("\tlogTime(操作时间):").append(logTime);
		builder.append("\tlogOperator(操作者):").append(logOperator);
		builder.append("\n");
		builder.append("\tlogUrl(访问URL):").append(logUrl);
		return builder.toString();
	}
	//==================定制内容开始======================

	//==================定制内容结束======================
}
