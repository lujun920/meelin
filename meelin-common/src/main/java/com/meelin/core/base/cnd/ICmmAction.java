package com.meelin.core.base.cnd;

import com.meelin.core.base.model.BaseModel;

public abstract interface ICmmAction<B extends BaseModel<B>> {
	
	public abstract ExeType getExeType();

	public static enum ExeType {
		SIMPLE, BATCH;
	}
	
	public abstract String getNameSpace();
	
}
