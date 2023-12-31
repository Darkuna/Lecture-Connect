package com.lecture.coordinator.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class RootRedirectController {
    @GetMapping({"/", "index.html", "index.xhtml"})
    public RedirectView redirectWithUsingRedirectView() {
        return new RedirectView("login.xhtml");
    }
}
