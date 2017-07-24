package com.meelin.ass.shiro;

import com.meelin.core.model.SUser;
import com.meelin.core.service.ISUserService;
import com.meelin.core.util.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by lujun on 2016/12/25.
 */
public class MyShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);
    /**
     * <p/>方法功能描述: 登录授权
     * 
     * @作者: luj
     * @时间: 2017/1/14 15:30
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName=  principalCollection.getPrimaryPrincipal().toString();
        SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
        try {
            SUser user = new SUser();
            user.setUsername(userName);
            List<Map<String,Object>> resList= sUserService.findResourceByUserName(user);

            for(Map<String,Object>  res: resList){
                if(null!=res.get("PERMITS_KEY") && !res.get("PERMITS_KEY").toString().equals("")){
                    logger.info("权限: {}", res.get("PERMITS_KEY").toString());
                    auth.addStringPermission(res.get("PERMITS_KEY").toString());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auth;
    }
    /**
     * <p/>方法功能描述: 登录认证
     *
     * @作者: luj
     * @时间: 2017/1/14 15:30
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("token userName: {}", token.getUsername());
//        SUser user= new SUser();
//        user.setUsername(token.getUsername());
//        try {
//            user= sUserService.selectOneUserByName(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        SUser user= (SUser) getSession().getAttribute(Constants.USER_SESSION_TEMP);
        if(user.getUserId()==null){
            return null;
        }
        if(user.getEnabled().equals("2")){
            throw new AuthenticationException("msg:该已帐号禁止登录!");
        }

        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), getName());
    }

    /**
     * <p/>方法功能描述: 清除权限,刷新缓存
     *
     * @作者: luj
     * @时间: 2017/1/14 15:33
     */
    public void removeUserAuthorizationInfoCache(String username){
        SimplePrincipalCollection pc = new SimplePrincipalCollection();
        pc.add(username, super.getName());
        super.clearCachedAuthorizationInfo(pc);
    }
    private void setSession(Object key, Object value){
        Session session = getSession();
        logger.info("session默认超时时间为[{}]毫秒", session.getTimeout());
        if(null != session){
            session.setAttribute(key, value);
        }
    }
    private Session getSession(){
        try{
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null){
                session = subject.getSession();
            }
            if (session != null){
                return session;
            }
        }catch (InvalidSessionException e){

        }
        return null;
    }
    @Resource
    ISUserService sUserService;


}
