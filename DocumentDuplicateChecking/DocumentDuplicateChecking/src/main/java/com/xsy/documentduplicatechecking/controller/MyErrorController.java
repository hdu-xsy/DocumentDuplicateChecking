package com.xsy.documentduplicatechecking.controller;

import com.xsy.documentduplicatechecking.dto.Response;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xushiyue
 * @date 2019年7月30日11:01:50
 */

@RestController
@RequestMapping("/error")
public class MyErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    @GetMapping
    public Response error() {
        return Response.error("err");
    }
}
