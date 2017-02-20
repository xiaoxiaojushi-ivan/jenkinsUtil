package com.ndpmedia.comm.sql;

import com.ndpmedia.comm.config.ConfigUtil;


public class DAOFactory {
	public static Object getInstance(String type) {
		Object obj = null;
		String className = ConfigUtil.map.get(type);
		try {
			obj = Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
