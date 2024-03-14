package com.cognicx.AppointmentRemainder.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

public class CommonUtil {
	
	public static String nullRemove(String value) {
		return value != null ? value:"";
	}
	
	public static String nullRemove(Object value) {
		return value != null ? value.toString():"";
	}
	
	public static String nullRemoveWithTrim(String value) {
		return value != null ? value.trim():"";
	}
	
	
	public static void copyProperties(Object source, Object target) {
		 BeanUtils.copyProperties(source, target);
	}
	
	public static boolean nullCheckBigInt(Object value) {
		boolean flag = false;
		if (value != null) {
			flag = true;
		}
		return flag;
	}
	
	public static void getMapResultObj(List<Object[]> resultObjList, Map<String, String> courAgencyNameMap) {
		if (resultObjList != null && !resultObjList.isEmpty()) {
			for (Object[] courAgencyName : resultObjList) {
				courAgencyNameMap.put(courAgencyName[0].toString(), courAgencyName[1].toString());
			}
		}
	}

	public static int nullRemoveInt(Object value) {
		return value != null ? (int) value : 0;
	}

}
