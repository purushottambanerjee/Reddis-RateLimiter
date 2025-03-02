package com.puru.ReddisRateLimiting.ExceptionHandling;


public class RateLimitException extends RuntimeException{

    public RateLimitException(String message){
        super(message);
    }
}
