package com.xsy.documentduplicatechecking.consumer;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.xsy.documentduplicatechecking.utils.CodeUtils;
import com.xsy.documentduplicatechecking.utils.RedisUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author xushiyue
 * @date 2019年8月1日09:31:24
 */
@Component
public class RegSendMsgListener {

    @Value("${accessKeyId}")
    private String accessKeyId;

    @Value("${accessKeySecret}")
    private String accessKeySecret;

    @Value("${templateCode}")
    private String templateCode;

    private static final Random RANDOM = new Random();

    @Resource
    private RedisUtil redisUtil;

    @KafkaListener(topics = "reg-send-msg")
    public void listen(ConsumerRecord<?, ?> record) {
        String mobileNo = record.value().toString();
        String code = CodeUtils.getVerificationCode();

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobileNo);
        request.putQueryParameter("SignName", "hduxsy查重服务");
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        System.out.println("发送短信" + mobileNo + "验证码" + code);
        redisUtil.setMinute("mobile-" + mobileNo, code, 5L);
        /*try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }*/
    }


}
