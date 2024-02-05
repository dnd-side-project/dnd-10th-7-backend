package com.sendback.domain.healthCheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HealthCheckController {

    @GetMapping()
    public String checkHealth() {
        return "✅ server is running";
    }

}
