package com.demo.project.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class EncodeUtils {

    public static String encode(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            log.error("", e);
            return url;
        }
    }

    public static String decode(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            log.error("", e);
            return url;
        }
    }
}
