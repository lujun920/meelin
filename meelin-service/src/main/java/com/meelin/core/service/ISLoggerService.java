package com.meelin.core.service;
import com.meelin.core.base.service.BaseService;
import com.meelin.core.model.SLogger;

import java.util.List;

public interface ISLoggerService extends BaseService<SLogger> {
	//==================定制内容开始======================
	public Integer saveLogger(SLogger model) throws Exception;

	/**
	 * <p/>方法功能描述: 保存日志记录
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 15:53
	 */
	public Integer saveLogger(String context, String operateType) throws Exception;
	/**
	 * <p/>方法功能描述: 批量保存日志记录
	 * 
	 * @作者: luj
	 * @时间: 2016/12/20 16:26
	 */
	public Integer saveBatchLogger(List<SLogger> model) throws Exception;
	//==================定制内容结束======================
}
