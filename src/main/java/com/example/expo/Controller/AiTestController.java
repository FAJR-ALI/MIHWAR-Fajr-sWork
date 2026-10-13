        package com.example.expo.Controller;

import com.example.expo.Client.OpenRouterClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiTestController {

    private final OpenRouterClient openRouterClient;

    @GetMapping("/test")
    public String testAi() {

        return openRouterClient.sendPrompt(
                "You are a test assistant. Follow the user's request exactly.",
                "Reply with exactly this text: AI connection successful"
        );
    }
}
