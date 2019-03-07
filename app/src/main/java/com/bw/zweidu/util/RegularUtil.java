package com.bw.zweidu.util;

import java.util.regex.Pattern;
public class RegularUtil {
    public static final String REG_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
    //判断手机号
    public static boolean isPhone(String mphone)



    {
        return Pattern.matches(REG_MOBILE,mphone);
    }

    //判断是否为空
    public static boolean isNull(String mnull){
        return mnull.equals("");
    }

    //判断密码的长度
    public static boolean isPass(String mpass){
        return mpass.length()<6;
    }
}
