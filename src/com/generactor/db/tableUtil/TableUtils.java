package com.generactor.db.tableUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.generactor.db.imp.DbConnectionImp;
import com.generactor.db.model.TableInfo;

/**
 * 数据库表信息获取
 * @author taohu
 *
 */
public class TableUtils {
	
	public static Connection connection = DbConnectionImp.getInstance().getDBConnection();
	
	/**
	 * 获取所有的表名信息
	 * @return
	 */
	public static List<String> getTableList(){
		List<String> result = new LinkedList<String>();
		if(connection == null) {
			System.out.println("数据库连接信息不正确,无法获取表名信息...");
			return result;
		}
		try {
			java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet rs = databaseMetaData.getTables(null, null, "%", new String[] {"TABLE"});
			while(rs.next()) {
				result.add(rs.getString(3));
			}
		} catch (Exception e) {
			System.out.println("获取信息发生异常:"+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取列的信息
	 * @return
	 */
	public static List<TableInfo> getColumnsInfo(String tableName){
		List<TableInfo> tableInfos = new LinkedList<TableInfo>(); 
		List<String> tableNames = getTableList();
		if(!tableNames.contains(tableName)) {
			System.out.println("表名不存在！");
			return tableInfos;
		}
		try {
			DatabaseMetaData data = connection.getMetaData();
			ResultSet resultSet = data.getColumns(null, "%", tableName, "%");
			while(resultSet.next()) {
				TableInfo persistent = new TableInfo();
				persistent.setName(resultSet.getString("COLUMN_NAME"));
				persistent.setType(resultSet.getString("TYPE_NAME"));
				persistent.setDesc(resultSet.getString("REMARKS"));
				//默认第一项为主键
				if(tableInfos.size()==0) {
					persistent.setPrimary(Boolean.TRUE);
				}
				tableInfos.add(persistent);
			}
		} catch (Exception e) {
			System.out.println(String.format("解析%s发生错误：", tableName)+e.getMessage());
			e.printStackTrace();
		}
		return tableInfos;
	}
	
	public static void main(String[] args) {

	}
}
