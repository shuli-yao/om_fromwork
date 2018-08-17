package com.business.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName SystemLnterceptor
 * @Description 拦截器
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/16 下午3:02
 */
//@Configuration
public class SystemLnterceptor extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("进入拦截器");
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
