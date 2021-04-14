package com.mmkj.sqlfile;

import lombok.Data;

/**
 * @author by jicai on 2019/7/15.
 */
@Data
public class SqlFieldAttribute {

    private String sqlFieldName;

    private String javaFieldName;

    private String fieldType;

    private Integer fieldLong;

    private Integer decimalPoint;

    private Boolean isSqlNull;

    private Integer indexType;

    private String fieldNotes;

    private Boolean unsigned;
}
