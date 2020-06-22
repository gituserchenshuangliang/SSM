package com.ssm.web;

import com.ssm.config.RedisConfig;
import com.ssm.config.RootConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * @description:通过注解初始化Spring MVC替代xml文件
 * @author: Cherry
 * @time: 2020/6/12 9:21
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    //配置Spring IoC容器，加载各类Spring Beaan
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class, RedisConfig.class};
    }

    //DispatcherServlet的URI映射关系配置，生成web请求上下文
    @Override
    protected Class<?>[] getServletConfigClasses() {
        //可返回的Java配置文件
        return new Class<?>[]{DispatcherServlet.class};
    }

    //DispatcherServlet拦截的内容
    @Override
    protected String[] getServletMappings() {
        return new String[]{"*.do"};
    }

    //配置过滤器
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};
    }
}
