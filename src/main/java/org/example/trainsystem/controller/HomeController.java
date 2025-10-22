package org.example.trainsystem.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {

        return "index";
    }

    @GetMapping("/chatbot")
    public String showChatbotPage() {
        return "passenger/chatbot";
    }
}
