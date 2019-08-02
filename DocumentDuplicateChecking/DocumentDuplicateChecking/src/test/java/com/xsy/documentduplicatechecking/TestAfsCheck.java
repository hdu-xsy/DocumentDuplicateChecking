package com.xsy.documentduplicatechecking;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TestAfsCheck {

    IAcsClient client = null;

    @Value("${accessKeyId}")
    private String accessKeyId;

    @Value("${accessKeySecret}")
    private String accessKeySecret;

    @BeforeClass
    public void setUp() throws Exception {

        //YOUR ACCESS_KEY、YOUR ACCESS_SECRET请替换成您的阿里云accesskey id和secret
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        client = new DefaultAcsClient(profile);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "afs", "afs.aliyuncs.com");
    }

    @Test
    public void test() {
        AuthenticateSigRequest request = new AuthenticateSigRequest();
        request.setSessionId("xxx");// 必填参数，从前端获取，不可更改，android和ios只传这个参数即可
        request.setSig("xxx");// 必填参数，从前端获取，不可更改
        request.setToken("xxx");// 必填参数，从前端获取，不可更改
        request.setScene("xxx");// 必填参数，从前端获取，不可更改
        request.setAppKey("xxx");// 必填参数，后端填写CF_APP_1
        request.setRemoteIp("xxx");// 必填参数，后端填写

        try {
            AuthenticateSigResponse response = client.getAcsResponse(request);
            if (response.getCode() == 100) {
                System.out.println("验签通过");
            } else {
                System.out.println("验签失败");
            }
            // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}