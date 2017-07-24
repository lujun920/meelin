package com.meelin.core.base.cnd;
import com.meelin.core.base.model.BaseModel;
import com.meelin.core.exception.CoreBaseException;
import com.meelin.core.util.MyUtils;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

public abstract interface IIdNameStrategy {
	public abstract String getCommand(BaseModel<?> paramBaseModel,
									  IStmtCommand paramIStmtCommand, Configuration paramConfiguration);

	public abstract String getCommand(BaseModel<?> paramBaseModel,
									  String paramString, Configuration paramConfiguration);

	public static abstract interface IStmtCommand {
		public abstract String getSqlId();
	}

	public static class DefaultIdNameStrategy implements IIdNameStrategy {
		//
		private static final Logger logger = LoggerFactory.getLogger(DefaultIdNameStrategy.class);
		@SuppressWarnings("rawtypes")
		public String getCommand(BaseModel<?> baseModel, IStmtCommand stmtCommand, Configuration cfg) {
			ICmmAction<?> cmmAction = null;
			cmmAction = baseModel.getDataWrapper().getCmmAction();
			String modelClass = baseModel.getClass().getSimpleName();
			String nameSpace = cmmAction.getNameSpace();
			String commandId = stmtCommand.getSqlId();
			if (baseModel.getStmtCommand() != null) {
				commandId = baseModel.getStmtCommand().getSqlId();
			}
			String common = nameSpace + "." + commandId;
			logger.info("nameSpace common:{}", common);

			Collection<String> set= cfg.getMappedStatementNames();
			String better = common + modelClass;
			String stmt = null;
			if (cfg.hasStatement(better)) {
				stmt = better;
			} else if (cfg.hasStatement(common)) {
				stmt = common;
			} else {
				Class<?> parent = baseModel.getClass().getSuperclass();
				TypeVariable[] tv = parent.getTypeParameters();
				if (tv.length == 0) {
					BaseModel<?> parentModel = (BaseModel<?>) MyUtils.buildObject(parent);
					parentModel.setStmtCommand(baseModel.getStmtCommand());
					stmt = getCommand(parentModel, stmtCommand, cfg);
				}
			}
			if (!StringUtils.hasText(stmt)) {
				throw new CoreBaseException("没有检索到指定的Command:" + stmtCommand);
			}
			return stmt;
		}

		@SuppressWarnings("rawtypes")
		public String getCommand(BaseModel<?> baseModel, String command,
				Configuration cfg) {
			String stmt = command;
			if (stmt.indexOf(".") < 0) {
				ICmmAction<?> cmmAction = null;
				cmmAction = baseModel.getDataWrapper().getCmmAction();
				String nameSpace = cmmAction.getNameSpace();
				stmt = nameSpace + "." + stmt;
			}
			if (!cfg.hasStatement(stmt)) {
				Class<?> parent = baseModel.getClass().getSuperclass();
				TypeVariable[] tv = parent.getTypeParameters();
				if (tv.length == 0) {
					BaseModel<?> parentModel = (BaseModel<?>) MyUtils
							.buildObject(parent);
					parentModel.setStmtCommand(baseModel.getStmtCommand());
					stmt = getCommand(parentModel, stmt, cfg);
				} else {
					stmt = null;
				}
			}
			if (!StringUtils.hasText(stmt)) {
				throw new CoreBaseException("没有检索到指定的Command:" + command);
			}
			return stmt;
		}
	}

	public static enum CRUD implements IStmtCommand {
		save, update, forceUpdate, remove, get, load, selectMapList, selectModelList, selectMap, selectWithHandler, batchSave(
				"save"), batchUpdate("update"), batchForceUpdate("forceUpdate"), removeSome, updateSome;

		private String sqlId;

		private CRUD() {
			this.sqlId = toString();
		}

		private CRUD(String sqlId) {
			this.sqlId = sqlId;
		}

		public String getSqlId() {
			return this.sqlId;
		}
	}
}
