package com.ndpmedia.comm.sql.tableExangeEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class TableExangeEntityAbstract {
	protected String packName = "com.ndpmedia.bean";// 指定实体生成所在包的路径
	protected String sourcePath = "src/main/java/";// 指定实体生成source路径
	protected String authorName = "ivan.wang";// 作者名字
	protected static String tablename = "absent";// 表名
	protected String[] colnames; // 列名数组
	protected String[] colTypes; // 列名类型数组
	protected int[] colSizes; // 列名大小数组
	protected boolean f_util = false; // 是否需要导入包java.util.*
	protected boolean f_sql = false; // 是否需要导入包java.sql.*

	// 数据库连接
	protected static final String URL = "jdbc:mysql://172.30.10.94:3306/jenkinsUtil";
	protected static final String NAME = "root";
	protected static final String PASS = "root";
	protected static final String DRIVER = "com.mysql.jdbc.Driver";
	
	//所有表
	protected static List<String> tableList =new ArrayList<String>();

	public abstract void getOneTableExangeEntity();

	public abstract void getAllTablesExangeEntity();

	public static void main(String[] args) {
		TableExangeEntityAbstract t = new TableExangeEntity();	
		t.getAllTablesExangeEntity();
		for (String tableName : tableList) {
		    if (tableName.equals("gkt_JDB")) {
		        System.out.println(tableName);
		        tablename=tableName;
		        t.getOneTableExangeEntity();
            }
		}
		System.out.println("所有表转化完毕");
	}

}
