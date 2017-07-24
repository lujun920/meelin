package com.meelin.core.base.service;

import com.meelin.core.base.model.BaseModel;
import org.springframework.dao.DataAccessException;

public interface BaseService<B extends BaseModel<B>>{

	/**
	 * 一个包含插入一条记录所需要信息的javabean
	 * @param model
	 * @return 插入记录的条件数(只有返回1才是正确的)
	 */
	public int saveRecord(BaseModel<?> model) throws DataAccessException;

	/**
	 * @param model
	 * @return
	 * @throws DataAccessException
	 */
	public int updateRecord(BaseModel<?> model) throws DataAccessException;

	/**
	 * 删除记录
	 * @param model
	 * @return
	 * @throws DataAccessException
	 */
	public int removeRecord(B model) throws DataAccessException;

	/**
	 * 获取实体Bean
	 * @param model
	 * @return
	 * @throws DataAccessException
	 */
	public B getRecord(B model) throws DataAccessException;

}