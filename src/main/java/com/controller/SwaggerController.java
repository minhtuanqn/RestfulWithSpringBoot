package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller layer for get api document resource
 */
@Controller
@RequestMapping(path = "/swagger")
public class SwaggerController {
    /**
     * Redirect api documentation
     * @param model
     * @return name of index file
     */
    @GetMapping(path = "")
    public String redirectAPIDoc(Model model) {
        return "index";
    }
}
