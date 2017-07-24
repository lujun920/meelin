package com.meelin.core.base.cnd;


public class SqlWrapper {
	private transient ISqlExp updateSet = ISqlExp.EMPTY;
	
	private transient ISqlExp dynamicSQL = ISqlExp.EMPTY;
	
	private transient IIdNameStrategy.IStmtCommand stmtCommand = null;

//	@JSON(serialize = false)
	public ISqlExp getUpdateSet() {
		return this.updateSet == null ? ISqlExp.EMPTY : this.updateSet;
	}

	public void setUpdateSet(ISqlExp updateSet) {
		this.updateSet = updateSet;
	}

//	@JSON(serialize = false)
	public ISqlExp getDynamicSQL() {
		return this.dynamicSQL == null ? ISqlExp.EMPTY : this.dynamicSQL;
	}

	public void setDynamicSQL(ISqlExp dynamicSQL) {
		this.dynamicSQL = dynamicSQL;
	}

//	@JSON(serialize = false)
	public IIdNameStrategy.IStmtCommand getStmtCommand() {
		return this.stmtCommand;
	}

	public void setStmtCommand(IIdNameStrategy.IStmtCommand stmtCommand) {
		this.stmtCommand = stmtCommand;
	}
}
