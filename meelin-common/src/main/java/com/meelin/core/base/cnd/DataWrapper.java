package com.meelin.core.base.cnd;


import com.meelin.core.base.model.BaseModel;

public class DataWrapper<B extends BaseModel<B>> implements IDataWrapper<B> {

	private B model;

	private ICmmAction<B> cmmAction;

	private SqlWrapper sqlWrapper;

	public DataWrapper(B model) {
		this.model = model;
		this.model.setDataWrapper(this);
	}

	public B getModel() {
		return this.model;
	}

	public void setModel(B model) {
		this.model = model;
		if (equals(this.model.getDataWrapper())) {
			this.model.setDataWrapper(this);
		}
	}

	public ICmmAction<B> getCmmAction() {
		if (this.cmmAction == null) {
			this.cmmAction = new CmmAction<B>(getModel());
		}
		return this.cmmAction;
	}

	public SqlWrapper getSqlWrapper() {
		if (this.sqlWrapper == null) {
			this.sqlWrapper = new SqlWrapper();
		}
		return this.sqlWrapper;
	}

}
