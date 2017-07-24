package com.meelin.core.base.service;

import javax.annotation.Resource;
import com.meelin.core.base.dao.MyBatisDaoManager;
import com.meelin.core.base.model.BaseModel;
import com.meelin.core.exception.ServiceException;
import com.meelin.core.util.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BaseServiceImpl<B extends BaseModel<B>> implements BaseService<B>{

	@Resource
	private MyBatisDaoManager myBatisDaoManager;

	private Class<? extends B> modelClass;

	protected MyBatisDaoManager getMyBatisDaoManager() {
		return this.myBatisDaoManager;
	}

	protected Class<? extends B> getModelClass() {
		if (this.modelClass == null) {
			this.modelClass = BeanUtils.getClassGenricType(getClass());
		}
		return this.modelClass;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public int saveRecord(BaseModel<?> model) throws DataAccessException {
		if(null == model){
			throw new ServiceException("模型实体不能为null");
		}
		return myBatisDaoManager.save(model);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public int updateRecord(BaseModel<?> model) throws DataAccessException {
		if(null == model){
			throw new ServiceException("模型实体不能为null");
		}
		return myBatisDaoManager.update(model);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public int removeRecord(B model) throws DataAccessException {
		if(null == model){
			throw new ServiceException("模型实体不能为null");
		}
		return myBatisDaoManager.remove(model);
	}

	@Override
	public B getRecord(B model) throws DataAccessException {
		if(null == model){
			throw new ServiceException("模型实体不能为null");
		}
		return (B) myBatisDaoManager.get(model);
	}

}