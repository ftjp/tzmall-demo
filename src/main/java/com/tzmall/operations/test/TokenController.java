package com.tzmall.operations.test;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenBucket tokenBucket;

    @GetMapping("/api/token")
    public String getToken() {
        if (tokenBucket.tryConsume()) {
            return "成功获取令牌";
        } else {
            return "令牌桶已空，无法获取令牌";
        }
    }
}