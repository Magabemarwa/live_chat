/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james.chat.controller;

import com.james.chat.model.ChatMessage;
import java.io.IOException;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 *
 * @author james
 */
@Controller
public class WebSocketController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/queue/private")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage , @DestinationVariable String receiver, Principal principal ) throws IOException {

        chatMessage.setContent(chatMessage.getContent().toUpperCase());
        chatMessage.setSender(principal.getName());
        messagingTemplate.convertAndSendToUser(chatMessage.getReceiver(), "/queue/private", chatMessage);
        return chatMessage;
    }
    
    @MessageMapping("/chat.sendToSpecific")
    public ChatMessage sendToSpecificUser(@Payload ChatMessage chatMessage,
    Principal principal) throws IOException {
   
    messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/private", chatMessage);
    return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendToUser("/queue/private")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor accessor,
            Principal principal) {
        
        chatMessage.setSender(principal.getName());
        System.out.println(chatMessage.getSender());
        accessor.getSessionAttributes().put("username", chatMessage.getSender());
        
        return chatMessage;
    }


    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
