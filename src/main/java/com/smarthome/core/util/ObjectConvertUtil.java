package com.smarthome.core.util;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * 对象转换成其他对象的转换类
 * @author RM
 *
 */
public class ObjectConvertUtil {
	
	 /**
	  * 将java对象转换成Map对象
	  * @param obj  java对象
	  * @return
	  */
	 public static Map<String, Object> beanToMap(Object obj) { 
         Map<String, Object> params = new HashMap<String, Object>(0); 
         try { 
             PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean(); 
             PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj); 
             for (int i = 0; i < descriptors.length; i++) { 
                 String name = descriptors[i].getName(); 
                 if (!"class".equals(name)) { 
                     params.put(name, propertyUtilsBean.getNestedProperty(obj, name)); 
                 } 
             } 
         } catch (Exception e) { 
             e.printStackTrace(); 
         } 
         return params; 
 }
}
