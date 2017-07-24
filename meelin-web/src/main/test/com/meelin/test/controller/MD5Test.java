package com.meelin.test.controller;

import com.meelin.core.model.SUser;
import com.meelin.core.util.JsonUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p/>类文件: com.meelin.test.controller
 * <p/>类功能描述: ${todo}
 *
 * @作者: luj
 * @时间: 2017/1/18 10:59
 */
public class MD5Test {
    public static void main(String[] args){
//        String source = "11111";
//        String salt = "qwertyw";
//        int hashIterations = 1;
//        Md5Hash md5 = new Md5Hash(source, salt);
//
//        System.out.println(md5.toString());
//        SimpleHash simpleHash = new SimpleHash("md5", source, salt, hashIterations);
//        System.out.println(simpleHash.toString());
//        System.out.println(MD5(source, salt));

        List<Map<String, Object>> listMap= new ArrayList<Map<String, Object>>();
        Map<String, Object> map= new HashMap<String, Object>();
//        map.put("aaa", "111");
//        map.put("bbb", "222");
//        map.put("ccc", "333");
//        map.put("ddd", "444");
//        listMap.add(map);
        SUser user= new SUser();
        user.setUsername("123");
        user.setPassword("abc");
        map.put("user", user);
        listMap.add(map);
        System.out.println(JsonUtil.toJson(listMap).toString());

    }


    public static String MD5(String input, String salt) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(salt.getBytes("utf-8"));
            byte[] md = mdInst.digest(input.getBytes("utf-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                String shaHex = Integer.toHexString(md[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";

    }
}
