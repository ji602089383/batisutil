package com.mmkj.enums;

/**
 * @author by jicai on 2019/7/15.
 */
public enum IndexTypeEnum {
    /**
     * 索引方式 1、主键  2、普通
     */
    DAY(1, "全文索引","MUL", "FULLTEXT"),
    WEEK(2,"普通索引","MUL", "NORMAL"),
    MONTH(3, "空间索引", "MUL", "SPATIAL"),
    FINISH(4, "唯一索引", "UNI", "UNIQUE"),
    PrimaryKey(5, "主键索引", "PRI","Primary key");

    private Integer code;
    private String remark;

    private String sqlFull;

    private String sqlSimple;

    IndexTypeEnum(Integer code, String remark, String sqlSimple, String sqlFull) {
        this.code = code;
        this.remark = remark;
        this.sqlFull = sqlFull;
        this.sqlSimple = sqlSimple;
    }

    public Integer getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    // 普通方法
    public static String getRemark(Integer code) {
        for (IndexTypeEnum c : IndexTypeEnum.values()) {
            if (c.code.equals(code)) {
                return c.remark;
            }
        }
        return null;
    }

    public static Integer getCode(String sqlSimple) {
        for (IndexTypeEnum c : IndexTypeEnum.values()) {
            if (c.sqlSimple.equals(sqlSimple)) {
                return c.code;
            }
        }
        return null;
    }
}
