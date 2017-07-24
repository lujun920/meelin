package com.meelin.core.controller;

import javax.annotation.Resource;

import com.meelin.core.base.controller.BaseController;
import com.meelin.core.model.SLogger;
import com.meelin.core.service.ISLoggerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class SLoggerController extends BaseController<SLogger> {
	//==================定制内容开始======================
//在这里修改代码
	//==================定制内容结束======================

	@Resource
	ISLoggerService sLoggerService;
}
