package com.xsy.documentduplicatechecking.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class MD5Util {

    private static ThreadLocal<MessageDigest> messageDigestHolder = new ThreadLocal<MessageDigest>();

    static Logger log = LoggerFactory.getLogger(MD5Util.class);

    // 用来将字节转换成 16 进制表示的字符
    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    // 盐字节数组大小
    static final int SALT_LENGTH = 3;

    static {
        try {
            MessageDigest message = MessageDigest.getInstance("MD5");
            messageDigestHolder.set(message);
        } catch (NoSuchAlgorithmException e) {
            log.error("初始化java.security.MessageDigest失败:" + StackTraceUtil.getStackTrace(e), e);
        }
    }

    /***
     *
     * @Title: getMD5Format
     * @Description: 计算MD5并转换为32字节明文显示串
     * @author wujl
     * @param data
     * @return String 返回类型
     */
    public static String getMD5Format(String data) {
        try {
            MessageDigest message = messageDigestHolder.get();
            if (message == null) {
                message = MessageDigest.getInstance("MD5");
                messageDigestHolder.set(message);
            }
            message.update(data.getBytes());
            byte[] b = message.digest();

            String digestHexStr = "";
            for (int i = 0; i < 16; i++) {
                digestHexStr += byteHEX(b[i]);
            }

            return digestHexStr;
        } catch (Exception e) {
            log.error("MD5格式化时发生异常[{}]: {}", data, StackTraceUtil.getStackTrace(e));
            return null;
        }
    }

    public static String getMD5Format(byte[] data) {
        try {
            MessageDigest message = messageDigestHolder.get();
            if (message == null) {
                message = MessageDigest.getInstance("MD5");
                messageDigestHolder.set(message);
            }

            message.update(data);
            byte[] b = message.digest();

            String digestHexStr = "";
            for (int i = 0; i < 16; i++) {
                digestHexStr += byteHEX(b[i]);
            }

            return digestHexStr;
        } catch (Exception e) {
            return null;
        }
    }

    /***
     *
     * @Title: byteHEX
     * @Description:
     * @author wujl
     * @param ib
     * @return String 返回类型
     */
    private static String byteHEX(byte ib) {
        char[] ob = new char[2];
        ob[0] = hexDigits[(ib >>> 4) & 0X0F];
        ob[1] = hexDigits[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    /**
     * @return
     * @Title: getSaltString
     * @Description: 获取随机盐值（6个字符）
     */
    public static String getSaltString() {
        SecureRandom sc = new SecureRandom();
        byte[] saltBytes = new byte[3];
        sc.nextBytes(saltBytes);
        String salt = "";
        for (int i = 0; i < saltBytes.length; i++) {
            salt += byteHEX(saltBytes[i]);
        }
        return salt;
    }

    /**
     * @param data
     * @param salt
     * @return
     * @Title: getMD5FormatForDiscuz
     * @Description: 获取discuz用户密码加密字符串
     */
    public static String getMD5FormatForDiscuz(String data, String salt) {
        return getMD5Format(getMD5Format(data) + salt);
    }

    public static String getMD5FormatWithSalt(String md5Data, String salt) {
        return getMD5Format(md5Data + salt);
    }


    public static void main(String[] args) {
        //push: 20150202300002513?tm=20150127130000&sign=b5a165342a3065a97ea5c1acba6dec57
        //play: 20150202300002513?tm=20150127130000&sign=1ba90e68797edccada6a07c0a8c46f3f
        //a845d386c1428b304ae211f4e97562ef
        //System.out.println(MD5Util.getMD5Format("20150202300002513" + "20150127130000" + "738d71338de206763fb11334bcdcd60a"));
        //System.out.println(MD5Util.getMD5Format("20150202300002513" + "20150127130000" + "738d71338de206763fb11334bcdcd60a" + "ucloud"));
        //System.out.println(MD5Util.getMD5FormatForDiscuz("fuckall", "fuck"));
//		String usernames = "live1@ekb.com,live2@ekb.com,live3@ekb.com,live4@ekb.com,live5@ekb.com,live6@ekb.com,live7@ekb.com,live8@ekb.com,live9@ekb.com,live10@ekb.com,live11@ekb.com,live12@ekb.com,live13@ekb.com,live14@ekb.com,live15@ekb.com";
//
//		String passwords = "live131,live163,live127,live980,live989,live847,live204,live510,live204,live195,live677,live347,live782,live833,live908";
//
//		String[] names = usernames.split(",");
//		String[] pwds = passwords.split(",");
//
//
//		for(int i=0; i<names.length ; i++){
//			String salt = getSaltString();
//			String password = getMD5Format(getMD5Format(pwds[i]) + salt);
//			String name = names[i];
//			System.out.println("update client_user set password = '"+password+"' , salt = '"+salt+"' where e_mail = '"+name+"';");
//			726083
//		}903dcd78696b99525d7647e5edaedbcc
//		19a285e240f1e80ca05f266fdcd65dea  362e75
//		9cd72c

        //old 8794344564e9c0ac461cab872b02e2f7
        String salt = "688190";//getSaltString();
//		System.out.println(salt);
        String password = "123456";
        System.out.println(getMD5Format(getMD5Format(password) + salt));
//		System.out.println(getMD5Format("123456"));
//		SecureRandom sc = new SecureRandom();
//		long aa = sc.nextInt(1000);
//		System.out.println(aa);

    }

}
