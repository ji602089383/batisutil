package com.mmkj.util;

import lombok.Data;

import java.io.File;

/**
 * @author by jicai on 2019/7/15.
 */
@Data
public class FilePath {

    public static final String BASIC_PATH = System.getProperty("user.dir") + "\\myBatisUtil";

    public static final String PROJECT_PATH = BASIC_PATH + "\\src\\java\\";

    public static String START_PATH ;

    private String entityPath ;
    private String mapperPath ;
    private String xmzPath;

    /**
     * 根据路径，创建文件夹，
     * 请写到实体类的文件夹的上一层，例如:实体类文件夹路径是com.joyowo.xbz.entity，
     * 你只需填写com.joyowo.xbz,该方法会为你增加entity,mapper,xml等3个文件夹
     * @param filePath
     */
    public void createFileDir( String filePath){
        START_PATH = PROJECT_PATH + filePath.replace('.','\\');
        File file = new File(START_PATH);
        file.delete();

        entityPath = START_PATH + "\\entity";
        File entityFile = new File(entityPath);
        entityFile.mkdirs();

        mapperPath = START_PATH + "\\mapper";
        File mapperFile = new File(mapperPath);
        mapperFile.mkdirs();

        xmzPath = START_PATH + "\\xml";
        File xmlFile = new File(xmzPath);
        xmlFile.mkdirs();
    }

}
