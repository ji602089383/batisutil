package com.mmkj.sqlfile;

import lombok.Data;

import java.util.List;

/**
 * @author by jicai on 2019/7/15.
 */
@Data
public class SqlEntity {

    /**
     * 用于标记查询使用
     */
    private Integer id;

    /**
     * 表字段
     */
    private List<SqlFieldAttribute>  sfaList;

    /**
     * 表注释
     */
    private String notesToTable;

    /**
     * sql表名
     */
    private String sqlTableName;

    /**
     * JAVA实体类文件名 如有sql表名转换过来的
     */
    private String javaEntityFileName;

    /**
     * JAVAmapper类文件名
     */
    private String javaMapperFileName;

    /**
     * xml文件名
     */
    private String xmlFileName;

    /**
     * 主键字段 sql语句
     */
    private String primaryKeyFieldSqlString;

    /**
     * 主键字段 java语句
     */
    private String primaryKeyFieldJavaString;
}
