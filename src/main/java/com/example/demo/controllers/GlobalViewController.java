package com.example.demo.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Scope("session")
@RequestMapping("/api/global")
public class GlobalViewController {

}
