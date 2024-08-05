package com.varughese.tracktracker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class StaticController {

    @GetMapping("/{path:[^\\.]*}")
    fun redirect(): String {
        // Redirect all non-API requests to the index.html for React routing
        return "forward:/index.html"
    }
}