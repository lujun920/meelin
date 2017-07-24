package com.meelin.core.base.intercept;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>类文件: com.core.base.intercept.AvoidDuplicateSubmission.java
 * <p/>类功能描述:
 * 防止重复提交注解，用于注解方法上<br/>
 * 在新建页面方法上，设置needSaveToken()为true，此时拦截器会在Session中保存一个token，
 * 同时需要在新建的页面中添加
 * <input type="hidden" name="token" value="${token}">
 * <br/>
 * 保存方法需要验证重复提交的，设置needRemoveToken为true
 * 此时会在拦截器中验证是否重复提交
 * </p>
 *
 * @作者: luj
 * @时间: 2016/12/20 14:48
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidDuplicateSubmission {
	/**
	 * 保存Token,生成请求唯一标识,跳转至表单页的方法需要加上该注解
	 * @return
	 */
	boolean needSaveToken() default false;
	/**
	 * 移除Token,确保每次请求都是不同的Token防止表单重复提交,请求逻辑处理方法加上该注解
	 * @return
	 */
    boolean needRemoveToken() default false;
}
