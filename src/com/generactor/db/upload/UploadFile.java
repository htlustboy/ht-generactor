package com.generactor.db.upload;

import java.util.List;


import com.generactor.db.model.TableInfo;
import com.generactor.db.tableUtil.TableUtils;

/**
 * 生成文件的主方法
 * @author taohu
 *
 */
public class UploadFile {
	
	public static void upload(String tableName) {
		
		List<TableInfo> infos = TableUtils.getColumnsInfo(tableName);
		if(infos==null || infos.size()==0) {
			System.out.println("表信息不存在，请确认后再生成！");
			return;
		}
		//需要首先生成xml文件
		UploadUtil.uploadXmlFile(tableName, infos);
		UploadUtil.uploadBean(tableName, infos);
		UploadUtil.uploadController(tableName, infos);
		UploadUtil.uploadService(tableName, infos);
		UploadUtil.uploadMapper(tableName, infos);
	}

}
