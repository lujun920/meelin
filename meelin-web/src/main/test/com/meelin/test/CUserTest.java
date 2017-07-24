package com.meelin.test;

import com.meelin.core.base.cnd.Cnd;
import com.meelin.core.base.cnd.UpdateSet;
import com.meelin.core.model.SUser;
import com.meelin.core.service.impl.SUserServiceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;

//import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
//import net.sourceforge.groboutils.junit.v1.TestRunnable;

/**
 * <p/>类文件: com.core.test
 * <p/>类功能描述: ${todo}
 *
 * @作者: luj
 * @时间: 2016/5/17 17:04
 */
public class CUserTest {
    private static final Logger logger = LoggerFactory.getLogger(CUserTest.class);
    private static ApplicationContext ctx = null;

    static {
        ctx = new ClassPathXmlApplicationContext(new String[] {"file:src/main/resources/public/applicationContext.xml", "file:src/main/resources/public/mybatis.xml"});
    }
    @Test
    public void getUser() throws Throwable {

        SUserServiceImpl user= (SUserServiceImpl)ctx.getBean("sUserService");
        SUser u= new SUser();
        UpdateSet updateSet = new UpdateSet().add("userName");

        u.setUsername("名称666");
        u.setUpdateSet(updateSet);
        Cnd cnd= new Cnd();
//        cnd.andEquals("userId");
//        u.setCondition(cnd);
//        user.updateRecord(u);
        u.setUserId(1);
        cnd.andEquals("userId");
        u.setCondition(cnd);
        List<Map<String, Object>> uList= user.listUser(u);
        logger.info("uList size: {}", uList.size());


    }


}
