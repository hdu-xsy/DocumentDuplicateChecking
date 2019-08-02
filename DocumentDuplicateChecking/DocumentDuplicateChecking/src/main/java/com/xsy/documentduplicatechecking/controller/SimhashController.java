package com.xsy.documentduplicatechecking.controller;

import com.xsy.documentduplicatechecking.dto.Response;
import com.xsy.documentduplicatechecking.entity.HostHolder;
import com.xsy.documentduplicatechecking.serivce.SimhashService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author xushiyue
 * @date 2019年7月30日10:17:47
 */
@RestController
@RequestMapping("/simhash")
public class SimhashController {

    @Resource(name = "simhashService")
    private SimhashService simhashService;

    @Resource
    private HostHolder hostHolder;

    @GetMapping("/")
    public Response examination() {
        return Response.ok(hostHolder.getUser());
    }

}
