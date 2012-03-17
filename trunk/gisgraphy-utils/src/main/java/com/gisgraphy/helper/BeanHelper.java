/*******************************************************************************
 * Gisgraphy Project 
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *  
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 *  
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 *  
 *   Copyright 2008  Gisgraphy project 
 * 
 *   David Masclet <davidmasclet@gisgraphy.com>
 ******************************************************************************/
package com.gisgraphy.helper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class BeanHelper {

    private static final long serialVersionUID = 2497822953120680812L;

    private static Logger logger = Logger.getLogger(BeanHelper.class);

    private boolean compareProperty(final Object object1, final Object object2, final String propertyName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	final Object object1PropValue = PropertyUtils.getProperty(object1, propertyName);
	final Object object2PropValue = PropertyUtils.getProperty(object2, propertyName);
	if (object1PropValue == null) {
	    return object2PropValue == null;
	}
	return object1PropValue.equals(object2PropValue);
    }

    public boolean equals(final Object other, final Object current) {
	if (!current.getClass().isAssignableFrom(other.getClass())) {
	    return false;
	}
	final String thisName = current.getClass().getSimpleName();
	final String objectName = other.getClass().getSimpleName();
	String propertyName;
	Exception exception;
	try {
	    for (final PropertyDescriptor thisPropertyDescriptor : Introspector.getBeanInfo(current.getClass(), Object.class).getPropertyDescriptors()) {
		exception = null;
		propertyName = thisPropertyDescriptor.getName();
		logger.debug("propertyName=" + propertyName);
		try {
		    if (!compareProperty(current, other, propertyName)) {
			return false;
		    }
		} catch (final IllegalAccessException e) {
		    exception = e;
		} catch (final InvocationTargetException e) {
		    exception = e;
		} catch (final NoSuchMethodException e) {
		    exception = e;
		}
		if (exception != null) {
		    logger.debug("impossible to compare property " + propertyName + " for beans " + thisName + " and " + objectName, exception);
		    continue;
		}
	    }
	} catch (final IntrospectionException e) {
	    logger.debug("impossible to get properties for bean " + thisName, e);
	}
	return true;
    }

    public static String toString(Object current) {
	if (current == null) {
	    return "null";
	}
	final StringBuffer buffer = new StringBuffer();
	final String beanName = current.getClass().getSimpleName();
	buffer.append(beanName);
	buffer.append(" { ");
	String propertyName;
	Object propertyValue = null;
	boolean first = true;
	Exception exception;
	try {
	    for (final PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(current.getClass(), Object.class).getPropertyDescriptors()) {
		exception = null;
		propertyName = propertyDescriptor.getName();
		logger.debug("propertyName=" + propertyName);
		try {
		    propertyValue = PropertyUtils.getProperty(current, propertyName);
		} catch (final IllegalAccessException e) {
		    exception = e;
		} catch (final InvocationTargetException e) {
		    exception = e;
		} catch (final NoSuchMethodException e) {
		    exception = e;
		}
		if (exception != null) {
		    logger.debug("impossible to get value of property " + propertyName + " of bean " + beanName, exception);
		    continue;
		}
		if (first) {
		    first = false;
		} else {
		    buffer.append(", ");
		}
		buffer.append(propertyName);
		buffer.append(':');
		buffer.append(propertyValue);
	    }
	} catch (final IntrospectionException e) {
	    logger.error("impossible to get properties of bean " + beanName, e);
	}
	buffer.append(" }");
	return buffer.toString();
    }

    public static Object cloneBean(final Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
	return org.apache.commons.beanutils.BeanUtils.cloneBean(bean);
    }

    public static PropertyDescriptor[] getBeanProperties(final Object bean) throws IntrospectionException {
	return Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
    }

    public static Class getBeanPropertyClass(final Object bean, final String propertyName) throws IntrospectionException {
	final PropertyDescriptor[] propDescriptors = BeanHelper.getBeanProperties(bean);
	for (final PropertyDescriptor propDescriptor : propDescriptors) {
	    if (propertyName.equals(propDescriptor.getName())) {
		return propDescriptor.getPropertyType();
	    }
	}
	return null;
    }

}
