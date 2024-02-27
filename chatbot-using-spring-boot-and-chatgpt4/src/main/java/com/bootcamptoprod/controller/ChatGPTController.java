package com.bootcamptoprod.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamptoprod.model.request.ChatBotInputRequest;
import com.bootcamptoprod.model.response.ChatGPTResponse;
import com.bootcamptoprod.service.ChatGPTService;

@Controller
@RequestMapping("/chat")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;
    
    private final List<String> chatHistory = new ArrayList<>();

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    // RESTful endpoint for API communication
    @PostMapping("/api")
    public ResponseEntity<ChatGPTResponse> processInputRequest(@RequestBody ChatBotInputRequest chatbotInputRequest) {
        ChatGPTResponse chatCPTResponse = chatGPTService.getChatCPTResponse(chatbotInputRequest.getMessage());
        return new ResponseEntity<>(chatCPTResponse, HttpStatus.OK);
    }

    // Controller method for rendering the chat page with Thymeleaf
    @GetMapping("/ui")
    public String showChatPageUI(Model model) {
        // Pass the chat history to the template in the correct order
        model.addAttribute("chatHistory", chatHistory);
        return "index";
    }

    @PostMapping("/ui")
    public String processQuestion(@RequestParam String question, Model model) {
        // Get the answer from ChatGPT
        String answer = chatGPTService.getChatCPTResponse(question).getChoices().get(0).getMessage().getContent();

        // Add the question and answer to the chat history in the correct order
        chatHistory.add("You: " + question);
        chatHistory.add("ChatBot: " + answer);

        // Pass the updated chat history to the template in the correct order
        model.addAttribute("chatHistory", chatHistory);
        return "index";
    }


}
