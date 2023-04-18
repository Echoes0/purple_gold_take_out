package com.itl.purple_gold.filter;

import com.alibaba.fastjson.JSON;
import com.itl.purple_gold.common.BaseContext;
import com.itl.purple_gold.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        
        //请求的Url
        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}",requestURI);


        //不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //请求是否需要处理
        boolean check = check(urls, requestURI);

        //无需处理，直接放行
        if (check) {
            log.info("本次请求{}无需处理",requestURI);
            //请求放行
            filterChain.doFilter(request,response);
            return;
        }

        //已登录则直接放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户{}已登录",request.getSession().getAttribute("employee"));

            //将登录用户的id存入当前线程
            Long emId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(emId);

            filterChain.doFilter(request,response);
            return;
        }

        //移动端-已登录则直接放行
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户{}已登录",request.getSession().getAttribute("user"));
            //将登录用户的id存入当前线程
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        //未登录则返回未登录结果，通过输出流方式向客户端页面返回响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("用户未登录访问！");
    }

    /**
     * 路径匹配，检查本次请求是否放行
     * @param urls
     * @param requestURL
     * @return
     */
    public boolean check(String[] urls, String requestURL){
        for (String url:urls){
            boolean match=PATH_MATCHER.match(url,requestURL);
            if (match){
                return true;
            }
        }
        return false;
    }
}
