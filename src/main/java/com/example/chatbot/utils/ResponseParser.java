package com.example.chatbot.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class ResponseParser {
    public static String extractAssistantMessage(String jsonResponse) {
        JSONObject obj = new JSONObject(jsonResponse);
        JSONArray choices = obj.getJSONArray("choices");
        JSONObject choice0 = choices.getJSONObject(0);
        JSONObject message = choice0.getJSONObject("message");
        return message.getString("content");
    }
}
