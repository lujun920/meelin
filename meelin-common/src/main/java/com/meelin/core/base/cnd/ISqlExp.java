package com.meelin.core.base.cnd;

public abstract interface ISqlExp {
	public static final ISqlExp EMPTY = new ISqlExp() {
		public boolean isEmpty() {
			return true;
		}

		public String getSql() {
			return "";
		}

		public String toString() {
			return "";
		}
	};
	public static final String OPEN = "#{";
	public static final String CLOSE = "}";

	public abstract boolean isEmpty();

	public abstract String getSql();

	public static enum OP implements ISqlExp {
		AND("AND"), OR("OR"), NOT("NOT"), LP("("), RP(")"), LT("<"), LTE("<="), GT(
				">"), GTE(">="), EQ("="), NEQ("<>"), LIKE("LIKE"), NLIKE(
				"NOT LIKE"), IN("IN"), NIN("NOT IN"), EXIST("EXIST"), NEXIST(
				"NOT EXIST"), NULL("IS NULL"), NNULL("NOT IS NULL"), ORDER(
				"ORDER");

		private String key;

		private OP(String key) {
			this.key = key;
		}

		public boolean isNullType() {
			return (this == NULL) || (this == NNULL);
		}

		public boolean isInType() {
			return (this == IN) || (this == NIN);
		}

		public boolean isEQType() {
			return (this == LT) || (this == LTE) || (this == GT)
					|| (this == GTE) || (this == EQ) || (this == NEQ)
					|| (this == LIKE) || (this == NLIKE);
		}

		public String toString() {
			return this.key;
		}

		public boolean isEmpty() {
			return false;
		}

		public String getSql() {
			return this.key;
		}
	}

	public static enum ORDER {
		ASC, DESC;
	}
}
