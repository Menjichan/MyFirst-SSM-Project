package com.menjilearn.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author menji
 * @version 1.0
 * @date 2022/6/23
 */
@Controller
public class WorkbenchMainController {

    @RequestMapping("/workbench/main/index.do")
    public String mainIndex() {
        return "workbench/main/index";
    }
}
