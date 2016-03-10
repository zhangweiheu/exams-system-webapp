package com.online.exams.system.webapp.spring;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.io.IOException;

public class JsonpFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {

    private String jsonPrefix = null;

    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = (prefixJson ? "{} && " : null);
    }

    protected void writePrefix(HttpOutputMessage outputMessage, Object object) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (this.jsonPrefix != null) {
            sb.append(jsonPrefix);
        }
        String jsonpFunction =
                (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
        if (jsonpFunction != null) {
            sb.append(jsonpFunction);
            sb.append("(");
        }
        byte[] bytes = sb.toString().getBytes(getCharset());
        outputMessage.getBody().write(bytes);
    }

    protected void writeSuffix(HttpOutputMessage outputMessage, Object object) throws IOException {
        String jsonpFunction =
                (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
        if (jsonpFunction != null) {
            outputMessage.getBody().write(");".getBytes(getCharset()));
        }
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        if (obj instanceof MappingJacksonValue) {
            writePrefix(outputMessage, obj);
            super.writeInternal(((MappingJacksonValue) obj).getValue(), outputMessage);
            writeSuffix(outputMessage, obj);
        } else {
            super.writeInternal(obj, outputMessage);
        }
    }
}
