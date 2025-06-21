package com.example.chatbot.controller;

import com.example.chatbot.service.OpenAIService;
import com.example.chatbot.utils.GroqParser;
import com.example.chatbot.utils.MarkDownUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController implements ErrorController {

    @Autowired
    private OpenAIService openAIService;

    @GetMapping("/")
    public String index(){
        return "chat";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam("message") String message, Model model){
        String response =openAIService.askGPT(message);
//        String html = response.replaceAll("\n", "<br>");
        model.addAttribute("response", MarkDownUtil.renderMarkdownToHtml(GroqParser.extractAssistantMessage(response)));
        return "chat";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "An unexpected error occurred.";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 404 -> message = "The page you're looking for doesn't exist.";
                case 500 -> message = "Internal server error. Please try again.";
                case 403 -> message = "Access denied.";
                case 400 -> message = "Bad request.";
                default -> message = "Unexpected error (Code: " + statusCode + ")";
            }
        }

        model.addAttribute("errorMessage", message);
        return "error";
    }
}
