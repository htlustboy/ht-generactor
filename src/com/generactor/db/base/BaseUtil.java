package com.generactor.db.base;

/**
 * 基本工具类
 * @author taohu
 *
 */
public class BaseUtil {
	
	/**
	 * 给定字符串是否为null
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if(str==null || str.equals("") || str.length()==0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 数据库表名转java类名
	 * @param tableName
	 * @return
	 */
	public static String tableNameToJava(String tableName) {
		if(isEmpty(tableName)) {
			return "";
		}
		return getTableName(tableName, "_");
	}
	
	/**
	 * 数据库列名转java属性名
	 * @param columnName
	 * @return
	 */
	public static String columnNameToJava(String columnName) {
		if(isEmpty(columnName)) {
			return "";
		}
		return getColumnName(columnName, "_");
	}
	
	/**
	 * getter setter属性名称
	 * @param columnName
	 * @return
	 */
	public static String columnToGetSet(String columnName) {
		if(isEmpty(columnName)) {
			return "";
		}
		return getColumnFunc(columnName,"_");
	}
	
	
	/**
	 * 数据库表名转驼峰
	 * @param tableName
	 * @param regex
	 * @return
	 */
	private static String getTableName(String tableName,String regex) {
		StringBuilder result = new StringBuilder("");
		if(tableName.contains(regex)) {
			String[] tmp = tableName.split(regex);
			if(tmp!=null && tmp.length>1) {
				for(int i=1;i<tmp.length;i++) {
					if(tmp[i].length()<1) {
						continue;
					}
					result.append(tmp[i].substring(0, 1).toUpperCase()+tmp[i].substring(1));
				}
			}
			return result.toString();
		}else {
			return tableName;
		}
	}
	
	/**
	 * 数据库列名转驼峰
	 * @param column
	 * @param regex
	 * @return
	 */
	private static String getColumnName(String column,String regex) {
		StringBuilder result = new StringBuilder("");
		if(column.contains(regex)) {
			String[] tmp = column.split(regex);
			if(tmp!=null && tmp.length>1) {
				for(int i=0;i<tmp.length;i++) {
					if(i==0) {
						result.append(tmp[i]);
						continue;
					}
					result.append(tmp[i].substring(0, 1).toUpperCase()+tmp[i].substring(1));
				}
			}
			return result.toString();
		}else {
			return column;
		}
	}
	
	/**
	 * getter setter名称
	 * @param column
	 * @param regex
	 * @return
	 */
	private static String getColumnFunc(String column,String regex) {
		StringBuilder result = new StringBuilder("");
		if(column.contains(regex)) {
			String[] tmp = column.split(regex);
			if(tmp!=null && tmp.length>1) {
				for(int i=0;i<tmp.length;i++) {
					result.append(tmp[i].substring(0, 1).toUpperCase()+tmp[i].substring(1));
				}
			}
			return result.toString();
		}else {
			return column.substring(0,1).toUpperCase() + column.substring(1);
		}
	}
	
	/**
	 * sql类型转java类型
	 * @param type
	 * @return
	 */
	public static String sqlTypeToJava(String type) {
		String result = "";
		switch (type.toLowerCase()) {
			case "int":
				result = "int";
				break;
			case "bit":
				result = "boolean";
				break;
			case "tinyint":
				result = "boolean";
				break;
			case "varchar":
				result = "String";
				break;
			case "datetime":
				result = "Date";
				break;
			case "date":
				result = "Date";
				break;
			case "timestamp":
				result = "Date";
				break;
			case "double":
				result = "double";
				break;
			case "float":
				result = "float";
				break;
			case "char":
				result = "char";
				break;
			default:
				result = "Object";
				break;
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getColumnName("is_delete_hello_", "_"));
	}

	/**
	 * 判断数组中是否包含某个元素
	 * @param ignoreList
	 * @param name
	 * @return
	 */
	public static boolean hasItem(String[] ignoreList, String name) {
		if(ignoreList==null || ignoreList.length==0) {
			return false;
		}else {
			for(String s : ignoreList) {
				if(s.contentEquals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
