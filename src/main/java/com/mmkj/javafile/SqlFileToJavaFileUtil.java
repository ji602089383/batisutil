package com.mmkj.javafile;

import com.mmkj.sqlfile.SqlEntity;
import com.mmkj.sqlfile.SqlFieldAttribute;
import com.mmkj.util.Properties;
import com.mmkj.util.StringUtils;
import com.mmkj.util.SwitchUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author by jicai on 2019/7/16.
 */
public class SqlFileToJavaFileUtil {

    private static HashMap<String, JavaFileContent> hashMap = new HashMap<>();

    private static final String FIELD_TEMPLATE = "private %s %s ;";

    private static final String BIG_DECIMAL_PACKAGE_NAME = "import java.math.BigDecimal;";

    private static final String BIG_INTEGER_PACKAGE_NAME = "import java.math.BigInteger;";

    private static final String DATE_PACKAGE_NAME = "import java.sql.Date;";

    private static final String TIMESAMP_PACKAGE_NAME = "import java.sql.Timestamp;";

    private static final String TIME_PACKAGE_NAME = "import java.sql.Time;";

    static {
        JavaFileContent jfcBit = new JavaFileContent();
        jfcBit.setDefaultType("Boolean");
        hashMap.put("bit", jfcBit);

        JavaFileContent jfcDecimal = new JavaFileContent();
        jfcDecimal.setDefaultType("BigDecimal");
        jfcDecimal.setDefaultImportPackageName(BIG_DECIMAL_PACKAGE_NAME);
        hashMap.put("decimal", jfcDecimal);

        JavaFileContent jfcInt = new JavaFileContent();
        jfcInt.setDefaultType("Integer");
        jfcInt.setUnsignedType("Long");
        hashMap.put("int", jfcInt);

        JavaFileContent jfcTinyInt = new JavaFileContent();
        jfcTinyInt.setDefaultType("Integer");
        jfcTinyInt.setUnsignedType("Integer");
        jfcTinyInt.setLengthIsOne("Boolean");
        hashMap.put("tinyint", jfcTinyInt);

        JavaFileContent jfcSmallInt = new JavaFileContent();
        jfcSmallInt.setDefaultType("Integer");
        jfcSmallInt.setUnsignedType("Integer");
        hashMap.put("smallint", jfcSmallInt);

        JavaFileContent jfcMediumInt = new JavaFileContent();
        jfcMediumInt.setDefaultType("Integer");
        jfcMediumInt.setUnsignedType("Long");
        hashMap.put("mediumint", jfcMediumInt);

        JavaFileContent jfcBigInt = new JavaFileContent();
        jfcBigInt.setDefaultType("Long");
        jfcBigInt.setUnsignedType("BigInteger");
        jfcBigInt.setUnsignedImportPackageName(BIG_INTEGER_PACKAGE_NAME);
        hashMap.put("bigint", jfcBigInt);

        JavaFileContent jfcInteger = new JavaFileContent();
        jfcInteger.setDefaultType("Integer");
        jfcInteger.setUnsignedType("Long");
        hashMap.put("integer", jfcInteger);

        JavaFileContent jfcDouble = new JavaFileContent();
        jfcDouble.setDefaultType("Double");
        jfcDouble.setUnsignedType("Double");
        hashMap.put("double", jfcDouble);

        JavaFileContent jfcFloat = new JavaFileContent();
        jfcFloat.setDefaultType("Float");
        jfcFloat.setUnsignedType("Float");
        hashMap.put("float", jfcFloat);

        JavaFileContent jfcDate = new JavaFileContent();
        jfcDate.setDefaultType("Date");
        jfcDate.setDefaultImportPackageName(DATE_PACKAGE_NAME);
        hashMap.put("date", jfcDate);

        JavaFileContent jfcDateTime = new JavaFileContent();
        jfcDateTime.setDefaultType("Timestamp");
        jfcDateTime.setDefaultImportPackageName(TIMESAMP_PACKAGE_NAME);
        hashMap.put("datetime", jfcDateTime);

        JavaFileContent jfcTime = new JavaFileContent();
        jfcTime.setDefaultType("Time");
        jfcTime.setDefaultImportPackageName(TIME_PACKAGE_NAME);
        hashMap.put("time", jfcTime);

        JavaFileContent jfcTimestamp = new JavaFileContent();
        jfcTimestamp.setDefaultType("Timestamp");
        jfcTimestamp.setDefaultImportPackageName(TIMESAMP_PACKAGE_NAME);
        hashMap.put("timestamp", jfcTimestamp);

        JavaFileContent jfcYear = new JavaFileContent();
        jfcYear.setDefaultType("Date");
        jfcYear.setDefaultImportPackageName(DATE_PACKAGE_NAME);
        hashMap.put("year", jfcYear);

        JavaFileContent jfcChar = new JavaFileContent();
        jfcChar.setDefaultType("String");
        hashMap.put("char", jfcChar);

        JavaFileContent jfcEnum = new JavaFileContent();
        jfcEnum.setDefaultType("String");
        hashMap.put("enum", jfcEnum);

        JavaFileContent jfcLongText = new JavaFileContent();
        jfcLongText.setDefaultType("String");
        hashMap.put("longtext", jfcLongText);

        JavaFileContent jfcMediumtext = new JavaFileContent();
        jfcMediumtext.setDefaultType("String");
        hashMap.put("mediumtext", jfcMediumtext);

        JavaFileContent jfcText = new JavaFileContent();
        jfcText.setDefaultType("String");
        hashMap.put("text", jfcText);

        JavaFileContent jfcTinytext = new JavaFileContent();
        jfcTinytext.setDefaultType("String");
        hashMap.put("tinytext", jfcTinytext);

        JavaFileContent jfcVarchar = new JavaFileContent();
        jfcVarchar.setDefaultType("String");
        hashMap.put("varchar", jfcVarchar);

        JavaFileContent jfcVarbinary = new JavaFileContent();
        jfcVarbinary.setDefaultType("byte[]");
        hashMap.put("varbinary", jfcVarbinary);

        JavaFileContent jfcBinary = new JavaFileContent();
        jfcBinary.setDefaultType("byte[]");
        hashMap.put("binary", jfcBinary);

        JavaFileContent jfcBlob = new JavaFileContent();
        jfcBlob.setDefaultType("byte[]");
        hashMap.put("blob", jfcBlob);

        JavaFileContent jfcTinyblob = new JavaFileContent();
        jfcTinyblob.setDefaultType("byte[]");
        hashMap.put("tinyblob", jfcTinyblob);

        JavaFileContent jfcMediumblob = new JavaFileContent();
        jfcMediumblob.setDefaultType("byte[]");
        hashMap.put("mediumblob", jfcMediumblob);

        JavaFileContent jfcLongblob = new JavaFileContent();
        jfcLongblob.setDefaultType("byte[]");
        hashMap.put("longblob", jfcLongblob);
    }

