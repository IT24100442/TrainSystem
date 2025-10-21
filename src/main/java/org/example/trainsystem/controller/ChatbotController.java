package org.example.trainsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalTime;

@Controller
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {



    // POST endpoint for chatbot messages
    @PostMapping("/message")
    public ResponseEntity<Map<String, Object>> getChatResponse(@RequestBody Map<String, String> request) {
        try {
            String userMessage = request.get("message");

            // Validate input
            if (userMessage == null || userMessage.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Please enter a message."));
            }

            String messageLower = userMessage.toLowerCase().trim();
            String response = generateResponse(messageLower);

            // Logging
            System.out.println("User said: " + userMessage);
            System.out.println("Bot replied: " + response);

            return ResponseEntity.ok(createSuccessResponse(response));

        } catch (Exception e) {
            System.err.println("Error processing chatbot message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(createErrorResponse("Internal server error"));
        }
    }

    // Optional: GET endpoint for testing
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Chatbot API is working!");
        response.put("endpoint", "/api/chatbot/message");
        response.put("method", "POST");
        return ResponseEntity.ok(response);
    }

    private String generateResponse(String message) {
        // Greetings
        if (containsAny(message, "hello", "hi", "hey", "greetings")) {
            return getGreeting() + " How can I help you with TrainLink today?";
        }

        // Schedule queries
        if (containsAny(message, "schedule", "timetable", "time")) {
            if (containsAny(message, "check", "view", "see", "show")) {
                return "📅 You can view complete train schedules and timings on our Train Schedule page. Would you like to know about a specific route?";
            }
            return "🕐 Our trains run daily from 6:00 AM to 10:00 PM. Check the full schedule for specific train times.";
        }

        // Available trains
        if (containsAny(message, "train") && containsAny(message, "available", "list", "all")) {
            return "🚂 You can browse all available trains, routes, and classes on our View Trains page. Each listing includes departure times, arrival times, and available seats!";
        }

        // Booking
        if (containsAny(message, "book", "ticket", "reservation", "reserve")) {
            if (containsAny(message, "how", "process", "steps")) {
                return "📝 Booking is easy! Just follow these steps:\n1. Visit the booking page\n2. Select your route and date\n3. Choose your preferred class\n4. Complete payment\nNeed help with a specific step?";
            }
            return "🎫 Ready to book? Start your journey on our booking page. You can select from multiple classes and payment options!";
        }

        // Tracking
        if (containsAny(message, "track", "location", "where", "status", "live")) {
            if (containsAny(message, "train", "my")) {
                return "📍 Track your train in real-time on our Live Train Status page. You'll need your PNR number or train number.";
            }
            return "🔍 You can check live train locations and estimated arrival times on our tracking page.";
        }

        // Cancellation
        if (containsAny(message, "cancel", "cancellation")) {
            return "❌ To cancel your booking, go to My Bookings and select the booking you want to cancel. Cancellation charges may apply based on timing.";
        }

        // Refund
        if (containsAny(message, "refund", "money back", "reimbursement")) {
            return "💰 Refunds are processed within 3-5 business days after cancellation. The amount will be credited to your original payment method. Cancellation charges (if any) will be deducted.";
        }

        // Concerns/Complaints
        if (containsAny(message, "concern", "complaint", "problem", "issue", "help", "support")) {
            return "🤝 We're here to help! You can submit your concern or complaint through our support page. Our support team will respond within 24 hours.";
        }

        // Payment
        if (containsAny(message, "payment", "pay", "payment method", "card")) {
            return "💳 We accept multiple payment methods including Credit/Debit Cards, Net Banking, and Digital Wallets. All transactions are secure and encrypted.";
        }

        // Seat availability
        if (containsAny(message, "seat", "available seat", "vacancy")) {
            return "💺 Check real-time seat availability on the View Trains page. You can filter by class and date to see available seats.";
        }

        // Classes
        if (containsAny(message, "class", "first class", "second class", "sleeper", "ac")) {
            return "🎯 We offer multiple travel classes:\n• First Class AC\n• Second Class AC\n• Sleeper Class\n• General Class\nView details and prices on our booking page.";
        }

        // Price/Cost
        if (containsAny(message, "price", "cost", "fare", "how much", "charge")) {
            return "💵 Ticket prices vary based on route, class, and booking time. You can check exact fares on the booking page after selecting your route.";
        }

        // Account related
        if (containsAny(message, "account", "profile", "login", "register", "sign up")) {
            return "👤 You can manage your account, view booking history, and update preferences in your Profile section.";
        }

        // Contact
        if (containsAny(message, "contact", "phone", "email", "reach")) {
            return "📞 Contact us:\n• Email: support@trainlink.com\n• Phone: 1-800-TRAINLINK\n• Or submit a query through our support page";
        }

        // Thank you
        if (containsAny(message, "thank", "thanks", "appreciate")) {
            return "You're very welcome! 😊 Is there anything else I can help you with?";
        }

        // Bye
        if (containsAny(message, "bye", "goodbye", "see you", "exit")) {
            return "Goodbye! 👋 Have a great journey with TrainLink. Come back anytime you need assistance!";
        }

        // Default response with suggestions
        return "🤔 I'm not quite sure about that. Here's what I can help you with:\n" +
                "• 🚂 View available trains\n" +
                "• 🎫 Book tickets\n" +
                "• 📍 Track trains\n" +
                "• ❌ Cancel bookings\n" +
                "• 💰 Refund information\n" +
                "• 🤝 Submit concerns\n" +
                "What would you like to know?";
    }

    private String getGreeting() {
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.NOON)) {
            return "Good morning! ☀️";
        } else if (now.isBefore(LocalTime.of(17, 0))) {
            return "Good afternoon! 👋";
        } else {
            return "Good evening! 🌙";
        }
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> createSuccessResponse(String reply) {
        Map<String, Object> response = new HashMap<>();
        response.put("reply", reply);
        response.put("status", "success");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    private Map<String, Object> createErrorResponse(String error) {
        Map<String, Object> response = new HashMap<>();
        response.put("reply", "Sorry, something went wrong. Please try again.");
        response.put("status", "error");
        response.put("error", error);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}