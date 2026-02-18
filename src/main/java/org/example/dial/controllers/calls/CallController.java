package org.example.dial.controllers.calls;

import java.util.Map;
import org.example.dial.models.Contact;
import org.example.dial.services.ContactsService;
import org.example.dial.utils.JwtGenerator;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CallController {

    private final ContactsService contactsService;
    private final JwtGenerator jwtGenerator;
    private final SimpMessagingTemplate messagingTemplate;

    public CallController(SimpMessagingTemplate messagingTemplate, JwtGenerator jwtGenerator, ContactsService contactsService) {
        this.messagingTemplate = messagingTemplate;
        this.jwtGenerator = jwtGenerator;
        this.contactsService = contactsService;
    }

    @GetMapping("/call")
    public String get(@CookieValue("satisfy") String jwtToken, Model model) {
        String name = this.jwtGenerator.parseName(jwtToken);
        Iterable<Contact> contacts = this.contactsService.getMyAll(name);
        model.addAttribute("name", name);
        model.addAttribute("contacts", contacts);
        return "call";
    }

    // 1. СИГНАЛ ПРО ВИКЛИК (Показує модалку "Прийняти/Відхилити")
    @MessageMapping("/call")
    public void handleCall(Map<String, String> message) {
        String toUser = message.get("toUser");
        // Відправляємо дані про того, хто дзвонить (fromUser) отримувачу
        messagingTemplate.convertAndSendToUser(toUser, "/topic/call", message);
    }

    // 2. ПЕРЕДАЧА ОФФЕРА (WebRTC)
    @MessageMapping("/offer")
    public void handleOffer(Map<String, Object> payload) {
        String toUser = (String) payload.get("toUser");
        messagingTemplate.convertAndSendToUser(toUser, "/topic/offer", payload);
    }

    // 3. ПЕРЕДАЧА ВІДПОВІДІ (WebRTC)
    @MessageMapping("/answer")
    public void handleAnswer(Map<String, Object> payload) {
        String toUser = (String) payload.get("toUser");
        messagingTemplate.convertAndSendToUser(toUser, "/topic/answer", payload);
    }

    // 4. ПЕРЕДАЧА ICE-КАНДИДАТІВ (Зв'язок через мережу)
    @MessageMapping("/candidate")
    public void handleCandidate(Map<String, Object> payload) {
        String toUser = (String) payload.get("toUser");
        messagingTemplate.convertAndSendToUser(toUser, "/topic/candidate", payload);
    }

    // 5. ЗАВЕРШЕННЯ ДЗВІНКА
    @MessageMapping("/endCall")
    public void handleEndCall(Map<String, String> message) {
        String toUser = message.get("toUser");
        // Повідомляємо іншу сторону, що дзвінок розірвано
        messagingTemplate.convertAndSendToUser(toUser, "/topic/endCall", "ended");
    }
}