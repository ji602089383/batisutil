package com.mmkj.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by jicai on 2019/7/18.
 */
@Data
@Slf4j
public class Properties {
    private static Properties ourInstance = new Properties();
    public String ip;
    public String tableName;
    public String userName;
    public String passWord;
    public String basicFileStructure;
    public String deleteFileStart;
    public String edition;
    public String swagger;

    public String idNum;
    public String idNow;
    public Map<String, String> map = new HashMap<>();

    private java.util.Properties properties = new java.util.Properties();
    public String basicUrl = System.getProperty("user.dir") + "\\myBatisUtil";
    public String fileUrl = basicUrl + "\\myBatisUtil.properties";


    {
        File file = new File(this.basicUrl);
        file.mkdirs();
        try{
            file = new File(this.fileUrl);
            file.createNewFile();
            file.setWritable(true);
            file.setReadable(true);
            properties.load(new FileInputStream(file));
            this.idNum = properties.getProperty("idNum");

            if(StringUtils.isNotEmpty(this.idNum)){
                String[] sts = this.idNum.split(",");
                for (int i = 0, b = sts.length; i < b; i ++){
                    String id = sts[i];
                    String ip = properties.getProperty(id + "ip");
                    String tableName = properties.getProperty(id + "tableName");
                    if(StringUtils.isNotEmpty(ip) && StringUtils.isNotEmpty(tableName)){
                        this.map.put(id, ip + "-" + tableName);
                    }
                }
                if(StringUtils.isEmpty(this.idNow)){
                    this.idNow = this.idNum.split(",")[0];
                }
            }
            this.ip = properties.getProperty(this.idNow + "ip");
            this.userName = properties.getProperty(this.idNow + "username");
            this.passWord = properties.getProperty(this.idNow + "password");
            this.tableName = properties.getProperty(this.idNow + "tableName");
            this.edition = properties.getProperty(this.idNow + "edition");
            this.swagger = properties.getProperty(this.idNow + "swagger");
            this.basicFileStructure = properties.getProperty(this.idNow + "basicFileStructure");
            this.deleteFileStart = properties.getProperty(this.idNow + "deleteFileStart");

        }catch (IOException e){
            if(SwitchUtil.LOG){
                e.printStackTrace();
            }
           log.error("初始化配置文件:", e);
        }
    }

    public static Properties getInstance() {
        return ourInstance;
    }

    /**
     *
     * @param type 1主页添加的  2配置页添加的
     * @param ip
     * @param userName
     * @param passWord
     * @param tableName
     * @param edition
     * @param swagger
     * @param basicFileStructure
     * @throws Exception
     */
    public void setProerties(Integer type, String ip, String userName, String passWord, String tableName,
                             String edition, String swagger, String basicFileStructure, String deleteFileStart) throws Exception{
        File file = new File(this.fileUrl);
        String id = null;
        boolean isNew = true;
        for (Map.Entry<String, String> mapEntry : this.map.entrySet()){
            if(mapEntry.getValue().equals(ip + "-" + tableName)){
                isNew = false;
                id = mapEntry.getKey();
            }
        }
        if(StringUtils.isEmpty(id)){
            if(StringUtils.isNotEmpty(this.idNum)){
                String[] str = this.idNum.split(",");
                id = Integer.parseInt(str[str.length-1]) + 1 + "";
                this.idNum += "," + id;
            }else{
                id = "0";
                this.idNum = id;
            }
        }
        if(type == 1){
            this.idNow = id;
        }
        if(isNew){
            map.put(id, ip + "-" + tableName);
        }

        FileOutputStream fos = new FileOutputStream(file);
        properties.setProperty("idNum", idNum);
        properties.setProperty(id + "ip", ip);
        properties.setProperty(id + "username", userName);
        properties.setProperty(id + "password", passWord);
        properties.setProperty(id + "tableName", tableName);
        properties.setProperty(id + "edition", edition);
        properties.setProperty(id + "swagger", swagger);
        properties.setProperty(id + "basicFileStructure", basicFileStructure);
        properties.setProperty(id + "deleteFileStart", deleteFileStart);
        properties.store(fos, "");

        if(type == 1){
            this.ip = ip;
            this.userName = userName;
            this.passWord = passWord;
            this.tableName = tableName;
            this.edition = edition;
            this.swagger = swagger;
            this.basicFileStructure = basicFileStructure;
            this.deleteFileStart = deleteFileStart;
        }
        fos.close();
    }

    public void setProperties(String id) throws Exception{
        File file = new File(this.fileUrl);
        properties.load(new FileInputStream(file));
        this.ip = properties.getProperty(id + "ip");
        this.userName = properties.getProperty(id + "username");
        this.passWord = properties.getProperty(id + "password");
        this.tableName = properties.getProperty(id + "tableName");
        this.edition = properties.getProperty(id + "edition");
        this.swagger = properties.getProperty(id + "swagger");
        this.basicFileStructure = properties.getProperty(id + "basicFileStructure");
        this.deleteFileStart = properties.getProperty(id + "deleteFileStart");
        this.idNow = id;
    }

    public void deleteProperties(String id) throws Exception{
        File file = new File(this.fileUrl);
        FileOutputStream fos = new FileOutputStream(file);
        properties.remove(id + "id");
        properties.remove(id + "username");
        properties.remove(id + "password");
        properties.remove(id + "tableName");
        properties.remove(id + "edition");
        properties.remove(id + "swagger");
        properties.remove(id + "basicFileStructure");
        properties.remove(id + "deleteFileStart");
        properties.store(fos, "");
        if(this.idNow != null && id.equals(this.idNow)){
            this.idNow = null;
            this.ip = null;
            this.userName = null;
            this.passWord = null;
            this.tableName = null;
            this.edition = null;
            this.swagger = null;
            this.basicFileStructure = null;
            this.deleteFileStart = null ;
        }

        map.remove(id);
        String[] idnums = idNum.split(",");
        List<String> list = Arrays.asList(idnums);
        idNum = null ;
        for(String s : list){
            if(!s.equals(id)){
                idNum += "," + s ;
            }
        }
        idNum = idNum.substring(1);
        fos.close();
    }
}
