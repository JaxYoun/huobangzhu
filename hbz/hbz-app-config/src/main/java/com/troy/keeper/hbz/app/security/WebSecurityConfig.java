package com.troy.keeper.hbz.app.security;

/**
 * Created by leecheng on 2017/10/11.
 */

import com.troy.keeper.hbz.app.web.AuthCodeFilter;
import com.troy.keeper.hbz.app.web.CheckUserFilter;
import com.troy.keeper.hbz.app.web.CrossWrapperFilter;
import com.troy.keeper.hbz.sys.ExtSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
@EnableSpringHttpSession
@EnableWebSecurity
@Order(99)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Autowired
    private SecurityAuthencationFilter securityAuthencationFilter;

    @Autowired
    private CheckUserFilter checkUserFilter;

    @Autowired
    CrossWrapperFilter crossWrapperFilter;

    @Value("${session.header}")
    private String sessionHeaderName;

    @Value("${debug}")
    private Boolean debug;

    @Autowired
    private SecurityLoginFailureHandler securityLoginFailureHandler;

    @Autowired
    private SecurityAccessDeniedHandler securityAccessDeniedHandler;

    @Autowired
    private SecurityLogoutSuceessHandler securityLogoutSuceessHandler;

    @Autowired
    private SecurityRequestFilter securityRequestFilter;

    @Autowired
    private HbzAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthCodeFilter authCodeFilter;

    @Autowired
    private SecurityLoginSuccessHandler securityLoginSuccessHandler;

    @Value("${session.strategy}")
    private String sessionStrategy;

    @Value("${security.parameter.user}")
    private String securityParameterUser;

    @Value("${security.parameter.pass}")
    private String securityParameterPass;

    @Value("${security.url.login}")
    private String securityUrlLogin;

    @Value("${security.url.home}")
    private String securityUrlHome;

    @Value("${security.url.auths}")
    private String securityUrlAuths;

    @Value("${security.url.logout}")
    private String securityUrlLogout;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (debug) {
            http.authorizeRequests().antMatchers("/debug").permitAll();
        }
        http.headers().frameOptions().disable();

        http.exceptionHandling().accessDeniedHandler(securityAccessDeniedHandler);              //未授权
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);            //认证体
        http.authorizeRequests().antMatchers("/api/user/app/reset").permitAll();    //重置密码
        http.authorizeRequests().antMatchers("/api/upload/**").permitAll();  //文件上传下载
        http.authorizeRequests().antMatchers("/api/web/banner/**").permitAll();  //获取banner
        http.authorizeRequests().antMatchers("/api/web/appVersion/**").permitAll();  //获取app版本检测更新
        http.authorizeRequests().antMatchers("/api/web/news/**").permitAll();  //web端首页获取咨询
        http.authorizeRequests().antMatchers("/api/web/agreement/**").permitAll();  //合同加载 test
        http.authorizeRequests().antMatchers("/api/web/aliPagePay/**").permitAll();  //网页支付 test
        http.authorizeRequests().antMatchers("/api/web/pay/**").permitAll();  //网页支付 test
        http.authorizeRequests().antMatchers("/api/web/driverLine/export/**").permitAll();  //批量导出 测试 yang
        http.authorizeRequests().antMatchers("/api/web/export/**").permitAll();  //批量导出 统一接口 测试 yang
        http.authorizeRequests().antMatchers("/api/web/user/getUserRoleMenu").permitAll();  //web端获取菜单，兼容未登录用户yang
        http.authorizeRequests().antMatchers("/api/user/app/telephone/check").permitAll();  //短信认证码
        http.authorizeRequests().antMatchers("/api/user/app/authCode/check").permitAll();   //图形认证码
        http.authorizeRequests().antMatchers("/api/session/token").permitAll();     //Token
        http.authorizeRequests().regexMatchers("/login").permitAll();               //登录
        http.authorizeRequests().antMatchers("/api/public/enums").permitAll();       //类型查询
        http.authorizeRequests().antMatchers("/api/user/app/registry").permitAll(); //注册
        http.authorizeRequests().antMatchers("/api/user/authCode/*").permitAll();   //认证码发送
        http.authorizeRequests().regexMatchers("/favicon.*").permitAll();          //图标
        http.authorizeRequests().regexMatchers("/api/user/isAuthencated").permitAll();  //是否认证
        http.authorizeRequests().antMatchers("/api/alipay/callback").permitAll();   //支付宝支付回调
        http
                .addFilterBefore(securityAuthencationFilter, FilterSecurityInterceptor.class)
                .addFilterBefore(crossWrapperFilter, LogoutFilter.class)
                .addFilterBefore(securityRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(checkUserFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().anyRequest().authenticated().and()

                .formLogin().failureHandler(securityLoginFailureHandler)
                .usernameParameter(securityParameterUser)
                .passwordParameter(securityParameterPass)
                .loginProcessingUrl(securityUrlAuths)
                .successHandler(securityLoginSuccessHandler)
                .permitAll()
                .and()

                .logout().logoutUrl(securityUrlLogout)
                .invalidateHttpSession(true)
                .logoutSuccessHandler(securityLogoutSuceessHandler)
                .and()

                .rememberMe()
                .tokenValiditySeconds(1209600).
                and().

                csrf().disable().
                cors().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.eraseCredentials(false);
    }

    @Bean
    CheckUserFilter checkUserFilter() {
        return new CheckUserFilter();
    }

    @Bean
    public AuthCodeFilter cAuthCodeFilter() {
        return new AuthCodeFilter();
    }

    @Bean
    public CrossWrapperFilter createwerewrFilter() {
        return new CrossWrapperFilter();
    }

    @Bean
    public SecurityRequestFilter createSecurityRequestFilter() {
        return new SecurityRequestFilter();
    }

    @Bean
    public SecurityAccessDeniedHandler createSecurityAccessDeniedHandler() {
        return new SecurityAccessDeniedHandler();
    }

    @Bean
    public HbzAuthenticationEntryPoint createHbzAuthenticationEntryPoint() {
        return new HbzAuthenticationEntryPoint();
    }

    @Bean
    public SecurityLoginSuccessHandler createSecurityLoginSuccessHandler() {
        return new SecurityLoginSuccessHandler();
    }

    @Bean
    public SecurityLoginFailureHandler createSecurityLoginFailureHandler() {
        return new SecurityLoginFailureHandler();
    }

    @Bean
    public SecurityLogoutSuceessHandler createSecurityLogoutSuceessHandler() {
        return new SecurityLogoutSuceessHandler();
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new ExtSessionRepository();
    }

    @Bean
    public HttpSessionStrategy pushHttpSessionStrategy() throws Exception {
        switch (sessionStrategy) {
            case "cookie": {
                return new CookieHttpSessionStrategy();
            }
            case "header": {
                HeaderHttpSessionStrategy hhss = new HeaderHttpSessionStrategy();
                hhss.setHeaderName(sessionHeaderName);
                return hhss;
            }
            default: {
                throw new Exception("无法实现session方案");
            }
        }
    }

}