package com.generactor.db.imp;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.generactor.db.inter.AbstractDbBase;

public class DbConnectionImp extends AbstractDbBase{
	
	private static Properties properties;
	private static DbConnectionImp instance = null;
	
	private DbConnectionImp() {  }
	
	static {
		if(properties==null) {
			properties = new Properties();
		}
	}
	
	public static DbConnectionImp getInstance() {
		if(instance == null) {
			synchronized (DbConnectionImp.class) {
				if(instance==null) {
					instance = new DbConnectionImp();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 初始化properties文件
	 * @return
	 */
	public static Properties initData() {
		InputStream in = Object.class.getResourceAsStream("/jdbc.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return properties;
	}
	
	/**
	 * 获取数据库连接
	 */
	@Override
	public Connection getDBConnection() {
		initData();
		Connection connection = null;
		try {
			String driver = properties.getProperty("jdbc.driver");
			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			System.out.println("数据库连接失败："+e.getMessage());
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 关闭数据库
	 * @param connection
	 */
	public void destory(Connection connection) {
		if(connection!=null) {
			try {
				connection.close();
				System.out.println("数据库关闭成功!");
			} catch (SQLException e) {
				System.out.println("数据库关闭失败:"+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Connection connection = DbConnectionImp.getInstance().getDBConnection();
		System.out.println(connection);
	}
	
}
