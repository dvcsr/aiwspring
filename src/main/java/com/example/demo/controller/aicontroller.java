package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // Change to your Angular app's URL in production
public class aicontroller {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${mistral.api.key}")
    private String mistralApiKey;

    @Value("${mistral.api.url}")
    private String mistralApiUrl;

    @PostMapping("/ask-color-gpt")
    public ResponseEntity<String> askColor(@RequestBody Map<String, String> payload) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Build the OpenAI payload
        String body = "{\n" +
                "  \"model\": \"openai/gpt-4o\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"You are a friendly teacher helping a child learn colors. The current color being shown is " + payload.get("currentColor") + ". Be encouraging, patient, and educational. If the child is correct, praise them and maybe teach them something interesting about the color. If they're wrong, gently correct them and help them learn. Keep responses short and child-friendly.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"The child answered: \\\"" + payload.get("userAnswer") + "\\\" when asked about the color " + payload.get("currentColor") + ".\"}\n" +
                "  ],\n" +
                "  \"max_tokens\": 150,\n" +
                "  \"temperature\": 0.7\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
        System.out.println("gpt api hit");
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @PostMapping("/ask-mistral")
    public ResponseEntity<String> askMistral(@RequestBody Map<String, String> payload) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + mistralApiKey);

        // Build the Mistral AI payload
        String body = "{\n" +
                "  \"model\": \"mistral-large-latest\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"You are a creative and enthusiastic teacher helping a child learn colors. The current color being shown is " + payload.get("currentColor") + ". Be imaginative, encouraging, and fun. If the child is correct, celebrate with them and share a cool fact or story about the color. If they're wrong, guide them gently with hints and make learning enjoyable. Keep responses engaging and child-friendly.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"The child answered: \\\"" + payload.get("userAnswer") + "\\\" when asked about the color " + payload.get("currentColor") + ".\"}\n" +
                "  ],\n" +
                "  \"max_tokens\": 150,\n" +
                "  \"temperature\": 0.8\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(mistralApiUrl, entity, String.class);
        System.out.println("mistral api hit");
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
