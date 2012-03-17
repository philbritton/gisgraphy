/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisgraphy.hibernate.projection.ProjectionBean;

/**
 * Inspect object and all subClass to retrieve the fields in order to be
 * used by the {@link ProjectionBean}
 * 
 * @see IntrospectionIgnoredField
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class IntrospectionHelper {
    
    protected static final Logger logger = LoggerFactory
    .getLogger(IntrospectionHelper.class);

    private static Map<Class<?>, String[]> cacheArray = new HashMap<Class<?>, String[]>();

    private static Map<Class<?>, List<String>> cacheList = new HashMap<Class<?>, List<String>>();

    /**
     * clear the cache of introspected class
     */
    public static void clearCache() {
	cacheArray.clear();
	cacheList.clear();
    }

    /**
     * @param clazz
     *                the class to inspect
     * @return a list of all private fields of the specified class that are not :
     *         <ul>
     *         <li>Of type List</li>
     *         <li>Annoted with {@link IntrospectionIgnoredField}</li>
     *         <li>Synthetic</li>
     *         </ul>
     *         The result of the method will be cached
     * 
     * see http://java.sun.com/j2se/1.5.0/docs/api/java/lang/reflect/Field.html
     */
    public static List<String> getFieldsAsList(
	    Class<?> clazz) {
	List<String> cached = cacheList.get(clazz);
	if (cached == null) {
	    updateCache(clazz);
	    return cacheList.get(clazz);
	} else {
	    return cached;
	}
    }

    /**
     * @param clazz
     *                the class to inspect
     * @return an Array of all private fields of the specified class that are
     *         not :
     *         <ul>
     *         <li>Of type List</li>
     *         <li>Annoted with {@link IntrospectionIgnoredField}</li>
     *         <li>Synthetic</li>
     *         <li>final</li>
     *         </ul>
     *         The result of the method will be cached
     * 
     * see http://java.sun.com/j2se/1.5.0/docs/api/java/lang/reflect/Field.html
     */
    public static String[] getFieldsAsArray(
	    Class<?> clazz) {
	String[] cached = cacheArray.get(clazz);
	if (cached == null) {
	    updateCache(clazz);
	    return cacheArray.get(clazz);
	} else {
	    return cached;
	}
    }

    private static void updateCache(Class<?> clazz) {
	Class<?> clazzParent = clazz;
	List<String> introspectedFields = new ArrayList<String>();
	try {
	    do {
		int searchMods = 0x0;
		searchMods |= modifierFromString("private");

		Field[] flds = clazzParent.getDeclaredFields();
		for (Field f : flds) {
		    int foundMods = f.getModifiers();
		    if ((foundMods & searchMods) == searchMods
			    && !f.isSynthetic() && f.getType() != List.class
			    && !isIgnoreField(f) && !Modifier.isFinal(foundMods)) {
			introspectedFields.add(f.getName());
		    }
		}
		clazzParent = (Class<?>) clazzParent.getSuperclass();
	    } while (clazzParent != Object.class);

	} catch (RuntimeException x) {
	  logger.error("can not update introspection cache :"+x.getMessage(),x);
	}
	cacheList.put(clazz, introspectedFields);
	cacheArray.put(clazz, introspectedFields.toArray(new String[] {}));
    }

    private static boolean isIgnoreField(Field field) {
	for (int i = 0; i < field.getDeclaredAnnotations().length; i++) {
	    if (field.getDeclaredAnnotations()[i] instanceof IntrospectionIgnoredField) {
		return true;
	    }
	}
	return false;
    }

    private static int modifierFromString(String s) {
	int m = 0x0;
	if ("public".equals(s))
	    m |= Modifier.PUBLIC;
	else if ("protected".equals(s))
	    m |= Modifier.PROTECTED;
	else if ("private".equals(s))
	    m |= Modifier.PRIVATE;
	else if ("static".equals(s))
	    m |= Modifier.STATIC;
	else if ("final".equals(s))
	    m |= Modifier.FINAL;
	else if ("transient".equals(s))
	    m |= Modifier.TRANSIENT;
	else if ("volatile".equals(s))
	    m |= Modifier.VOLATILE;
	return m;
    }
}
