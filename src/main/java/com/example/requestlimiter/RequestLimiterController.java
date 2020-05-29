package com.example.requestlimiter;


import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller

public class RequestLimiterController
{
    @Resource
    private RequestLimiterService requestLimiterService;

    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public ResponseEntity request(HttpServletRequest request)
    {
        if (requestLimiterService.checkRequestCount(request.getRemoteAddr())) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_GATEWAY);
    }
}
