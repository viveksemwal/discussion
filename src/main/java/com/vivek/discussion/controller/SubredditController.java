package com.vivek.discussion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subreddit")
public class SubredditController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return new ResponseEntity<>("User is authenticated", HttpStatus.OK);
    }
}
