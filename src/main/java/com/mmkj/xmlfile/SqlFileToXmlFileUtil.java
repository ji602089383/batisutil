package com.mmkj.xmlfile;

import com.mmkj.sqlfile.SqlEntity;
import com.mmkj.sqlfile.SqlFieldAttribute;
import com.mmkj.util.StringUtils;

import java.io.*;
import java.util.List;

/**
 * @author by jicai on 2019/7/17.
 */
public class SqlFileToXmlFileUtil {

    private static String INDENT_ONE_LEVEL = "    ";
    private static String INDENT_TWO_LEVEL = "        ";
    private static String INDENT_THREE_LEVEL = "            ";

    public void doXmlFile(String xmlFilePath, String mapperPackageName, String entityPackageName, SqlEntity sqlEntity) throws Exception{
        List<SqlFieldAttribute> sfaList = sqlEntity.getSfaList();

        File file = new File(xmlFilePath + "\\" + sqlEntity.getXmlFileName() + ".xml");
        if(!file.exists()){
            file.createNewFile();
        }else{
            file.delete();
            file.createNewFile();
        }
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        /**
         * 写入命名空间
         */
        write(bw, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> ");
        write(bw, "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");

        /**
         * 写入头部
         */
        write(bw, "<mapper namespace=\"" + mapperPackageName + "."+ sqlEntity.getJavaMapperFileName() +"\">");

        /**
         * 写入mapper
         */
        write(bw, INDENT_ONE_LEVEL + "<resultMap id=\"AllColumnMap\" type=\"" + entityPackageName + "." + sqlEntity.getJavaEntityFileName() + "\">");
        for (int i = 0, b = sfaList.size(); i < b; i ++){
            write(bw, INDENT_TWO_LEVEL + "<result column=\"" + sfaList.get(i).getSqlFieldName() + "\" property=\"" + sfaList.get(i).getJavaFieldName() + "\"/>");
        }
        write(bw, INDENT_ONE_LEVEL + "</resultMap>");

        /**
         * 写入sql字段
         */
        bw.newLine();
        write(bw, INDENT_ONE_LEVEL + "<sql id=\"all_column\">");
        for (int i = 0, b = sfaList.size(); i < b; i ++){
            if(i + 1 == b){
                write(bw, INDENT_TWO_LEVEL + "`" + sfaList.get(i).getSqlFieldName() + "`");
            }else{
                write(bw, INDENT_TWO_LEVEL + "`" + sfaList.get(i).getSqlFieldName() + "`,");
            }
        }
        write(bw, INDENT_ONE_LEVEL + "</sql>");


        /**
         * 写入 insertSelective
         */
        bw.newLine();
        if(StringUtils.isNotEmpty(sqlEntity.getPrimaryKeyFieldJavaString())){
            write(bw, INDENT_ONE_LEVEL + "<insert id=\"insertSelective\" useGeneratedKeys=\"true\" keyProperty=\"" + sqlEntity.getPrimaryKeyFieldJavaString() + "\">");
        }else{
            write(bw, INDENT_ONE_LEVEL + "<insert id=\"insertSelective\"");
        }
        write(bw, INDENT_TWO_LEVEL + "INSERT INTO " + sqlEntity.getSqlTableName());
        write(bw, INDENT_TWO_LEVEL + "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (int i = 0, b = sfaList.size(); i < b; i ++){
            SqlFieldAttribute sfa = sfaList.get(i);
            write(bw, INDENT_THREE_LEVEL + "<if test=\""+ sfa.getJavaFieldName() +"!=null\"> `"+ sfa.getSqlFieldName() +"`,</if>");
        }
        write(bw, INDENT_TWO_LEVEL + "</trim>");
        write(bw, INDENT_TWO_LEVEL + "VALUES");
        write(bw, INDENT_TWO_LEVEL + "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (int i = 0, b = sfaList.size(); i < b; i ++){
            SqlFieldAttribute sfa = sfaList.get(i);
            write(bw, INDENT_THREE_LEVEL + "<if test=\""+ sfa.getJavaFieldName() +"!=null\"> #{"+ sfa.getJavaFieldName() +"},</if>");
        }
        write(bw, INDENT_TWO_LEVEL + "</trim>");
        write(bw,INDENT_TWO_LEVEL + "</insert>");


        /**
         * 写入 findById
         */
        bw.newLine();
        write(bw, INDENT_ONE_LEVEL + "<select id=\"findById\" resultMap=\"AllColumnMap\"  parameterType=\"int\">");
        write(bw, INDENT_TWO_LEVEL + "SELECT <include refid=\"all_column\"/>");
        write(bw, INDENT_TWO_LEVEL + "FROM " + sqlEntity.getSqlTableName());
        if(StringUtils.isNotEmpty(sqlEntity.getPrimaryKeyFieldSqlString())){
            write(bw, INDENT_TWO_LEVEL + "WHERE `" + sqlEntity.getPrimaryKeyFieldSqlString() + "` = #{id}");
        }else{
            write(bw, INDENT_TWO_LEVEL + "WHERE `" + sfaList.get(0).getSqlFieldName() + "` = #{id}");
        }
        write(bw, INDENT_ONE_LEVEL + "</select>");

        /**
         * 写入 insertList
         */
        bw.newLine();
        write(bw, INDENT_ONE_LEVEL + "<insert id=\"insertList\">");
        write(bw, INDENT_TWO_LEVEL + "INSERT INTO " + sqlEntity.getSqlTableName() + "(<include refid=\"all_column\"/>)");
        write(bw, INDENT_TWO_LEVEL + "VALUES ");
        write(bw, INDENT_TWO_LEVEL + "<foreach collection=\"list\" item=\"pojo\" index=\"index\" separator=\",\">");
        write(bw, INDENT_THREE_LEVEL + "( ");
        for (int i = 0, b = sfaList.size(); i < b; i ++){
            if(i + 1 == b){
                write(bw, INDENT_THREE_LEVEL + " #{pojo." + sfaList.get(i).getJavaFieldName() + "}");
            }else{
                write(bw, INDENT_THREE_LEVEL + " #{pojo." + sfaList.get(i).getJavaFieldName() + "},");
            }
        }
        write(bw, INDENT_THREE_LEVEL + ")");
        write(bw, INDENT_THREE_LEVEL + "</foreach>");
        write(bw, INDENT_ONE_LEVEL + "</insert>");

        /**
         * 写入 updateByPrimaryKey
         */
        bw.newLine();
        write(bw, INDENT_ONE_LEVEL + "<update id=\"updateByPrimaryKey\">");
        write(bw, INDENT_TWO_LEVEL + "UPDATE " + sqlEntity.getSqlTableName());
        write(bw, INDENT_TWO_LEVEL + "<set>");
        for (int i = 0, b = sfaList.size(); i < b; i ++){
            SqlFieldAttribute sfa = sfaList.get(i);
            write(bw, INDENT_THREE_LEVEL + "<if test=\""+ sfa.getJavaFieldName() +"!=null\">`"+ sfa.getSqlFieldName() +"` = #{"+ sfa.getJavaFieldName() +"},</if>");
        }
        write(bw, INDENT_THREE_LEVEL + "</set>");
        write(bw, INDENT_TWO_LEVEL + "WHERE `" + sqlEntity.getPrimaryKeyFieldSqlString() + "` = #{id}");
        write(bw, INDENT_ONE_LEVEL + "</update>");
        write(bw, "</mapper>");
        bw.flush();
        bw.close();
    }

    private void write(BufferedWriter bw, String content) throws IOException {
        bw.write(content);
        bw.newLine();
    }
}
