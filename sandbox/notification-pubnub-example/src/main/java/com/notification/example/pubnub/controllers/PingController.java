package com.notification.example.pubnub.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {

  @GetMapping
  public ResponseEntity<String> ping() {
    return new ResponseEntity<>("ping success", HttpStatus.OK);
  }
}
