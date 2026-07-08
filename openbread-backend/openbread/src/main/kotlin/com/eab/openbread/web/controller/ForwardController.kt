package com.eab.openbread.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ForwardController {
    @RequestMapping(value = ["/{path:[^\\.]*}", "/**/{path:[^\\.]*}"])
    fun forward(): String {
        return "forward:/"
    }
}