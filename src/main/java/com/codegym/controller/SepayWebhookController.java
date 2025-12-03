package com.codegym.controller;

import com.codegym.service.SepayService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sepay")
@RequiredArgsConstructor
public class SepayWebhookController {

    private final SepayService sepayService;

    @Value("${sepay.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public String webhook(
            @RequestHeader("Authorization") String authorization,
            @RequestBody String rawBody
    ) {
        String expected = "Apikey " + webhookSecret;

        if (!authorization.equals(expected)) {
            return "INVALID_SECRET";
        }

        Map<String, Object> payload = new Gson().fromJson(rawBody, Map.class);
        sepayService.processTopup(payload);
        return "OK";
    }


}
