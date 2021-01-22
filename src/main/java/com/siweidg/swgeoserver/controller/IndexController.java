package com.siweidg.swgeoserver.controller;

import com.siweidg.swgeoserver.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

//    @GetMapping("/")
//    public String index() {
//        return "/build/index.html";
//    }




    @GetMapping("/swagger")
    public String swaggerIndex() {
        System.out.println("swagger-ui.html");
        return "redirect:swagger-ui.html";
    }
}
