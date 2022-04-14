package com.mmkj;

import com.mmkj.frame.BasicInfoFrame;
import com.mmkj.frame.DataInfoFrame;
import com.mmkj.javafile.SqlFileToJavaFileUtil;
import com.mmkj.sqlfile.SqlDataProcess;
import com.mmkj.sqlfile.SqlEntity;
import com.mmkj.util.FilePath;
import com.mmkj.util.Properties;
import com.mmkj.util.StringUtils;
import com.mmkj.xmlfile.SqlFileToXmlFileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by jicai on 2019/7/18.
 */
@Slf4j
@Data
public class StartWindows {

    public static void main(String[] args) {
        StartWindows startWindows = new StartWindows();
        BasicInfoFrame basicInfoFrame = new BasicInfoFrame();
        DataInfoFrame dataInfoFrame = new DataInfoFrame();
        basicInfoFrame.setMyCompoments(dataInfoFrame, startWindows);
        //c二十zxcz

    }

    private Map<Integer, Checkbox> checkboxMapLeft = new HashMap<>();

    private Map<Integer, Checkbox> checkboxMapRight = new HashMap<>();

    private Map<Integer, SqlEntity> sqlMap = new HashMap<>();

    private Map<Integer, Checkbox> selectMap = new HashMap<>();

    public void connectSuccess() throws Exception {
        /**
         * 获取数据库的所有信息
         */
        SqlDataProcess sqlDataProcess = new SqlDataProcess();
        sqlMap.putAll(sqlDataProcess.dataProcess());
    }

    public boolean selectSqlEntity(String content){
        Map<Integer, Checkbox> map = new HashMap<>();
        for(Map.Entry<Integer, Checkbox> entityEntry: checkboxMapLeft.entrySet()){
            if(entityEntry.getValue().getLabel().contains(content)){
                map.put(entityEntry.getKey(), entityEntry.getValue());
            }
        }
        if(map.size() > 0){
            selectMap = map;
            return true;
        }else {
            return false;
        }
    }

    public void goStart() throws Exception{
        Properties properties = Properties.getInstance();
        String parentPackageName = "com.joyowo.xbz";
        if(StringUtils.isNotEmpty(properties.getBasicFileStructure())){
            parentPackageName = properties.getBasicFileStructure();
        }
        String entityPackageName = parentPackageName + ".entity";
        String mapperPackageName = parentPackageName + ".mapper";
        String xmlPackageName = parentPackageName + ".xml";
        FilePath filePath = new FilePath();
        filePath.createFileDir(parentPackageName);

        /**
         * 写文件
         */
        SqlFileToJavaFileUtil javafileUtil = new SqlFileToJavaFileUtil();
        SqlFileToXmlFileUtil xmlFileUtil = new SqlFileToXmlFileUtil();

        for (Map.Entry<Integer, Checkbox> entry : checkboxMapRight.entrySet()){
            SqlEntity p = sqlMap.get(entry.getKey());
            javafileUtil.doJavaEntityFile(filePath.getEntityPath(), entityPackageName, p);
            javafileUtil.doJavaMapperFile(filePath.getMapperPath(), entityPackageName, mapperPackageName, p);
            xmlFileUtil.doXmlFile(filePath.getXmzPath(), mapperPackageName, entityPackageName, p);
        }
    }
}
