package com.hackx.web.controller;

import com.hackx.web.service.MemberService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private static Logger logger = Logger.getLogger(MemberController.class);

    @Autowired
    MemberService memberService;

    @RequestMapping("/")
    String home() {
        return memberService.queryById(1L).toString();
    }

}
