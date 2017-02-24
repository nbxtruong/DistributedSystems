package com.drivecloud.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Listfile1 {

    @RequestMapping(value = "/abc1", method = RequestMethod.GET)
    public
    @ResponseBody
    String printWelcome() {
        return "{\"records\":[{\"Name\":\"Alfreds Futterkiste\"}]}";
    }
}
