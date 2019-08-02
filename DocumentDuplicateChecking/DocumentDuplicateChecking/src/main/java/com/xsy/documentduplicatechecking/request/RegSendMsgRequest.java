package com.xsy.documentduplicatechecking.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author xushiyue
 * @date 2019年7月31日17:19:27
 */
@Data
public class RegSendMsgRequest {

    @JsonProperty("mobileNo")
    private String mobileNo;

    @JsonProperty("token")
    private String token;

    @JsonProperty("scene")
    private String scene;

    @JsonProperty("sig")
    private String sig;

    @JsonProperty(value = "sessionId")
    private String sessionId;
}
