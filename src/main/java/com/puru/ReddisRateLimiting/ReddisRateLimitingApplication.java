package com.puru.ReddisRateLimiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ReddisRateLimitingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReddisRateLimitingApplication.class, args);
	}

}
