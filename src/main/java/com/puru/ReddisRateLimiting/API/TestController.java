package com.puru.ReddisRateLimiting.API;

import com.puru.ReddisRateLimiting.Filter.RateLimit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class TestController {

    // Define the TestData class as static
    public static class TestData {
        String data;
        Date date_fetched_when;

        public TestData(String data) {
            this.data = data;
            this.date_fetched_when = new Date();
        }

        // Getters and setters (optional, depending on your need)
        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Date getDate_fetched_when() {
            return date_fetched_when;
        }

        public void setDate_fetched_when(Date date_fetched_when) {
            this.date_fetched_when = date_fetched_when;
        }
    }

    @GetMapping(value = "/getTestData")
    @RateLimit
    public ResponseEntity<TestData> getTestData(@RequestParam(name = "UserName") String userName) {
        // You might want to add validation or defaults for UserName
        if (userName == null || userName.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TestData("UserName is required"));
        }

        // Return response with sample data and timestamp
        TestData testData = new TestData("Sample Data with TimeStamp");
        return ResponseEntity.status(HttpStatus.OK).body(testData);
    }
}
