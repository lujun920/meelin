package com.meelin.core.base.cnd;
import com.meelin.core.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class UpdateSet implements ISqlExp {
	private List<ISqlExp> setExps;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public UpdateSet() {
		this(new ArrayList());
	}

	public UpdateSet(List<ISqlExp> setExps) {
		this.setExps = setExps;
	}

	public UpdateSet append(ISqlExp cndExp) {
		this.setExps.add(cndExp);
		return this;
	}

	public UpdateSet append(String snippet) {
		return append(new SimpleSqlExp(snippet));
	}

	public UpdateSet add(String propName) {
		String realName = propName;
		if (propName.indexOf(":") > 0) {
			realName = propName.substring(0, propName.indexOf(":"));
		}
		if (propName.indexOf(",") > 0) {
			realName = propName.substring(0, propName.indexOf(","));
		}
		return add(MyUtils.mapperColName(realName.trim()), propName);
	}

	public UpdateSet add(String colName, String propName) {
		StringBuilder sb = new StringBuilder();
		sb.append(MyUtils.mapperColName(colName)).append(OP.EQ);
		sb.append("#{").append(propName).append("}");
		return append(new SimpleSqlExp(sb.toString()));
	}

	public boolean isEmpty() {
		List<ISqlExp> setExps = this.setExps;
		for (ISqlExp cndExp : setExps) {
			if (!cndExp.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public String getSql() {
		List<ISqlExp> setExps = this.setExps;
		StringBuilder builder = new StringBuilder();
		int sign = 0;
		for (int i = 0; i < setExps.size(); i++) {
			if (((ISqlExp) setExps.get(i)).isEmpty()) {
				sign++;
			} else {
				if (i != sign) {
					builder.append(", ");
				}
				builder.append(((ISqlExp) setExps.get(i)).getSql());
			}
		}
		return builder.toString();
	}

	public String toString() {
		return getSql();
	}
}