    private static final JavaFileContent DEFAULT_JFC = new JavaFileContent("default");

    private static final String INDENT = "    ";

    public void doJavaEntityFile(String javaFilePath,  String packageName, SqlEntity sqlEntity) throws Exception{

        List<JavaFileContent> jfcs = sqlEntity.getSfaList().stream().map(this::getJavaFileContent).collect(Collectors.toList());
        Set<String> importPackageNames = jfcs.stream().map(JavaFileContent::getFileImportPackageNameString).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());

        File file = new File(javaFilePath + "\\" + sqlEntity.getJavaEntityFileName() + ".java");
        if(!file.exists()){
            file.createNewFile();
        }else{
            file.delete();
            file.createNewFile();
        }
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);

        /**
         * 写入包名
         */
        write(bw, "package " + packageName + ";");

        /**
         * 写入导入的包
         */
        write(bw, "import lombok.Data;");
        if(SwitchUtil.SERIALIZABLE){
            write(bw, "import java.io.Serializable;");
        }
        if(Properties.getInstance().getSwagger().equals("true")){
            write(bw, "import io.swagger.annotations.ApiModel;");
            write(bw, "import io.swagger.annotations.ApiModelProperty;");
        }
        Iterator<String> iterator = importPackageNames.iterator();
        while (iterator.hasNext()){
            write(bw, iterator.next());
        }

        /**
         * 写入类注释和类名
         * */
        write(bw, "/**");
        write(bw, "* " + sqlEntity.getNotesToTable());
        write(bw, "* @author by admin on " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        write(bw, "*/");
        write(bw, "@Data");
        if(Properties.getInstance().getSwagger().equals("true")){
            write(bw, "@ApiModel(value = \""+ sqlEntity.getJavaEntityFileName() +"\", description = \""+ sqlEntity.getNotesToTable() +"\")");
        }
        if(SwitchUtil.SERIALIZABLE){
            write(bw, "public class " + sqlEntity.getJavaEntityFileName() + " implements Serializable {");
            write(bw, "private static final long serialVersionUID = 1L;");
        }else{
            write(bw, "public class " + sqlEntity.getJavaEntityFileName() + "{");
        }

        /**
         * 写入字段
         */
        for(int i = 0, b = jfcs.size(); i < b ; i++){
            JavaFileContent jfc = jfcs.get(i);
            if(Properties.getInstance().getSwagger().equals("true")){
                write(bw, INDENT + "@ApiModelProperty(\""+ jfc.getFieldNotes() +"\")");
            }else{
                write(bw, INDENT + "/**");
                write(bw, INDENT + "* " + jfc.getFieldNotes());
                write(bw, INDENT + "*/");
            }
            write(bw, INDENT + jfc.getFileFieldString());
            bw.newLine();
        }
        bw.write("}");
        bw.flush();
        bw.close();
    }

    public void doJavaMapperFile(String javaFilePath, String entityPackageName, String mapperPackageName, SqlEntity sqlEntity) throws Exception{

        File file = new File(javaFilePath + "\\" + sqlEntity.getJavaMapperFileName() + ".java");
        if(!file.exists()){
            file.createNewFile();
        }else{
            file.delete();
            file.createNewFile();
        }
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        /**
         * 写入包名
         */
        write(bw, "package " + mapperPackageName + ";");
        /**
         * 写入导入的包
         */
        write(bw, "import org.apache.ibatis.annotations.Mapper;");
        write(bw, "import org.springframework.stereotype.Repository;");
        write(bw, "import " + entityPackageName + "." + sqlEntity.getJavaEntityFileName() + ";");
        write(bw, "import java.util.List;");

        /**
         * 写入类注释和类名
         * */
        write(bw, "/**");
        write(bw, "* " + sqlEntity.getNotesToTable());
        write(bw, "* @author by admin on " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        write(bw, "*/");
        write(bw, "@Mapper");
        write(bw, "@Repository");
        write(bw, "public interface " + sqlEntity.getJavaMapperFileName() + "{");

        /**
         * 写入方法
         */
        write(bw, INDENT + "int insertSelective( " + sqlEntity.getJavaEntityFileName() + " record);");
        bw.newLine();
        write(bw, INDENT + "int updateByPrimaryKey( " + sqlEntity.getJavaEntityFileName() + " record);");
        bw.newLine();
        write(bw, INDENT + sqlEntity.getJavaEntityFileName() + " findById(int id);");
        bw.newLine();
        write(bw, INDENT + "int insertList(List<" + sqlEntity.getJavaEntityFileName() + "> list);");
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }

    private void write(BufferedWriter bw, String content) throws IOException {
        bw.write(content);
        bw.newLine();
    }


    /**
     * 根据传入的sql字段 转换成 对应的java字段
     * @param sfa
     * @return
     */
    private JavaFileContent getJavaFileContent(SqlFieldAttribute sfa){
        JavaFileContent jfc = hashMap.getOrDefault(sfa.getFieldType(), DEFAULT_JFC);
        JavaFileContent returnJfc = new JavaFileContent();
        if(null == sfa.getUnsigned()){
            returnJfc.setFileFieldString(String.format(FIELD_TEMPLATE, jfc.getDefaultType(), sfa.getJavaFieldName()));
            returnJfc.setFileImportPackageNameString(jfc.getDefaultImportPackageName());
        }else if(sfa.getUnsigned()){
            returnJfc.setFileFieldString(String.format(FIELD_TEMPLATE, jfc.getUnsignedType(), sfa.getJavaFieldName()));
            returnJfc.setFileImportPackageNameString(jfc.getUnsignedImportPackageName());
        }
        returnJfc.setFieldNotes(sfa.getFieldNotes());
        return returnJfc;
    }
}
