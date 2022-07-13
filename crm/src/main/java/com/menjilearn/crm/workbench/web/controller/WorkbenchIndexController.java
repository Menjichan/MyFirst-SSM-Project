package com.menjilearn.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/20
 */
@Controller
public class WorkbenchIndexController {

    @RequestMapping("/workbench/index.do")
    public String index() {
        return "workbench/index";
    }
}
