package com.xsy.documentduplicatechecking.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author from search
 * 验证工具类
 */
public class ValidateUtil {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^(1)\\d{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{4,16}$");
    /**
     * 大陆地区地域编码最大值
     **/
    private static final int MAX_MAINLAND_AREA_CODE = 659004;
    /**
     * 大陆地区地域编码最小值
     **/
    private static final int MIN_MAINLAND_AREA_CODE = 110000;
    /**
     * 香港地域编码值
     **/
    private static final int HONGKONG_AREA_CODE = 810000;
    /**
     * 台湾地域编码值
     **/
    private static final int TAIWAN_AREA_CODE = 710000;
    /**
     * 澳门地域编码值
     **/
    private static final int MACAO_AREA_CODE = 820000;
    /**
     * 数字正则
     **/
    private static final String REGEX_NUM = "^[0-9]*$";
    /**
     * 闰年生日正则
     **/
    private static final String REGEX_BIRTHDAY_IN_LEAP_YEAR = "^((19[0-9]{2})|(20[0-9]{2}))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))$";
    /**
     * 平年生日正则
     **/
    private static final String REGEX_BIRTHDAY_IN_COMMON_YEAR = "^((19[0-9]{2})|(20[0-9]{2}))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))$";

    private ValidateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 验证手机号是否合法
     *
     * @param mobiles :
     * @return boolean
     */
    public static boolean isMobileNo(String mobiles) {
        if (StringUtils.isBlank(mobiles)) {
            return false;
        }
        Matcher mobileMatcher = MOBILE_PATTERN.matcher(mobiles);
        return mobileMatcher.matches();
    }

    /**
     * 验证邮箱号是否合法
     *
     * @param email :
     * @return boolean
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        return emailMatcher.matches();
    }

    /**
     * 验证身份证是否合法.
     *
     * @param identityNum :
     * @return boolean
     * 身份证格式强校验
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码， 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     * 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S =
     * Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5
     * 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     */
    public static boolean isIdentityNum(String identityNum) {

        if (StringUtils.isBlank(identityNum)) {
            return false;
        }
        String trimIdentityNum = identityNum.trim();

        if (!checkIdNumberRegex(trimIdentityNum)) {
            return false;
        }
        if (!checkIdNumberArea(trimIdentityNum.substring(0, 6))) {
            return false;
        }
        String newTrimIdentityNum = convertFifteenToEighteen(trimIdentityNum);
        if (!checkBirthday(newTrimIdentityNum.substring(6, 14))) {
            return false;
        }
        if (!checkIdNumberVerifyCode(newTrimIdentityNum)) {
            return false;
        }
        return true;
    }

    /**
     * 身份证正则校验
     */
    public static boolean checkIdNumberRegex(String idNumber) {
        return Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", idNumber);
    }

    /**
     * 身份证地区码检查
     */
    private static boolean checkIdNumberArea(String idNumberArea) {
        int areaCode = Integer.parseInt(idNumberArea);
        if (areaCode == HONGKONG_AREA_CODE || areaCode == MACAO_AREA_CODE || areaCode == TAIWAN_AREA_CODE) {
            return true;
        } else {
            return (areaCode <= MAX_MAINLAND_AREA_CODE && areaCode >= MIN_MAINLAND_AREA_CODE);
        }
    }

    /**
     * 将15位身份证转换为18位
     */
    private static String convertFifteenToEighteen(String idNumber) {
        if (15 != idNumber.length()) {
            return idNumber;
        }

        String newIdNumber = idNumber.substring(0, 6) + "19" + idNumber.substring(6, 15);
        newIdNumber = newIdNumber + getVerifyCode(newIdNumber);
        return newIdNumber;
    }

    /**
     * 根据身份证前17位计算身份证校验码
     */
    private static String getVerifyCode(String idNumber) {
        if (!Pattern.matches(REGEX_NUM, idNumber.substring(0, 17))) {
            return "";
        }
        String[] valCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(idNumber.charAt(i))) * Integer.parseInt(wi[i]);
        }
        return valCodeArr[sum % 11];
    }

    /**
     * 身份证出生日期嘛检查
     */
    private static boolean checkBirthday(String idNumberBirthdayStr) {
        int year = Integer.parseInt(idNumberBirthdayStr.substring(0, 4));

        Date birthday = DateUtil.parse(idNumberBirthdayStr);
        Date now = new Date();
        if (birthday != null && birthday.getTime() > now.getTime()) {
            return false;
        }

        if (isLeapYear(year)) {
            return Pattern.matches(REGEX_BIRTHDAY_IN_LEAP_YEAR, idNumberBirthdayStr);
        } else {
            return Pattern.matches(REGEX_BIRTHDAY_IN_COMMON_YEAR, idNumberBirthdayStr);
        }
    }

    /**
     * 判断是否为闰年
     */
    private static boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 100 != 0 && year % 4 == 0);
    }

    /**
     * 身份证校验码检查
     */
    private static boolean checkIdNumberVerifyCode(String idNumber) {
        return getVerifyCode(idNumber).equalsIgnoreCase(idNumber.substring(17));
    }

    /**
     * 根据身份证号码计算年龄
     *
     * @param idNo :
     * @return integer
     */
    public static Integer getAgeByIdNo(String idNo) {
        if (StringUtils.isBlank(idNo)) {
            return null;
        }
        int length = idNo.length();
        if (length == 18) {
            String dates = idNo.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            int age = Integer.parseInt(year) - Integer.parseInt(dates);
            return age;
        } else {
            String dates = idNo.substring(6, 8);
            return Integer.parseInt(dates);
        }
    }

    public static String getIp(HttpServletRequest httpRequest) {
        String ip = httpRequest.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getRemoteAddr();
        }
        return ip;
    }

    public static boolean isPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        Matcher emailMatcher = USERNAME_PATTERN.matcher(password);
        return emailMatcher.matches();
    }
}
