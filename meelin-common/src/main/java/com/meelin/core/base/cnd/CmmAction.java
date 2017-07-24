package com.meelin.core.base.cnd;


import com.meelin.core.base.model.BaseModel;

public class CmmAction<B extends BaseModel<B>> implements ICmmAction<B> {
	
	private static final ThreadLocal<ExeType> EXE_TYPE = new ThreadLocal<ExeType>();
	
	private B model;

	public CmmAction(B model) {
		this.model = model;
	}
	
	// @JSON(serialize=false)
	@SuppressWarnings("unchecked")
	public Class<B> getModelClass() {
		return (Class<B>) this.model.getClass();
	}
	
	public void setExeType(ExeType type) {
		EXE_TYPE.set(type);
	}

	public ExeType getExeType() {
		return (ExeType) EXE_TYPE.get();
	}

	// @JSON(serialize=false)
	public String getNameSpace() {
		Class<B> modelClass = getModelClass();
		String result = modelClass.getName();
		result = result.substring(0, result.lastIndexOf("."));
		result = result.substring(0, result.lastIndexOf("."));
		result = result + ".dao.mapper.I";
		result = result + modelClass.getSimpleName() + "Mapper";
		return result;
	}
}
