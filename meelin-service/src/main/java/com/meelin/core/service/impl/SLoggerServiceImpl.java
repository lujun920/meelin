package com.meelin.core.service.impl;

import com.meelin.core.base.service.BaseServiceImpl;
import com.meelin.core.model.SLogger;
import com.meelin.core.service.ISLoggerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service("sLoggerService")
public class SLoggerServiceImpl extends BaseServiceImpl<SLogger> implements ISLoggerService {
	//==================定制内容开始======================
//在这里修改代码
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveLogger(SLogger model) throws Exception{
//		model.setLogId(serialNoService.getSerialNo(SystemTypeEnum.SerialNo.SERIALNO_NORMAL.getValue()));
		model.setLogTime(new Date());
		return this.getMyBatisDaoManager().save(model);
	}
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveLogger(String context, String operateType) throws Exception{
		//业务层获取request session对象
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		SLogger model= new SLogger();
//		model.setLogId(serialNoService.getSerialNo(SystemTypeEnum.SerialNo.SERIALNO_NORMAL.getValue()));
//		model.setLogOs(UserSession.getInstance(request).getOs());
//		model.setLogBrowser(UserSession.getInstance(request).getBrowser());
//		model.setLogIp(UserSession.getInstance(request).getIp());
//		model.setLogOperator(UserSession.getInstance(request).getUser().getUsername());
		model.setLogOperateType(operateType);
		model.setLogContext(context);
		model.setLogTime(new Date());
		return this.getMyBatisDaoManager().save(model);
	}
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Integer saveBatchLogger(List<SLogger> model) throws Exception{
		return this.getMyBatisDaoManager().saveBatch(model, "saveBatchLogger");
	}
//	@Resource
//	SerialNoService serialNoService;
	//==================定制内容结束======================
}
