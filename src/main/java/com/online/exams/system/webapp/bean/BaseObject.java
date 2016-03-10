package com.online.exams.system.webapp.bean;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vincent on 2015/6/8.
 * brief desc : 基础类
 */
public abstract class BaseObject implements Serializable {

    private Map<String, Object> properties = null;

    public void setProperties(String key, Object value) {
        if (this.properties == null) {
            this.properties = new HashMap<String, Object>();
        }
        this.properties.put(key, value);
    }

    public Object getProperties(String key) {
        if (this.properties != null) {
            return this.properties.get(key);
        } else {
            return null;
        }
    }

    public void setProperties(Map properties) {
        if (this.properties == null) {
            this.properties = new HashMap<String, Object>();
        }
        this.properties.putAll(properties);
    }

    public Map<String, Object> getProperties() {
        if (this.properties == null) {
            this.properties = new HashMap<String, Object>();
        }
        return properties;
    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Returns a multi-line String with key=value pairs.
     *
     * @return a String representation of this class.
     */
    public String toString() {
        return super.toString();
    }

    /**
     * 将bean打成一个xml结构，方便调试
     *
     * @return
     */
    public String asXml() {
        Class claz = this.getClass();
        Field[] propers = claz.getDeclaredFields();
        StringBuffer sb = new StringBuffer("<bean-context name=\"" + claz.getName() + "\">\r\n");
        for (int i = 0; i < propers.length; i++) {
            Field proper = propers[i];
            String fName = proper.getName();
            sb.append("   <" + fName + ">");
            String mtdName = "get" + fName.substring(0, 1).toUpperCase() + fName.substring(1);
            try {
                Method mtd = claz.getMethod(mtdName);
                sb.append(mtd.invoke(this));
            } catch (Exception e) {
                sb.append("undefined");
            }
            sb.append("</" + proper.getName() + ">\r\n");
        }
        sb.append("</bean-context>");
        return sb.toString();
    }
}
