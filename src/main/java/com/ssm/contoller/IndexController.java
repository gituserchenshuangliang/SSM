package com.ssm.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @description:首页控制
 * @author: Cherry
 * @time: 2020/6/17 9:45
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("grap");
        return mav;
    }
}
