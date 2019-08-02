package com.xsy.documentduplicatechecking.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.xsy.documentduplicatechecking.dto.Response;
import com.xsy.documentduplicatechecking.exception.NotValidException;
import com.xsy.documentduplicatechecking.helper.SysUserHelper;
import com.xsy.documentduplicatechecking.request.RegRequest;
import com.xsy.documentduplicatechecking.request.RegSendMsgRequest;
import com.xsy.documentduplicatechecking.serivce.SysUserService;
import com.xsy.documentduplicatechecking.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xushiyue
 * @date 2019年7月31日11:04:30
 */
@RestController
public class UserController {

    @Value("${accessKeyId}")
    private String accessKeyId;

    @Value("${accessKeySecret}")
    private String accessKeySecret;

    @Value("${appKey}")
    private String appKey;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private SysUserService sysUserService;

    private static final int AUTH_PASS = 100;

    @GetMapping("/sendRegMsg")
    public JSONPObject sendRegMsg(HttpServletRequest httpRequest, RegSendMsgRequest regRequest) {
        String jsonpCallback = httpRequest.getParameter("callback");

        if (!ValidateUtil.isMobileNo(regRequest.getMobileNo())) {
            return new JSONPObject(jsonpCallback, Response.error("手机号格式错误"));
        }

        //TODO:短信频率过高
        //TODO:重复注册检测

        //YOUR ACCESS_KEY、YOUR ACCESS_SECRET请替换成您的阿里云accesskey id和secret
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "afs", "afs.aliyuncs.com");
        } catch (ClientException e) {
            return new JSONPObject(jsonpCallback, e.getLocalizedMessage());
        }

        AuthenticateSigRequest request = new AuthenticateSigRequest();
        // 必填参数，从前端获取，不可更改，android和ios只传这个参数即可
        request.setSessionId(regRequest.getSessionId());
        // 必填参数，从前端获取，不可更改
        request.setSig(regRequest.getSig());
        // 必填参数，从前端获取，不可更改
        request.setToken(regRequest.getToken());
        // 必填参数，从前端获取，不可更改
        request.setScene(regRequest.getScene());
        // 必填参数，后端填写
        request.setAppKey(appKey);
        // 必填参数，后端填写
        request.setRemoteIp(ValidateUtil.getIp(httpRequest));
        try {
            AuthenticateSigResponse response = client.getAcsResponse(request);
            if (response.getCode() == AUTH_PASS) {
                kafkaTemplate.send("reg-send-msg", regRequest.getMobileNo());
                return new JSONPObject(jsonpCallback, Response.ok("pass"));
            } else {
                System.out.println("验签失败" + regRequest.toString() + response.getMsg());
                return new JSONPObject(jsonpCallback, Response.error("fail"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONPObject(jsonpCallback, Response.error("error"));
        }
    }

    @PostMapping("/reg")
    public Response reg(RegRequest regRequest) {
        String code = sysUserService.findCodeByMobileNo(regRequest.getMobileNo());
        if (code.isEmpty()) {
            return Response.error("null_code");
        }
        if (!code.equals(regRequest.getCode())) {
            return Response.error("wrong_code");
        }
        sysUserService.removeCode(code);
        try {
            sysUserService.saveUser(SysUserHelper.convertToSysUserBo(regRequest));
        } catch (NotValidException e) {
            return Response.error(e.getLocalizedMessage());
        }
        return Response.ok("reg ok");
    }

    @GetMapping("/login")
    public JSONPObject login(HttpServletRequest httpServletRequest, @RequestParam("username") String username, @RequestParam("password") String password) {
        System.out.println(username + password);
        try {
            sysUserService.loginUser(username, password);
        } catch (LoginException e) {
            e.printStackTrace();
        }
        String jsonpCallback = httpServletRequest.getParameter("callback");
        return new JSONPObject(jsonpCallback, "ok");
    }
}
