package com.xsy.documentduplicatechecking.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * @author from search
 **/

public class DateUtil {

    private DateUtil() {
    }

    /**
     * 解析字符串成java.util.Date 使用pattern
     *
     * @param str :
     * @return date
     */
    static Date parse(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return DateUtils.parseDate(str, "yyyyMMdd");
        } catch (ParseException e) {
            throw new IllegalArgumentException("Can't parse " + str + " using " + "yyyyMMdd");
        }
    }
}


