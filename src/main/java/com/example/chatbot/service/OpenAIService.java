package com.example.chatbot.service;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenAIService {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    private final OkHttpClient okHttpClient = new OkHttpClient();

    public String askGPT(String userMessage) {
        String payload = """
                {
                "model":"%s",
                "messages": [
                    {
                        "role":"system",
                        "content":"You are an HR onboarding assistant. Answer questions about onboarding in a friendly and concise manner."
                    },
                    {
                        "role":"user",
                        "content":"%s"
                    }
                   ]
                }""".formatted(model, userMessage);

        logger.info("payload {}", payload);
        Request request = new Request.Builder().url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
//                .addHeader("OpenAI-Organization", "")
//                .addHeader("OpenAI-Project","")
                .post(RequestBody.create(payload, MediaType.get("application/json")))
                .build();

        logger.info("request : {}", request);

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                logger.info("If response : {}", response);
                return response.body().string();
            } else {
                logger.info("Else response : {}", response);
                return "Error : " + response.code();
            }
        } catch (IOException ioException) {
            return "Failed to connect to OpenAI : " + ioException.getMessage();
        }
    }
}
