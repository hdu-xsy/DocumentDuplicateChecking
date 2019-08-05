package com.xsy.documentduplicatechecking.serivce.impl;

import com.xsy.documentduplicatechecking.bo.SysUserBo;
import com.xsy.documentduplicatechecking.common.constant.USER_TYPE;
import com.xsy.documentduplicatechecking.entity.SysUser;
import com.xsy.documentduplicatechecking.exception.NotValidException;
import com.xsy.documentduplicatechecking.helper.SysUserHelper;
import com.xsy.documentduplicatechecking.repository.SysUserRepository;
import com.xsy.documentduplicatechecking.serivce.SysUserService;
import com.xsy.documentduplicatechecking.utils.MD5Util;
import com.xsy.documentduplicatechecking.utils.RedisUtil;
import com.xsy.documentduplicatechecking.utils.ValidateUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xushiyue
 * @date 2019年7月30日15:45:09
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SysUserRepository sysUserRepository;

    @Override
    public String findByToken(String token) {
        return redisUtil.get(token);
    }

    @Override
    public String findCodeByMobileNo(String mobileNo) {
        return redisUtil.get("mobile-" + mobileNo);
    }

    @Override
    public void removeCode(String mobileNo) {
        redisUtil.del("mobile-" + mobileNo);
    }

    @Override
    @Transactional(rollbackOn = Throwable.class)
    public void saveUser(SysUserBo userBo) throws NotValidException {
        SysUser user = SysUserHelper.convertToSysUser(userBo);

        if (ValidateUtil.isPassword(user.getPassword())) {
            String salt = MD5Util.getSaltString();
            user.setSalt(salt);
            String password = MD5Util.getMD5Format(MD5Util.getMD5Format(user.getPassword()) + user.getSalt());
            user.setPassword(password);
        } else {
            throw new NotValidException("密码格式错误");
        }

        if (ValidateUtil.isMobileNo(user.getMobileNo())) {
            user.setUsername(user.getMobileNo());
            if (user.getNickname().isEmpty()) {
                String nickName = user.getMobileNo().substring(0, 3) + "****" + user.getMobileNo().substring(7);
                user.setNickname(nickName);
            }
        } else {
            throw new NotValidException("手机号码错误");
        }

        //TODO:验证邮箱

        user.setIsDisabled(false);
        user.setUserType(USER_TYPE.NORMAL_USER.ordinal());
        this.sysUserRepository.save(user);
        sysUserRepository.flush();
    }

    @Override
    public String loginUser(String username, String password) throws LoginException {

        //判断登陆字段类型，根据用户名类型来进行登陆
        SysUser user = null;
        if (ValidateUtil.isMobileNo(username)) {
            //根据手机号进行匹配
            user = this.sysUserRepository.findByMobileNo(username);
        } else if (ValidateUtil.isEmail(username)) {
            //根据email进行匹配
            user = this.sysUserRepository.findByEmail(username);
        } else {
            //根据用户名进行匹配
            user = this.sysUserRepository.findByUsername(username);
        }
        if (user == null) {
            throw new LoginException("用户名不存在");
        }
        //
        if (user.getIsDisabled()) {
            throw new LoginException("该用户已被禁用，禁止登录");
        }
        if (user.getPassword().equals(MD5Util.getMD5Format(MD5Util.getMD5Format(password) + user.getSalt()))) {
            String token =  createToken(user);
            redisUtil.setHour(token,user.getId().toString(),24L);
            return token;
        } else {
            throw new LoginException("用户名或密码错误");
        }

    }

    private String createToken(SysUser user) {
       return UUID.randomUUID().toString().replace("-","_");
    }


    public static final String JWT_SECRET = "7786ff7fc3a34e26a61c054d5ec8245d";

    /**
     * jwtToken有效时间  millisecond
     */
    public static final long JWT_TTL = 365 * 24 * 60 * 60 * 1000L;
    /**
     * jwtToken有效刷新时间 millisecond
     */
    public static final long JWT_REFRESH_TTL = 7 * 24 * 60 * 60 * 1000L;

//    private String createToken(SysUser user) throws LoginException {
//        if (user == null) {
//            throw new LoginException("");
//        }
//        if (user.getIsDisabled()) {
//            throw new LoginException("该用户已被禁用，禁止登录");
//        }
//        try {
//            Map<String, String> map = new HashMap<>(2);
//            map.put("userId", String.valueOf(user.getId()));
//            map.put("realName", user.getNickname());
//            String subject = map.toString();
//            return createJwt(UUID.randomUUID().toString().replace("-", ""), subject, JWT_TTL);
//        } catch (Exception e) {
//            throw new LoginException(e.getLocalizedMessage());
//        }
//    }
//
//    public static String createJwt(String id, String subject, long ttlMillis) {
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//        SecretKey key = generalKey();
//        Map<String, Object> map = new HashMap<>(1);
//        String refreshJwt = createRefreshJwt(id, subject, JWT_REFRESH_TTL);
//        map.put("rt", refreshJwt);
//        JwtBuilder builder = Jwts.builder()
//                .setHeaderParam("typ", "JWT")
//                .setClaims(map)
//                .setId(id)
//                .setIssuedAt(now)
//                .setSubject(subject)
//                .signWith(key,signatureAlgorithm);
//        if (ttlMillis >= 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }
//        return builder.compact();
//    }
//
//    /**
//     * 创建刷新token
//     *
//     * @param id        tokenId
//     * @param ttlMillis 有效时间
//     * @return String
//     */
//    public static String createRefreshJwt(String id, String subject, long ttlMillis) {
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//        SecretKey key = generalKey();
//        JwtBuilder builder = Jwts.builder()
//                .setHeaderParam("typ", "JWT")
//                .setId(id)
//                .setIssuedAt(now)
//                .setSubject(subject)
//                .signWith(key,signatureAlgorithm);
//        if (ttlMillis >= 0) {
//            long expMillis = nowMillis + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }
//        return builder.compact();
//    }
//
//    /**
//     * 由字符串生成加密key
//     *
//     * @return
//     */
//    public static SecretKey generalKey() {
//        byte[] encodedKey = Base64.decodeBase64(JWT_SECRET);
//        return new SecretKeySpec(encodedKey, 0, encodedKey.length, );
//    }

}
