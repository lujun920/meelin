package com.meelin.core.base.controller;

import com.meelin.core.base.model.BaseModel;
import com.meelin.core.base.service.BaseService;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController<B extends BaseModel<B>> {
	
	private BaseService<B> baseService;

	public BaseService<B> getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService<B> baseService) {
		this.baseService = baseService;
	}

}
