package com.meelin.core.base.cnd;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Cnd implements ISqlExp {

	private List<ISqlExp> cndExps;
	private List<ISqlExp> orderBys;
	private Set<String> orderBySet;
	private boolean where = false;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Cnd() {
		this(new ArrayList(4));
	}

	public Cnd(List<ISqlExp> cndExps) {
		setCndExps(cndExps);
	}

	public Cnd(ISqlExp cndExp) {
		this();
		if ((cndExp instanceof Cnd)) {
			Cnd cnd = (Cnd) cndExp;
			this.cndExps = cnd.cndExps;
			this.orderBys = cnd.orderBys;
			this.orderBySet = cnd.orderBySet;
			this.where = cnd.where;
		} else {
			append(cndExp);
		}
	}

	public Cnd append(ISqlExp cndExp) {
		if ((cndExp instanceof Cnd)) {
			Cnd cnd = (Cnd) cndExp;
			getCndExps().addAll(cnd.getCndExps());
			if (cnd.hasOrderBy()) {
				getOrderBys().addAll(cnd.getOrderBys());
				getOrderBySet().addAll(cnd.getOrderBySet());
				cnd.getOrderBys().clear();
				cnd.getOrderBySet().clear();
			}
		} else {
			getCndExps().add(cndExp);
		}
		return this;
	}

	public Cnd append(ISqlExp... cndExps) {
		for (ISqlExp cndExp : cndExps) {
			append(cndExp);
		}
		return this;
	}

	public Cnd append(String snippet) {
		return append(new SimpleSqlExp(snippet));
	}

	public Cnd append(String... snippets) {
		return append(new SimpleSqlExp(snippets));
	}

	public Cnd andAppend(String snippet) {
		if (!isEmpty()) {
			append(OP.AND);
		}
		return append(new SimpleSqlExp(snippet));
	}

	public Cnd AND(ISqlExp cndExp) {
		if ((!isEmpty()) && (!cndExp.isEmpty())) {
			append(OP.AND);
		}
		return append(cndExp);
	}

	public Cnd AND(String colName, OP op, String propName) {
		if (!StringUtils.hasText(colName)) {
			colName = propName;
			if (colName.lastIndexOf(".") > 0) {
				colName = colName.substring(colName.lastIndexOf(".") + 1);
			}
			colName = mapperColName(colName);
		}
		propName = "#{" + propName + "}";
		if (!isEmpty()) {
			append(OP.AND);
		}
		return append(new String[] { colName, op.getSql(), propName });
	}

	/**
	 * 等于
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andEquals(String propName) {
		return andEquals(null, propName);
	}

	/**
	 * 等于
	 * @param colName 字段名
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andEquals(String colName, String propName) {
		return AND(colName, OP.EQ, propName);
	}

	/**
	 * 不等于
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andNotEquals(String propName) {
		return andNotEquals(null, propName);
	}

	/**
	 * 不等于
	 * @param colName 字段名
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andNotEquals(String colName, String propName) {
		return AND(colName, OP.NEQ, propName);
	}

	/**
	 * 大于
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andGT(String propName) {
		return andGT(null, propName);
	}

	/**
	 * 大于
	 * @param colName 字段名
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andGT(String colName, String propName) {
		return AND(colName, OP.GT, propName);
	}

	/**
	 * 大于等于
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andGTE(String propName) {
		return andGTE(null, propName);
	}

	/**
	 * 大于等于
	 * @param colName 字段名
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andGTE(String colName, String propName) {
		return AND(colName, OP.GTE, propName);
	}

	/**
	 * 小于
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andLT(String propName) {
		return andLT(null, propName);
	}

	/**
	 * 小于
	 * @param colName 字段名
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andLT(String colName, String propName) {
		return AND(colName, OP.LT, propName);
	}

	/**
	 * 小于等于
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andLTE(String propName) {
		return andLTE(null, propName);
	}

	/**
	 * 小于等于
	 * @param colName 字段名
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andLTE(String colName, String propName) {
		return AND(colName, OP.LTE, propName);
	}

	/**
	 * 集合包含
	 * @param propName 属性名
	 * @param listName 标识名（strList,strMap）
	 * @param list 集合对象
	 * @return
	 */
	public Cnd andExpandOR(String propName, String listName, Collection<?> list) {
		return andExpandOR(null, propName, listName, list);
	}

	/**
	 * 集合包含
	 * @param colName 字段名
	 * @param propName 属性名
	 * @param listName 标识名（strList,strMap）
	 * @param list 集合对象
	 * @return
	 */
	public Cnd andExpandOR(String colName, String propName, String listName,
						   Collection<?> list) {
		colName = (String) getValue(colName, mapperColName(propName));

		propName = expandOR(colName, OP.EQ, listName, list);
		if (!getCndExps().isEmpty()) {
			append(OP.AND);
		}
		if (list.size() == 0) {
			return append("1=0");
		}
		return append(propName);
	}

	/**
	 * 模糊查询（匹配）
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andLike(String propName) {
		return andLike(null, propName);
	}

	/**
	 * 模糊查询（匹配）
	 * @param colName 字段名
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andLike(String colName, String propName) {
		return AND(colName, OP.LIKE, propName);
	}

	/**
	 * 是否区分大小写进行模糊查询（匹配）
	 * @param propName 属性名
	 * @param useUpper true:大写，false:小写
	 * @return
	 */
	public Cnd andLike(String propName, boolean useUpper) {
		return andLike(null, propName, useUpper);
	}

	/**
	 * 是否区分大小写进行模糊查询（匹配）
	 * @param colName 字段名
	 * @param propName 属性名
	 * @param useUpper true:大写，false:小写
	 * @return
	 */
	public Cnd andLike(String colName, String propName, boolean useUpper) {
		colName = (String) getValue(colName, mapperColName(propName));
		if (useUpper) {
			colName = "UPPER(" + colName + ")";
		} else {
			colName = "LOWER(" + colName + ")";
		}
		return AND(colName, OP.LIKE, propName);
	}

	/**
	 * 模糊查询（不匹配）
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andNotLike(String propName) {
		return andNotLike(null, propName);
	}

	/**
	 * 模糊查询（不匹配）
	 * @param colName 字段名
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andNotLike(String colName, String propName) {
		return AND(colName, OP.NLIKE, propName);
	}

	/**
	 * 是否区分大小写进行模糊查询（不匹配）
	 * @param propName 属性名
	 * @param useUpper true:大写，false:小写
	 * @return
	 */
	public Cnd andNotLike(String propName, boolean useUpper) {
		return andNotLike(null, propName, useUpper);
	}

	/**
	 * 是否区分大小写进行模糊查询（不匹配）
	 * @param colName 字段名
	 * @param propName 属性名
	 * @param useUpper true:大写，false:小写
	 * @return
	 */
	public Cnd andNotLike(String colName, String propName, boolean useUpper) {
		colName = (String) getValue(colName, mapperColName(propName));
		colName = "UPPER(" + colName + ")";
		return AND(colName, OP.NLIKE, propName);
	}

	/**
	 * 等于NULL
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andNull(String propName) {
		return AND(new SimpleSqlExp(mapperColName(propName) + " IS NULL"));
	}

	/**
	 * 等于NULL
	 * @param colName 字段名
	 * @return
	 */
	public Cnd andIsNull(String colName) {
		return AND(new SimpleSqlExp(colName + " IS NULL"));
	}

	/**
	 * 不等于NULL
	 * @param propName 属性名
	 * @return
	 */
	public Cnd andNotNull(String propName) {
		return AND(new SimpleSqlExp(mapperColName(propName) + " IS NOT NULL"));
	}

	/**
	 * 不等于NULL
	 * @param colName 字段名
	 * @return
	 */
	public Cnd andIsNotNull(String colName) {
		return AND(new SimpleSqlExp(colName + " IS NOT NULL"));
	}

	public Cnd OR(String colName, OP op, String propName) {
		if (!StringUtils.hasText(colName)) {
			colName = propName;
			if (colName.lastIndexOf(".") > 0) {
				colName = colName.substring(colName.lastIndexOf(".") + 1);
			}
			colName = mapperColName(colName);
		}
		propName = "#{" + propName + "}";
		if (!getCndExps().isEmpty()) {
			append(OP.OR);
		}
		return append(new String[] { colName, op.getSql(), propName });
	}

	public Cnd OR(ISqlExp cndExp) {
		if (!getCndExps().isEmpty()) {
			append(OP.OR);
		}
		return append(cndExp);
	}

	public Cnd orEquals(String propName) {
		return orEquals(null, propName);
	}

	public Cnd orEquals(String colName, String propName) {
		return OR(colName, OP.EQ, propName);
	}

	public Cnd orNotEquals(String propName) {
		return orNotEquals(null, propName);
	}

	public Cnd orNotEquals(String colName, String propName) {
		return OR(colName, OP.NEQ, propName);
	}

	public Cnd orGT(String propName) {
		return orGT(null, propName);
	}

	public Cnd orGT(String colName, String propName) {
		return OR(colName, OP.GT, propName);
	}

	public Cnd orGTE(String propName) {
		return orGTE(null, propName);
	}

	public Cnd orGTE(String colName, String propName) {
		return OR(colName, OP.GTE, propName);
	}

	public Cnd orLT(String propName) {
		return orLT(null, propName);
	}

	public Cnd orLT(String colName, String propName) {
		return OR(colName, OP.LT, propName);
	}

	public Cnd orLTE(String propName) {
		return orLTE(null, propName);
	}

	public Cnd orLTE(String colName, String propName) {
		return OR(colName, OP.LTE, propName);
	}

	public Cnd orExpandOR(String propName, String listName, Collection<?> list) {
		return orExpandOR(null, propName, listName, list);
	}

	public Cnd orExpandOR(String colName, String propName, String listName,
						  Collection<?> list) {
		if (list.size() == 0) {
			return this;
		}
		colName = (String) getValue(colName, mapperColName(propName));

		propName = expandOR(colName, OP.EQ, listName, list);
		if (!getCndExps().isEmpty()) {
			append(OP.OR);
		}
		return append(propName);
	}

	public Cnd orLike(String propName) {
		return orLike(null, propName);
	}

	public Cnd orLike(String colName, String propName) {
		return OR(colName, OP.LIKE, propName);
	}

	public Cnd orLike(String propName, boolean useUpper) {
		return orLike(null, propName, useUpper);
	}

	public Cnd orLike(String colName, String propName, boolean useUpper) {
		colName = (String) getValue(colName, mapperColName(propName));
		colName = "UPPER(" + colName + ")";
		return OR(colName, OP.LIKE, propName);
	}

	public Cnd orNotLike(String propName) {
		return orNotLike(null, propName);
	}

	public Cnd orNotLike(String colName, String propName) {
		return OR(colName, OP.NLIKE, propName);
	}

	public Cnd orNotLike(String propName, boolean useUpper) {
		return orNotLike(null, propName, useUpper);
	}

	public Cnd orNotLike(String colName, String propName, boolean useUpper) {
		colName = (String) getValue(colName, mapperColName(propName));
		colName = "UPPER(" + colName + ")";
		return OR(colName, OP.NLIKE, propName);
	}

	public Cnd orNull(String propName) {
		return OR(new SimpleSqlExp(mapperColName(propName) + " IS NULL"));
	}

	public Cnd orIsNull(String colName) {
		return OR(new SimpleSqlExp(colName + " IS NULL"));
	}

	public Cnd orNotNull(String propName) {
		return OR(new SimpleSqlExp(mapperColName(propName) + " IS NOT NULL"));
	}

	public Cnd orIsNotNull(String colName) {
		return OR(new SimpleSqlExp(colName + " IS NOT NULL"));
	}

	public Cnd group() {
		if (!isEmpty()) {
			getCndExps().add(0, new SimpleSqlExp("("));
			getCndExps().add(new SimpleSqlExp(")"));
		}
		return this;
	}

	public Cnd NOT() {
		if (!isEmpty()) {
			getCndExps().add(0, new SimpleSqlExp(OP.NOT.toString()));
		}
		return this;
	}

	/**
	 * 排序
	 * @param propName 属性名
	 * @return
	 */
	public Cnd orderBy(String propName) {
		return orderBy(propName, ORDER.ASC);
	}

	/**
	 * 排序
	 * @param propName 属性名
	 * @param order 排序方式
	 * @return
	 */
	public Cnd orderBy(String propName, ORDER order) {
		String colName = mapperColName(propName);
		orderBy(colName, order.name());
		return this;
	}

	/**
	 * 排序
	 * @param colName 字段名
	 * @param order 排序方式
	 * @return
	 */
	public Cnd orderBy(String colName, String order) {
		getOrderBys().add(new SimpleSqlExp(new String[] { colName, order }));
		getOrderBySet().add(colName);
		return this;
	}

	/**
	 * 清空比较条件
	 * @return
	 */
	public Cnd resetCnd() {
		getCndExps().clear();
		return this;
	}

	/**
	 * 清空排序条件
	 * @return
	 */
	public Cnd resetOrderBy() {
		setOrderBys(null);
		this.orderBySet = null;
		return this;
	}

	/**
	 * 清空所有条件
	 * @return
	 */
	public Cnd reset() {
		resetOrderBy();
		resetCnd();
		return this;
	}

	public static String expandIn(String listName, Collection<?> list) {
		StringBuilder builder = new StringBuilder("(");
		Iterator<?> iterator = list.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Object item = iterator.next();
			if (i != 0) {
				builder.append(",");
			}
			builder.append("#{").append(listName).append("[").append(i++)
					.append("]");
			if (item == null) {
				builder.append(",javaType=").append("java.lang.Object");
			} else {
				builder.append(",javaType=").append(item.getClass().getName());
			}
			builder.append("}");
		}
		builder.append(")");
		return builder.toString();
	}

	public static String expandOR(String colName, OP op,
			String listName, Collection<?> list) {
		StringBuilder builder = new StringBuilder("(");
		int index = 0;
		for (Object item : list) {
			if (index != 0) {
				builder.append(" ").append(OP.OR).append(" ");
			}
			builder.append(colName).append(" ").append(op).append(" ");
			builder.append("#{").append(listName).append("[").append(index++)
					.append("]");
			if (item == null) {
				builder.append(",javaType=").append("java.lang.Object");
			} else {
				builder.append(",javaType=").append(item.getClass().getName());
			}
			builder.append("}");
		}
		builder.append(")");
		return builder.toString();
	}

	public boolean isWhere() {
		return this.where;
	}

	public boolean hasOrderBy() {
		return (getOrderBys() != null) && (!getOrderBys().isEmpty());
	}

	public boolean hasOrderBy(String prop) {
		if (this.orderBySet == null) {
			return false;
		}
		String colName = mapperColName(prop);
		return getOrderBySet().contains(colName);
	}

	public void setWhere(boolean where) {
		this.where = where;
	}

	public boolean isEmpty() {
		List<ISqlExp> cndExps = getCndExps();
		for (ISqlExp cndExp : cndExps) {
			if (!cndExp.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public String getSql() {
		List<ISqlExp> cndExps = getCndExps();
		StringBuilder builder = new StringBuilder();
		if ((!isEmpty()) && (isWhere())) {
			builder.append(" WHERE ");
		}
		for (int i = 0; i < cndExps.size(); i++) {
			if (i != 0) {
				builder.append(" ");
			}
			builder.append(((ISqlExp) cndExps.get(i)).getSql());
		}
		if (!getOrderBys().isEmpty()) {
			builder.append(" ORDER BY ");
		}
		for (int i = 0; i < getOrderBys().size(); i++) {
			if (i != 0) {
				builder.append(",");
			}
			builder.append(((ISqlExp) getOrderBys().get(i)).getSql());
		}
		return builder.toString();
	}

	public String toString() {
		return getSql();
	}

	public Cnd getCopy() {
		Cnd cnd = new Cnd(getCndExps());
		if (hasOrderBy()) {
			cnd.setOrderBys(new ArrayList<ISqlExp>(getOrderBys()));
			cnd.orderBySet = new HashSet<String>(this.orderBySet);
		}
		return cnd;
	}

	private Set<String> getOrderBySet() {
		return this.orderBySet == null ? (this.orderBySet = new HashSet<String>())
				: this.orderBySet;
	}

	public List<ISqlExp> getOrderBys() {
		return this.orderBys == null ? (this.orderBys = new ArrayList<ISqlExp>())
				: this.orderBys;
	}

	public void setOrderBys(List<ISqlExp> orderBys) {
		this.orderBys = orderBys;
	}

	public void setCndExps(List<ISqlExp> cndExps) {
		this.cndExps = cndExps;
	}

	public List<ISqlExp> getCndExps() {
		return this.cndExps;
	}

	public static <T> T getValue(T value, T dv) {
		return value == null ? dv : value;
	}

	public static String mapperColName(String propName) {
		return PROPERTY.matcher(propName).replaceAll("$1_$2").toUpperCase();
	}

	public static final Pattern PROPERTY = Pattern.compile("([a-z])([A-Z])");
	public static final Pattern CHECK = Pattern.compile("[0-9A-Za-z]{2,32}?");
}
