package com.online.exams.system.webapp.config;

import com.online.exams.system.core.config.CoreApplication;
import com.online.exams.system.webapp.Interceptor.ExceptionInterceptor;
import com.online.exams.system.webapp.Interceptor.LoginCheckInterceptor;
import com.online.exams.system.webapp.Interceptor.PrivilegeInterceptor;
import com.online.exams.system.webapp.spring.AbstractWebMvcConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import java.util.Properties;

/**
 * Created by zhangwei on 16/1/15.
 */
@Configuration
@EnableAutoConfiguration
@Import({CoreApplication.class})
@ComponentScan(basePackageClasses = {com.online.exams.system.webapp.controller.Pkg.class})
public class WebappConfiguration extends AbstractWebMvcConfiguration {

    @Configuration
    public static class CustomConversionService extends DelegatingWebMvcConfiguration {
        @Override
        public FormattingConversionService mvcConversionService() {
            // Use the DefaultFormattingConversionService but do not register defaults
            DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

            // Ensure @NumberFormat is still supported
            conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

            // DateTimeFormatterFactoryBean t = new DateTimeFormatterFactoryBean();
            // t.setPattern("yyyy-MM-dd HH:mm:ss");
            // JodaTimeFormatterRegistrar registrar = new JodaTimeFormatterRegistrar();
            // registrar.setDateTimeFormatter(t.getObject());
            // registrar.setDateFormatter(t.getObject());
            // registrar.setTimeFormatter(t.getObject());
            // registrar.registerFormatters(conversionService);

            DateFormatterRegistrar registrar = new DateFormatterRegistrar();
            registrar.setFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
            registrar.registerFormatters(conversionService);

            return conversionService;
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(new LocaleInterceptor());
        // registry.addInterceptor(new
        // ThemeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
        // registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/secure/*");
        registry.addInterceptor(loginCheckInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(privilegeInterceptor()).addPathPatterns("/**");
    }

    // 添加方法参数验证器
    @Bean
    public MethodValidationPostProcessor configMethodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public LoginCheckInterceptor loginCheckInterceptor() {
        return new LoginCheckInterceptor();
    }

    @Bean
    public PrivilegeInterceptor privilegeInterceptor() {
        return new PrivilegeInterceptor();
    }

    @Bean
    public ExceptionInterceptor exceptionInterceptor(){
        return new ExceptionInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/asset/**").addResourceLocations("classpath:/static/asset/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(velocityViewResolver());
    }

    @Bean
    public VelocityViewResolver velocityViewResolver() {
        VelocityViewResolver velocityViewResolver = new VelocityViewResolver();
        velocityViewResolver.setCache(false);
        velocityViewResolver.setOrder(10);
        velocityViewResolver.setSuffix(".vm");
        velocityViewResolver.setContentType("text/html;charset=UTF-8");
        return velocityViewResolver;
    }

    @Bean
    public VelocityConfigurer velocityConfigurer() {
        VelocityConfigurer configurer = new VelocityConfigurer();
        configurer.setResourceLoaderPath("classpath:/template/");
        Properties properties = new Properties();
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        configurer.setVelocityProperties(properties);
        return configurer;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        return commonsMultipartResolver;
    }
}

