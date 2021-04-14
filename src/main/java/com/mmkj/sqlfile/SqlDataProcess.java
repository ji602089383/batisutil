package com.mmkj.sqlfile;

import com.mmkj.enums.IndexTypeEnum;
import com.mmkj.util.Properties;
import com.mmkj.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by jicai on 2019/7/18.
 */
public class SqlDataProcess {
    private static final String url5 ="jdbc:mysql://%s?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    private static final String url8 ="jdbc:mysql://%s?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=GMT";

    public Map<Integer, SqlEntity> dataProcess() throws Exception {
        Properties properties = Properties.getInstance();
        String driverClassName = "com.mysql.jdbc.Driver" ;
        String ip = properties.getIp() + "/" +properties.getTableName();
        String url = String.format(url5, ip);
        if("8.0".equals(properties.getEdition())){
            driverClassName = "com.mysql.cj.jdbc.Driver";
            url = String.format(url8, ip);
        }
        Class.forName(driverClassName) ;
        Connection con = DriverManager.getConnection(url, properties.getUserName(), properties.getPassWord());

        Map<Integer, SqlEntity> seList = new HashMap<>();
        String tablesSql = "show table status;";
        PreparedStatement tablesPs = con.prepareStatement(tablesSql);
        ResultSet tablesSet = tablesPs.executeQuery();
        int i = 0 ;
        while (tablesSet.next()){
//            System.out.println("table_name: " + tablesSet.getString("name") + "   table_comment:" + tablesSet.getString("Comment"));
            SqlEntity sqlEntity = new SqlEntity();

            sqlEntity.setId(i ++);
            /**
             * 表名
             */
            sqlEntity.setSqlTableName(tablesSet.getString("name"));

            /**
             * 实体类名
             */
            sqlEntity.setJavaEntityFileName(doClassOrFileNameUnderlineProcess(sqlEntity.getSqlTableName()));
            if(StringUtils.isNotEmpty(properties.getDeleteFileStart())){
                sqlEntity.setJavaEntityFileName(sqlEntity.getJavaEntityFileName().replace(properties.getDeleteFileStart(), ""));
            }


            /**
             * mapper名
             */
            sqlEntity.setJavaMapperFileName(sqlEntity.getJavaEntityFileName() + "Mapper");


            /**
             * 表注释
             */
            sqlEntity.setNotesToTable(tablesSet.getString("Comment"));

            /**
             * xml名
             */
            sqlEntity.setXmlFileName(sqlEntity.getJavaMapperFileName());


            String fieldSql = "show full columns from " + tablesSet.getString("name");
            PreparedStatement fieldPs = con.prepareStatement(fieldSql);
            ResultSet fieldSet = fieldPs.executeQuery();
            List<SqlFieldAttribute> sfaList = new ArrayList<>();
            while (fieldSet.next()) {
                SqlFieldAttribute sfa = new SqlFieldAttribute();
                /**
                 * 字段名
                 */
                sfa.setSqlFieldName(fieldSet.getString("Field"));
                sfa.setJavaFieldName(doFieldNameUnderlineProcess(sfa.getSqlFieldName()));
                /**
                 * 字段属性
                 */
                sfa.setFieldType(fieldSet.getString("Type"));

                /**
                 * 无符号？
                 */
                if(sfa.getFieldType().contains("unsigned")){
                    sfa.setUnsigned(true);
                    sfa.setFieldType(sfa.getFieldType().replace("unsigned", "").trim());
                }

                /**
                 * 小数点和长度
                 */
                if(sfa.getFieldType().contains("(")){
                    int start = sfa.getFieldType().indexOf("(");
                    int end = sfa.getFieldType().indexOf(")");
                    String numberString = sfa.getFieldType().substring(start + 1, end);
                    if( numberString.contains(",")){
                        String[] str = numberString.split(",");
                        sfa.setFieldLong(Integer.parseInt(str[0]));
                        sfa.setDecimalPoint(Integer.parseInt(str[1]));
                    }else {
                        sfa.setFieldLong(Integer.parseInt(numberString));
                    }
                    sfa.setFieldType(sfa.getFieldType().substring(0, start));
                }

                /**
                 * 是否为空
                 */
                String isNull = fieldSet.getString("Null");
                if(StringUtils.isNotEmpty(isNull)){
                    if("NO".equals(isNull)){
                        sfa.setIsSqlNull(false);
                    }else{
                        sfa.setIsSqlNull(true);
                    }
                }

                /**
                 * 主键 和 索引
                 */
                String key = fieldSet.getString("Key");
                if(StringUtils.isNotEmpty(key)){
                    sfa.setIndexType(IndexTypeEnum.getCode(key));
                    if(IndexTypeEnum.PrimaryKey.getCode().equals(sfa.getIndexType())){
                        sqlEntity.setPrimaryKeyFieldJavaString(sfa.getJavaFieldName());
                        sqlEntity.setPrimaryKeyFieldSqlString(sfa.getSqlFieldName());
                    }
                }

                /**
                 * 注释
                 */
                sfa.setFieldNotes(fieldSet.getString("Comment"));
//                System.out.println("Field : " + fieldSet.getString("Field") + "   type:" + fieldSet.getString("Type") + "  Key:" + fieldSet.getString("Key"));
                sfaList.add(sfa);
            }
            sqlEntity.setSfaList(sfaList);
            seList.put(sqlEntity.getId(), sqlEntity);
        }
        return seList;
    }

    private String doFieldNameUnderlineProcess(String oldString){
        if(StringUtils.isEmpty(oldString)){
            return null;
        }
        int i ;
        StringBuilder sb = new StringBuilder(oldString);
        while ( (i = oldString.indexOf("_")) > -1){
            if(i < oldString.length()){
                sb.replace(i + 1, i + 2, oldString.substring(i + 1, i + 2).toUpperCase());
                sb.replace(i, i + 1, "");
                oldString = sb.toString();
            }
        }
        return sb.toString();
    }

    private String doClassOrFileNameUnderlineProcess(String oldString){
        String returnString = doFieldNameUnderlineProcess(oldString);
        StringBuilder sb = new StringBuilder(returnString);
        sb.replace(0,1, returnString.substring(0, 1).toUpperCase());
        return sb.toString();
    }
}
