package com.meelin.core.base.cnd;
import com.meelin.core.util.MyUtils;
import org.springframework.util.StringUtils;

public class SimpleSqlExp implements ISqlExp {
	private String snippet;

	public SimpleSqlExp(String snippet) {
		this.snippet = snippet;
	}

	public SimpleSqlExp(String... snippets) {
		this.snippet = StringUtils.arrayToDelimitedString(snippets, " ");
	}

	public SimpleSqlExp append(String... snippets) {
		this.snippet = StringUtils.arrayToDelimitedString(snippets, " ");
		return this;
	}

	public boolean isEmpty() {
		return !StringUtils.hasText(this.snippet);
	}

	public String getSql() {
		return (String) MyUtils.getValue(this.snippet, "");
	}

	public String toString() {
		return getSql();
	}
}
