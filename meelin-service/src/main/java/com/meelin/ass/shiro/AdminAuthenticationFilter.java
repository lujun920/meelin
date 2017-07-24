package com.meelin.ass.shiro;

import com.meelin.core.model.SUser;
import com.meelin.core.service.ISResourceService;
import com.meelin.core.service.ISUserService;
import com.meelin.core.support.UserSession;
import com.meelin.core.util.Constants;
import com.meelin.core.util.TreeNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p/>类文件: com.meelin.ass.shiro
 * <p/>类功能描述: 后台登录拦截器
 *
 * @作者: luj
 * @时间: 2017/1/16 16:31
 */
public class AdminAuthenticationFilter extends FormAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(AdminAuthenticationFilter.class);
    /**
     * 验证码名称
     */
    public static final String CAPTCHA_PARAM = "captcha";
    /**
     * 登录次数超出allowLoginNum时，存储在session记录是否展示验证码的key默认名称
     */
    public static final String DEFAULT_SHOW_CAPTCHA_KEY_ATTRIBUTE = "showCaptcha";

    /**
     * 默认在session中存储的登录次数名称
     */
    private static final String DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE = "loginNum";
    /**
     * 允许登录次数，当登录次数大于该数值时，会在页面中显示验证码
     */
    private Integer allowLoginNum = 1;


    /**
     * 返回URL
     */
//    public static final String RETURN_URL = "returnUrl";
    /**
     * 登录错误地址
     */
//    public static final String FAILURE_URL = "failureUrl";

    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//        AuthenticationToken token = this.createToken(request, response);
//        if (token == null) {
//            String msg = "create AuthenticationToken error";
//            throw new IllegalStateException(msg);
//        }
        Session session = getSubject(request, response).getSession();
        //获取登录次数
        Integer number = (Integer) session.getAttribute(DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE);
        //首次登录，将该数量记录在session中
        if (number == null) {
            number = new Integer(1);
            session.setAttribute(DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE, number);
        }
        //如果登录次数大于allowLoginNum，需要判断验证码是否一致
        if (number > getAllowLoginNum()) {
            //获取当前session验证码
            String currentCaptcha = (String) session.getAttribute(Constants.VALIDATE_CODE);
            //获取用户输入的验证码
            String submitCaptcha = WebUtils.getCleanParam(request, CAPTCHA_PARAM);
            //如果验证码不匹配，登录失败
            if (StringUtils.isEmpty(submitCaptcha) || !StringUtils.equals(currentCaptcha.toLowerCase(),submitCaptcha.toLowerCase())) {
                return onLoginFailure(this.createToken(request, response), new AccountException("验证码不正确"), request, response);
            }

        }
        String username = getUsername(request);
        String password = getPassword(request);
        SUser user= new SUser();
        user.setUsername(username);
        user= sUserService.selectOneUserByName(user);
        if(user== null){
            return onLoginFailure(this.createToken(request, response), new AccountException("用户名不正确"), request, response);
        }
        Md5Hash md5 = new Md5Hash(password, user.getSalt());
        if(!user.getPassword().equals(md5.toString())){
            return onLoginFailure(this.createToken(request, response), new AccountException("密码错误"), request, response);
        }
        session.setAttribute(Constants.USER_SESSION_TEMP, user);
        try {
            Subject subject = getSubject(request, response);
            subject.login(this.createToken(request, response));
            return super.executeLogin(request, response);
        } catch (AuthenticationException e) {
            //e.printStackTrace();
            return onLoginFailure(this.createToken(request, response), e, request, response);
        }
    }

    /**
     * 登录成功
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        Session session = subject.getSession(false);
        session.removeAttribute(DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE);
        session.removeAttribute(DEFAULT_SHOW_CAPTCHA_KEY_ATTRIBUTE);
        session.setAttribute("sv", subject.getPrincipal());
        logger.info("onLoginSuccess 登录成功....");
        SUser user= (SUser) session.getAttribute(Constants.USER_SESSION_TEMP);
        TreeNode menu= this.resourceService.listQueryTreeResource(user.getUserId());
        List<Map<String, Object>> resources= this.resourceService.listQueryByUserId(user.getUserId());
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;

        UserSession.getInstance(httpServletRequest).setMenu(menu);
        UserSession.getInstance(httpServletRequest).setUser(user);
        session.setAttribute(Constants.RES_RESOURCES, resources);
        return super.onLoginSuccess(token, subject, request, response);
    }

    /**
     * 登录失败
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        Session session = getSubject(request, response).getSession(false);
        Integer number = (Integer) session.getAttribute(DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE);
        //如果失败登录次数大于allowLoginNum时，展示验证码
        if (number > getAllowLoginNum() - 1) {
            session.setAttribute(DEFAULT_SHOW_CAPTCHA_KEY_ATTRIBUTE, true);
            session.setAttribute(DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE, ++number);
        }
        session.setAttribute(DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE, ++number);
        logger.info("登录失败,DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE: {}\tnumber: {}", DEFAULT_LOGIN_NUM_KEY_ATTRIBUTE, number);
        return super.onLoginFailure(token, e, request, response);
    }

    /**
     * 重写父类方法，当登录失败将异常信息设置到request的attribute中
     */
    @Override
    protected void setFailureAttribute(ServletRequest request,AuthenticationException ae) {
        if (ae instanceof IncorrectCredentialsException) {
            request.setAttribute(getFailureKeyAttribute(), "用户名密码不正确!");
        } else {
            request.setAttribute(getFailureKeyAttribute(), ae.getMessage());
        }
    }
    /**
     * 重写父类方法，创建一个自定义的{@link UsernamePasswordTokeExtend}
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String host = getHost(request);

        boolean rememberMe = false;
        String rememberMeValue = request.getParameter(getRememberMeParam());
        Integer rememberMeCookieValue = null;
        //如果提交的rememberMe参数存在值,将rememberMe设置成true
        if(StringUtils.isNotEmpty(rememberMeValue)) {
            rememberMe = true;

            rememberMeCookieValue = NumberUtils.toInt(rememberMeValue);
        }

        return new UsernamePasswordTokeExtend(username, password, rememberMe, host,rememberMeCookieValue);
    }
    /**
     * UsernamePasswordToke扩展，添加一个rememberMeValue字段，获取提交上来的rememberMe值
     * 根据该rememberMe值去设置Cookie的有效时间。
     */
    @SuppressWarnings("serial")
    protected class UsernamePasswordTokeExtend extends UsernamePasswordToken {
        //rememberMe cookie的有效时间
        private Integer rememberMeCookieValue;
        public UsernamePasswordTokeExtend() {
        }

        public UsernamePasswordTokeExtend(String username,String password,boolean rememberMe, String host,Integer rememberMeCookieValue) {
            super(username, password, rememberMe, host);
            this.rememberMeCookieValue = rememberMeCookieValue;
        }
        /**
         * 获取rememberMe cookie的有效时间
         *
         * @return Integer
         */
        public Integer getRememberMeCookieValue() {
            return rememberMeCookieValue;
        }

        /**
         * 设置rememberMe cookie的有效时间
         *
         * @param rememberMeCookieValue cookie的有效时间
         */
        public void setRememberMeCookieValue(Integer rememberMeCookieValue) {
            this.rememberMeCookieValue = rememberMeCookieValue;
        }


    }



    @Autowired
    private ISUserService sUserService;
    @Autowired
    ISResourceService resourceService;

    public Integer getAllowLoginNum() {
        return allowLoginNum;
    }

    public void setAllowLoginNum(Integer allowLoginNum) {
        this.allowLoginNum = allowLoginNum;
    }

}
