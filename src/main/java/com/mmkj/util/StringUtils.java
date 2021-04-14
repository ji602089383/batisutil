package com.mmkj.util;

/**
 * @author wanglei
 * @since 05.17.2017
 */
public final class StringUtils {

    private StringUtils(){

    }



    /**
     * 判断是否为null
     *
     * @param str 字符串
     * @return true表示为null，false表示不为null
     */
    public static boolean isNull(String str) {
        return str == null;
    }

    /**
     * 判断是否为空
     *
     * @param str 字符串
     * @return true表示为空，false表示不为空
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str) || "".equals(str.trim()) || "请填写!".equals(str);
    }

    /**
     * 判断是否不为空值
     *
     * @param str   字符串
     * @return  true表示不为空值，false表示为空值
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


}
