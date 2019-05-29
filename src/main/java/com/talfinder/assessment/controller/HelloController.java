package com.talfinder.assessment.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class HelloController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String sayHello() {
    return "Hello, Welcome to Java SpringBoot MySQL assessment";
  }
}
