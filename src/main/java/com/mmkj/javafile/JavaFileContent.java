package com.mmkj.javafile;

import lombok.Data;

/**
 * @author by jicai on 2019/7/16.
 */
@Data
public class JavaFileContent {
    public JavaFileContent(){

    }

    public JavaFileContent(String defaultStr){
        this.defaultImportPackageName = defaultStr;
        this.unsignedImportPackageName = defaultStr;
        this.defaultType = defaultStr;
        this.unsignedType = defaultStr;
        this.lengthIsOne = defaultStr;
    }

    /************* 以下字段提供给编写JAVA文件使用 ***************/
    /**
     * JAVA字段属性 字符串
     */
    private String fileFieldString;

    /**
     * JAVA文件包名 字符串
     */
    private String fileImportPackageNameString;


    /*********** 以下字段提供给初始化HASHMAP使用  ***************/
    /**
     *  默认包名
     */
    private String defaultImportPackageName;

    /**
     * 无符号类型包名
     */
    private String unsignedImportPackageName;

    /**
     * 默认类型
     */
    private String defaultType;

    /**
     * 无符号的类型
     */
    private String unsignedType;

    /**
     * 目前给 tinyint的长度=1 是使用，等于1时是boolean型
     */
    private String lengthIsOne;

    /**
     * 字段注释
     */
    private String fieldNotes;
}
