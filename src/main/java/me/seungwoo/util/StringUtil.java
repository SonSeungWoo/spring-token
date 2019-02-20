package me.seungwoo.util;

import java.util.UUID;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-20
 * Time: 18:04
 */
public class StringUtil {
    public static String randomUUID(){
        String uuid = UUID.randomUUID().toString().replace("-","");
        return uuid;
    }
}
