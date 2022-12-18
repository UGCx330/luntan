package com.zzh.luntan.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.digester.Digester;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String MD5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
