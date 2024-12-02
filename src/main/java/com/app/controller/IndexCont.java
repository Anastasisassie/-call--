package com.app.controller;

import com.app.controller.global.Global;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexCont extends Global {

    @GetMapping
    public String index1(Model model) {
        addAttributes(model);
        return "index";
    }

    @GetMapping("/index")
    public String index2(Model model) {
        addAttributes(model);
        return "index";
    }

}
