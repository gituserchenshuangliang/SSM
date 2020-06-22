package com.ssm.config;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.annotation.MultipartConfig;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @description:Java配置文件
 * @author: Cherry
 * @time: 2020/6/12 9:32
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.ssm.*")
@PropertySource(value = {"classpath:config/db.properties"})
@MapperScan(basePackages = "com.ssm.dao",annotationClass = Repository.class)
@MultipartConfig(location = "E:/DownloadDemo",maxFileSize = 10*1024*1024,maxRequestSize = 5*1024*1024,fileSizeThreshold = 0)
public class RootConfig extends WebMvcConfigurationSupport {
    DataSource dataSource;

    @Value("${jdbc.database.driver}")
    private String driver = null;

    @Value("${jdbc.database.url}")
    private String url = null;

    @Value("${jdbc.database.username}")
    private String username = null;

    @Value("${jdbc.database.password}")
    private String password = null;

    @Bean
    public DataSource getDataSource() {
        if(dataSource != null) {return dataSource;}

        Properties props = new Properties();
        props.setProperty("driverClassName", driver);
        props.setProperty("url", url);
        props.setProperty("username", username);
        props.setProperty("password", password);
        try {
            dataSource = BasicDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean initSqlSessionFactoryBean(){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        PathMatchingResourcePatternResolver path = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setConfigLocation(path.getResource("config/mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(path.getResource("mapper/RedPacket.xml"),path.getResource("mapper/UserRedPacket.xml"));
        return sqlSessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager initTransaction(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(getDataSource());
        return dataSourceTransactionManager;
    }

    //创建视图解析器
    @Bean
    public ViewResolver initViewResolver(){
        InternalResourceViewResolver view = new InternalResourceViewResolver();
        view.setViewClass(JstlView.class);
        view.setPrefix("/jsp/");
        view.setSuffix(".jsp");
        return view;
    }

    /**
     * 静态资源处理
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/html/**","/js/**","/jsp/**")
                .addResourceLocations("/html/","/js/","/jsp/");
    }
    @Override
    protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
