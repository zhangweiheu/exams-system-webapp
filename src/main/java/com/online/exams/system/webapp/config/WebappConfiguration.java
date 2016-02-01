package com.online.exams.system.webapp.config;

import com.online.exams.system.core.config.CoreApplication;
import com.online.exams.system.passport.config.PassportApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by zhangwei on 16/1/15.
 */
@Configuration
@EnableAutoConfiguration
@Import({CoreApplication.class, PassportApplication.class})
@ComponentScan(basePackageClasses = {com.online.exams.system.webapp.controller.Pkg.class})
public class WebappConfiguration {
}

