package com.meelin.core.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meelin.core.base.cnd.DataWrapper;
import com.meelin.core.base.cnd.IDataWrapper;
import com.meelin.core.base.cnd.IIdNameStrategy;
import com.meelin.core.base.cnd.ISqlExp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseModel<B extends BaseModel<B>> implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private transient IDataWrapper<B> dataWrapper;

	private transient ISqlExp condition = ISqlExp.EMPTY;
	
	private List<String> strList;
	
	private List<Integer> intList;
	
	private Map<String, String> strMap;
	
	private Map<String, Integer> intMap;
	
	private Map<String, Date> dateMap;
	
	private Map<String, Object> objMap;
	
	private Boolean isPage;
	
	private Integer start;

	private Integer length;

	private Integer recordsTotal;
	
	private Integer recordsFiltered;
	
	private List<Map<String,Object>> data;
	
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	@JsonIgnore
	public Boolean getIsPage() {
		return this.isPage = this.isPage == null ? true : this.isPage;
	}

	public void setIsPage(Boolean isPage) {
		this.isPage = isPage;
	}

	public List<String> getStrList() {
		return this.strList = this.strList == null ? new ArrayList<String>() : this.strList;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}
	
	public List<Integer> getIntList() {
		return this.intList = this.intList == null ? new ArrayList<Integer>() : this.intList;
	}

	public void setIntList(List<Integer> intList) {
		this.intList = intList;
	}

	public Map<String, String> getStrMap() {
		return this.strMap = this.strMap == null ? new HashMap<String,String>() : this.strMap;
	}

	public void setStrMap(Map<String, String> strMap) {
		this.strMap = strMap;
	}
	
	public Map<String, Integer> getIntMap() {
		return this.intMap = this.intMap == null ? new HashMap<String,Integer>() : this.intMap;
	}

	public void setIntMap(Map<String, Integer> intMap) {
		this.intMap = intMap;
	}
	
	public Map<String, Date> getDateMap() {
		return this.dateMap = this.dateMap == null ? new HashMap<String,Date>() : this.dateMap;
	}

	public void setDateMap(Map<String, Date> dateMap) {
		this.dateMap = dateMap;
	}

	public Map<String, Object> getObjMap() {
		return this.objMap = this.objMap == null ? new HashMap<String,Object>() : this.objMap;
	}

	public void setObjMap(Map<String, Object> objMap) {
		this.objMap = objMap;
	}

	@JsonIgnore
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IDataWrapper<B> getDataWrapper() {
		if (this.dataWrapper == null) {
			this.dataWrapper = new DataWrapper(this);
		}
		return this.dataWrapper;
	}

	@SuppressWarnings("unchecked")
	public void setDataWrapper(IDataWrapper<B> dataWrapper) {
		this.dataWrapper = dataWrapper;
		if (!equals(this.dataWrapper.getModel())) {
			this.dataWrapper.setModel((B) this);
		}
	}
	
	@JsonIgnore
	public ISqlExp getDynamicSQL() {
		return getDataWrapper().getSqlWrapper().getDynamicSQL();
	}

	public void setDynamicSQL(ISqlExp dynamicSQL) {
		getDataWrapper().getSqlWrapper().setDynamicSQL(dynamicSQL);
	}
	  
	@JsonIgnore
	public IIdNameStrategy.IStmtCommand getStmtCommand() {
		return getDataWrapper().getSqlWrapper().getStmtCommand();
	}

	public void setStmtCommand(IIdNameStrategy.IStmtCommand stmtCommand) {
		getDataWrapper().getSqlWrapper().setStmtCommand(stmtCommand);
	}

	@JsonIgnore
	public ISqlExp getCondition() {
		return this.condition == null ? ISqlExp.EMPTY : this.condition;
	}

	public void setCondition(ISqlExp cndExp) {
		this.condition = cndExp;
	}
	
	@JsonIgnore
	public ISqlExp getUpdateSet() {
		return getDataWrapper().getSqlWrapper().getUpdateSet();
	}

	public void setUpdateSet(ISqlExp updateSet) {
		getDataWrapper().getSqlWrapper().setUpdateSet(updateSet);
	}
}
