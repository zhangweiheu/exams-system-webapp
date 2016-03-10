/**
 * 
 */
package com.online.exams.system.webapp.spring;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

/**
 * web mvc 项目配置文件类
 *
 * @author tanxianwen 2015年9月25日
 */
public abstract class AbstractWebMvcConfiguration extends WebMvcConfigurerAdapter {



    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 自定义fastjson的转换器
        JsonpFastJsonHttpMessageConverter jsonConverter = new JsonpFastJsonHttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON));
        jsonConverter.setFeatures(SerializerFeature.QuoteFieldNames, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNonStringKeyAsString);
        converters.add(jsonConverter);

        // 自定义字符串转换器
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringConverter.setSupportedMediaTypes(Lists.newArrayList(MediaType.TEXT_HTML));
        stringConverter.setWriteAcceptCharset(false);
        converters.add(stringConverter);
    }

}
