package com.hilltop.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Aws health controller
 */
@RestController
public class AwsHealthController {

    /**
     * Used to check if hosted service is running.
     *
     * @return message string.
     */
    @GetMapping("/")
    public String ping() {
        return "This is hill-top-user service !!!";
    }
}
