package com.meelin.test.controller;

import com.meelin.core.base.cnd.Cnd;
import com.meelin.core.base.controller.BaseController;
import com.meelin.core.model.SUser;
import com.meelin.core.service.ISUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p/>类文件: com.meelin.test.controller
 * <p/>类功能描述: ${todo}
 *
 * @作者: luj
 * @时间: 2016/12/21 11:15
 */
@Controller
@Scope("prototype")
@RequestMapping("/test")
public class TestController extends BaseController<SUser> {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @RequestMapping("/list")
    public void listUserMap(SUser model, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        userService.listUserMap(model);
        SUser u= new SUser();
        Cnd cnd= new Cnd();
//        user.updateRecord(u);
        u.setUserId(1);
        cnd.andEquals("userId");
        u.setCondition(cnd);
        List<Map<String, Object>> uList= sUserService.listUser(u);
        logger.info("uList size: {}", uList.size());
    }
    @Resource
    ISUserService sUserService;
}
