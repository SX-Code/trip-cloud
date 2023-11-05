package com.swx.common.core.utils;

import java.security.MessageDigest;
import java.util.*;

/**
 * Md5工具类
 * @author pengqi
 * @date 2019年1月23日 上午11:21:35
 *
 */
public class Md5Utils {
	
	/**
     * @Description: 生成MD5
     * @param message
     * @return
     */
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // 创建一个md5算法对象
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte); // 获得MD5字节数组,16*8=128位
            md5 = bytesToHex(md5Byte); // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }
    
    /**
     * @Description: 二进制转十六进制
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
    
    /**
     * 
     * @Description: 签名：请求参数排序并后面补充key值，最后进行MD5加密，返回大写结果
     * @param params 参数内容
     * @return
     */
    public static String signatures(Map<String, Object> params){
        String signatures = "";
        try {
            List<String> paramsStr = new ArrayList<String>();
            for (String key1 : params.keySet()) {
                if(null != key1 && !"".equals(key1)){
                    paramsStr.add(key1);
                }
            }
            Collections.sort(paramsStr);
            StringBuilder sbff = new StringBuilder();
            for (String kk : paramsStr) {
                String value = params.get(kk).toString();
                if ("".equals(sbff.toString())) {
                    sbff.append(kk + "=" + value);
                } else {
                    sbff.append("&" + kk + "=" + value);
                }
            }
            //加上key值
            signatures = getMD5(sbff.toString()).toUpperCase();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return signatures;
    }

    public static void main(String[] args) {
        String str = "12345618888888888";
        String pass = Md5Utils.getMD5(str);
        System.out.println(pass);
    }
}
