package com.xsy.documentduplicatechecking.controller;

import com.xsy.documentduplicatechecking.dto.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xushiyue
 * @date 2019年7月30日11:06:11
 */
@RestController
public class ExceptionController {

    @ExceptionHandler({Throwable.class})
    @GetMapping("/exception")
    public Response error(Exception e) {
        return Response.error("exception : " + e.getLocalizedMessage());
    }

}
