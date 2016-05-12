package com.springmvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.springmvc.exceptions.HttpUnauthorizedException;

@ControllerAdvice // To Handle Exceptions
public class ExceptionController {
     //// ...........

     @ExceptionHandler({HttpUnauthorizedException.class})
     @ResponseBody
     @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
     Map<String, String> unauthorizedAccess(Exception e) {
         Map<String, String> exception = new HashMap<String, String>();
         exception.put("code", "401");
         exception.put("reason", e.getMessage());
         return exception;
     }
}