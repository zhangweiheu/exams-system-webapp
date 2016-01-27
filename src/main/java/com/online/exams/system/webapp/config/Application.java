package com.online.exams.system.webapp.config;

import com.online.exams.system.core.config.CoreApplication;
import com.online.exams.system.passport.config.PassportApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import java.util.Properties;

/**
 * Created by zhangwei on 16/1/15.
 */
@SpringBootApplication
@Import({CoreApplication.class, PassportApplication.class})
@ComponentScan(basePackageClasses = {com.online.exams.system.webapp.controller.Pkg.class})
public class Application extends SpringBootServletInitializer {
    @Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

/*    @Configuration
    public class viewresolver extends WebMvcConfigurerAdapter{
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
            configurer.setResourceLoaderPath("/WEB-INF/views/");
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
    }*/
}

