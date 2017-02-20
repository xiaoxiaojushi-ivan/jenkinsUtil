package com.ndpmedia.comm.sql.tableExangeEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName: TableExangeEntity
 * @Description: 实现一个table转化为实体
 * @author ivan.wang
 * @date 2015年7月12日
 * 
 */
public class TableExangeEntity extends TableExangeEntityAbstract {
    private String beanClassName;
    private Connection getConn() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, NAME, PASS);
        } catch (ClassNotFoundException e) {
            // 以堆栈的方式将错误信息打印出来
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn; // 将连接对象返回
    }

    @Override
    public void getOneTableExangeEntity() {
        beanClassName=initcap(tablename) + "Bean";
        // 查要生成实体类的表
        String sql = "select * from " + tablename;
        PreparedStatement pStemt = null;
        try {
            pStemt = getConn().prepareStatement(sql);
            ResultSetMetaData rsmd = pStemt.getMetaData();
            int size = rsmd.getColumnCount(); // 统计列
            colnames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];
            for (int i = 0; i < size; i++) {
                colnames[i] = rsmd.getColumnName(i + 1);
                colTypes[i] = rsmd.getColumnTypeName(i + 1);

                if (colTypes[i].equalsIgnoreCase("datetime")) {
                    f_util = true;
                }
                if (colTypes[i].equalsIgnoreCase("image")
                        || colTypes[i].equalsIgnoreCase("text")) {
                    f_sql = true;
                }
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }

            String content = parse(packName, colnames, colTypes, colSizes);

            try {
                String packNamePath = packName.replaceAll("\\.", "/");
                File directory = new File(sourcePath + packNamePath);
                String path = directory.getAbsolutePath();
                path = path + "\\" + beanClassName + ".java";
                FileWriter fw = new FileWriter(path);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(content);
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllTablesExangeEntity() {
        Statement stemt = null;
        String sql = "show tables";
        try {
            stemt = getConn().createStatement();
            ResultSet rs = stemt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                tableList.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 功能：生成实体类主体代码
     * 
     * @param colnames
     * @param colTypes
     * @param colSizes
     * @return
     */
    private String parse(String packName, String[] colnames, String[] colTypes,
            int[] colSizes) {
        StringBuffer sb = new StringBuffer();

        sb.append("package " + packName + ";\r\n");
        // 判断是否导入工具包
        if (f_util) {
            // sb.append("import java.util.Date;\r\n");
            sb.append("import java.sql.Timestamp;\r\n");
            sb.append("import java.sql.Time;\r\n");
            sb.append("import java.sql.Date;\r\n");
            sb.append("import java.math.BigDecimal;\r\n");

        }
        if (f_sql) {
            sb.append("import java.sql.*;\r\n");
        }
        sb.append("\r\n");
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * @ClassName: " + beanClassName + "\r\n");
        sb.append(" * @Description: " + beanClassName + " EntityClass\r\n");
        sb.append(" * @author " + this.authorName + "\r\n");
        sb.append(" * \r\n");
        sb.append(" */ \r\n");
        // 实体部分
        sb.append("\r\npublic class " + beanClassName + "{\r\n");
        processAllAttrs(sb);// 属性
        processAllMethod(sb);// get set方法
        sb.append("}\r\n");

        return sb.toString();
    }

    /**
     * 功能：生成所有属性
     * 
     * @param sb
     */
    private void processAllAttrs(StringBuffer sb) {

        for (int i = 0; i < colnames.length; i++) {
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " "
                    + colnames[i] + ";\r\n");
        }

    }

    /**
     * 功能：生成所有方法
     * 
     * @param sb
     */
    private void processAllMethod(StringBuffer sb) {
        //构造方法
        sb.append("\r\n");
        sb.append("\tpublic "+beanClassName+"() {\r\n" );
        sb.append("\t}");
        sb.append("\r\n");
        for (int i = 0; i < colnames.length; i++) {
            //链式bean方法
            sb.append("\r\n");
            sb.append("\tpublic " +beanClassName+ " bean" + initcap(colnames[i]) + "("
                    + sqlType2JavaType(colTypes[i]) + " " + colnames[i]
                    + "){\r\n");
            sb.append("\t\tthis." + colnames[i] + " = " + colnames[i] + ";\r\n");
            sb.append("\t\treturn this;\r\n");
            sb.append("\t}\r\n");
            //get 方法
            sb.append("\r\n");
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get"
                    + initcap(colnames[i]) + "(){\r\n");
            sb.append("\t\treturn " + colnames[i] + ";\r\n");
            sb.append("\t}\r\n");
            //set 方法
            sb.append("\r\n");
            sb.append("\tpublic void" + " set"
                    + initcap(colnames[i]) + "("+sqlType2JavaType(colTypes[i])+" "+colnames[i]+"){\r\n");
            sb.append("\t\tthis." + colnames[i] + " = " + colnames[i] + ";\r\n");
            sb.append("\t}\r\n");
        }

    }

    /**
     * 功能：将输入字符串的首字母改成大写
     * 
     * @param str
     * @return
     */
    private String initcap(String str) {

        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }

        return new String(ch);
    }

    /**
     * 功能：获得列的数据类型
     * 
     * @param sqlType
     * @return
     */
    private String sqlType2JavaType(String sqlType) {

        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "byte";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "int";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "float";
        } else if (sqlType.equalsIgnoreCase("decimal")) {
            return "BigDecimal";
        } else if (sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real")
                || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "double";
        } else if (sqlType.equalsIgnoreCase("varchar")
                || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar")
                || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "Timestamp";
        } else if (sqlType.equalsIgnoreCase("time")) {
            return "Time";
        } else if (sqlType.equalsIgnoreCase("date")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }

        return null;
    }

}
