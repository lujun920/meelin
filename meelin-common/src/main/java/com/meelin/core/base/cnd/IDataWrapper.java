package com.meelin.core.base.cnd;


import com.meelin.core.base.model.BaseModel;

public abstract interface IDataWrapper<B extends BaseModel<B>> {
	
	public abstract ICmmAction<B> getCmmAction();

	public abstract B getModel();

	public abstract void setModel(B paramB);
	
	public abstract SqlWrapper getSqlWrapper();
	
}
