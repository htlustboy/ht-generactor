package com.generactor.db.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;

import com.generactor.db.base.BaseUtil;
import com.generactor.db.model.TableInfo;

public class UploadUtil {
	
	private static Properties properties = new Properties();
	
	private static final String RN = "\r\n";
	private static final String SPACE = "	";
	
	private static String path = "";
	
	private static String basePackage = "";
	private static String modelPackage = "";
	private static String servicePackage = "";
	private static String controllerPackage = "";
	private static String mapperPackage = "";
	
	private static String superModel = "";
	private static String superController = "";
	private static String superService = "";
	private static String superMapper = "";
	
	private static String ignoreColumns = "";
	
	static {
		InputStream in = UploadUtil.class.getResourceAsStream("/config.properties");
		try {
			properties.load(in);
			
			path = properties.getProperty("base.path");
			
			basePackage = properties.getProperty("basepackage.name");
			modelPackage = basePackage + "." + properties.getProperty("basepackage.model");
			servicePackage = basePackage + "." + properties.getProperty("basepackage.service");
			controllerPackage = basePackage + "." + properties.getProperty("basepackage.controller");
			mapperPackage = basePackage + "." + properties.getProperty("basepackage.mapper");
			
			superModel = properties.getProperty("super.model");
			superController = properties.getProperty("super.controller");
			superService = properties.getProperty("super.service");
			superMapper = properties.getProperty("super.BaseMapper");
			
			ignoreColumns = properties.getProperty("ignore.bean.columns");
			
		} catch (IOException e) {
			System.out.println("config.properties文件加载失败！"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 生成java文件
	 * @param tableName
	 * @param infos
	 */
	public static void uploadBean(String tableName,List<TableInfo> infos) {
		String beanName = BaseUtil.tableNameToJava(tableName);
		StringBuilder sb = new StringBuilder("");
		
		//判断有没有忽略的字段
		if(!BaseUtil.isEmpty(ignoreColumns)) {
			String[] ignoreList = ignoreColumns.split(",");
			for(int i=0;i<infos.size();i++) {
				if(BaseUtil.hasItem(ignoreList,infos.get(i).getName())) {
					infos.remove(infos.get(i));
					i--;
				}
			}
		}
		
		//包名
		sb.append("package ")
		  .append(modelPackage)
		  .append(RN)
		  .append(RN);
		
		sb.append("public class ").append(beanName);
		if(!BaseUtil.isEmpty(superModel)) {
			sb.append(" extends " + superModel);
		}
		sb.append(" {").append(RN);
		
		//插入属性名称
		for(TableInfo info : infos) {
			sb.append(RN).append(SPACE)
			  .append("// ")
			  .append(info.getDesc())
			  .append(RN).append(SPACE)
			  .append("private ")
			  .append(BaseUtil.sqlTypeToJava(info.getType()) )
			  .append(" ")
			  .append(BaseUtil.columnNameToJava(info.getName()))
			  .append(";")
			  .append(RN);
		}
		
		//插入getter setter方法
		for (TableInfo info : infos) {
			//getter
			sb.append(RN).append(SPACE)
			  .append("public ")
			  .append(BaseUtil.sqlTypeToJava(info.getType()))
			  .append(" ")
			  .append("get")
			  .append(BaseUtil.columnToGetSet(info.getName()))
			  .append("(){")
			  .append(RN).append(SPACE).append(SPACE)
			  .append("return this.")
			  .append(BaseUtil.columnNameToJava(info.getName()))
			  .append(";").append(RN).append(SPACE)
			  .append("}")
			  .append(RN).append(SPACE);
			
			//setter
			sb.append(RN).append(SPACE)
			  .append("public void set")
			  .append(BaseUtil.columnToGetSet(info.getName()))
			  .append("(")
			  .append(BaseUtil.sqlTypeToJava(info.getType()))
			  .append(" ")
			  .append(BaseUtil.columnNameToJava(info.getName()))
			  .append("){")
			  .append(RN).append(SPACE).append(SPACE)
			  .append("this.")
			  .append(BaseUtil.columnNameToJava(info.getName()))
			  .append(" = ")
			  .append(BaseUtil.columnNameToJava(info.getName()))
			  .append(RN).append(SPACE)
			  .append("}").append(RN);
			  
		}
		
		sb.append("}").append(RN);
		System.out.println(String.format("开始导出%s.java文件：======", BaseUtil.tableNameToJava(tableName)));
		
		try {
			File file = new File(path + tableName);
			if(!file.exists()) {
				file.mkdirs();
			}
			OutputStream out = new FileOutputStream(path+tableName+"/"+BaseUtil.tableNameToJava(tableName)+".java");
			PrintStream p = new PrintStream(out);
			p.print(sb.toString());
			p.close();
			out.close();
			System.out.println(String.format("%s文件导出成功：======", BaseUtil.tableNameToJava(tableName)));
			
		} catch (Exception e) {
			System.out.println("javaBean写出异常："+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成service文件
	 * @param tableName
	 * @param infos
	 */
	public static void uploadService(String tableName,List<TableInfo> infos) {
		
		String serviceName = BaseUtil.tableNameToJava(tableName) + "Service";
		StringBuilder sb = new StringBuilder("");
		
		//包名
		sb.append("package ")
		  .append(servicePackage)
		  .append(RN)
		  .append(RN);
		
		//主体程序
		sb.append("@Service")
		  .append(RN)
		  .append("public class ")
		  .append(serviceName);
		  if(!BaseUtil.isEmpty(superService)) {
			sb.append(" extends " + superService);
		  }
		  sb.append("{")
		    .append(RN)
		    .append("}");
		  
		  System.out.println(String.format("开始导出%s.java文件：======", serviceName));
		  //导出
		  try {
				File file = new File(path + tableName);
				if(!file.exists()) {
					file.mkdirs();
				}
				OutputStream out = new FileOutputStream(path+tableName+"/"+serviceName+".java");
				PrintStream p = new PrintStream(out);
				p.print(sb.toString());
				p.close();
				out.close();
				System.out.println(String.format("%s文件导出完成", serviceName));
				
			} catch (Exception e) {
				System.out.println("javaService写出异常："+e.getMessage());
				e.printStackTrace();
			}
		}
	
	/**
	 * 导出Controller
	 * @param tableName
	 * @param infos
	 */
	public static void uploadController(String tableName,List<TableInfo> infos) {
		
		String controllerName = BaseUtil.tableNameToJava(tableName) + "Controller";
		StringBuilder sb = new StringBuilder("");
		
		//包名
		sb.append("package ")
		  .append(controllerPackage)
		  .append(RN)
		  .append(RN);
		
		//主体程序
		sb.append("@Controller")
		  .append(RN)
		  .append("public class ")
		  .append(controllerName);
		  if(!BaseUtil.isEmpty(superController)) {
			sb.append(" extends " + superController);
		  }
		  sb.append("{")
		    .append(RN)
		    .append("}");
		  
		  System.out.println(String.format("开始导出%s.java文件：======", controllerName));
		  //导出
		  try {
				File file = new File(path + tableName);
				if(!file.exists()) {
					file.mkdirs();
				}
				OutputStream out = new FileOutputStream(path+tableName+"/"+controllerName+".java");
				PrintStream p = new PrintStream(out);
				p.print(sb.toString());
				p.close();
				out.close();
				System.out.println(String.format("%s文件导出完成", controllerName));
				
			} catch (Exception e) {
				System.out.println("javaController写出异常："+e.getMessage());
				e.printStackTrace();
			}
		}
	
	/**
	 * 导出mapper
	 * @param tableName
	 * @param infos
	 */
	public static void uploadMapper(String tableName,List<TableInfo> infos) {
		
		String mapperName = BaseUtil.tableNameToJava(tableName) + "Mapper";
		StringBuilder sb = new StringBuilder("");
		
		//包名
		sb.append("package ")
		  .append(mapperPackage)
		  .append(RN)
		  .append(RN);
		
		//主体程序
		sb.append("@Repository")
		  .append(RN)
		  .append("public interface ")
		  .append(mapperName);
		  if(!BaseUtil.isEmpty(superMapper)) {
			sb.append(" extends " + superMapper);
		  }
		  sb.append("{")
		    .append(RN)
		    .append("}");
		  
		  System.out.println(String.format("开始导出%s.java文件：======", mapperName));
		  //导出
		  try {
				File file = new File(path + tableName);
				if(!file.exists()) {
					file.mkdirs();
				}
				OutputStream out = new FileOutputStream(path+tableName+"/"+mapperName+".java");
				PrintStream p = new PrintStream(out);
				p.print(sb.toString());
				p.close();
				out.close();
				System.out.println(String.format("%s文件导出完成", mapperName));
				
			} catch (Exception e) {
				System.out.println("mapper写出异常："+e.getMessage());
				e.printStackTrace();
			}
		}
	
	/**
	 * 生成mapper.xml文件
	 * @param tableName
	 * @param infos
	 */
	public static void uploadXmlFile(String tableName,List<TableInfo> infos) {
		
		String beanName = BaseUtil.tableNameToJava(tableName);
		String mapperName = beanName + "Mapper";
		StringBuilder sb = new StringBuilder("");
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		  .append(RN)
		  .append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >")
		  .append(RN)
		  .append(String.format("<mapper namespace=\"%s\" >", mapperPackage+"."+mapperName))
		  .append(RN)
		  .append(SPACE)
		  .append(String.format("<resultMap type=\"%s\" id=\"%s\" />", modelPackage+"."+beanName,mapperName))
		  .append(RN)
		  .append(SPACE);
		
		for(TableInfo info : infos) {
			sb.append(String.format("<result column=\"%s\" property=\"%s\" jdbcType=\"%s\" />", info.getName(),BaseUtil.columnNameToJava(info.getName()),info.getType()))
			  .append(RN)
			  .append(SPACE);
		}
		sb.append(RN);
		sb.append("</resultMap>");
		sb.append(RN);
		sb.append("</mapper>");
		 
		System.out.println(String.format("开始导出%s.xml文件：======", mapperName));
	   //导出
	   try {
			File file = new File(path + tableName);
			if(!file.exists()) {
				file.mkdirs();
			}
			OutputStream out = new FileOutputStream(path+tableName+"/"+mapperName+".xml");
			PrintStream p = new PrintStream(out);
			p.print(sb.toString());
			p.close();
			out.close();
			System.out.println(String.format("%s文件导出完成", mapperName));
			
		} catch (Exception e) {
			System.out.println("xml写出异常："+e.getMessage());
			e.printStackTrace();
		}
		
	}
}
