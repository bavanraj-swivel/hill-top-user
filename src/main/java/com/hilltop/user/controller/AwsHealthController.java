package com.hilltop.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Aws health controller
 */
@RestController
public class AwsHealthController {
    @GetMapping("/")
    public String ping() {
        return "Welcome to user service !!!";
    }
}
