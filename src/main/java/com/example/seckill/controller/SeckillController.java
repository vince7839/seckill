package com.example.seckill.controller;

import com.example.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SeckillController {

    static Logger logger = LoggerFactory.getLogger(SeckillController.class);
    @Autowired
    SeckillService seckillService;

    @RequestMapping("/seckill")
    @ResponseBody
    public String seckill() throws Exception{
        logger.info("seckill");
        seckillService.seckill(1);
        return "";
    }

}
