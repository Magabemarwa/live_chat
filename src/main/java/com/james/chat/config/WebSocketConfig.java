/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.james.chat.config;

import com.james.chat.model.ChatMessage;
import com.james.chat.model.StompPrincipal;
import java.security.Principal;
import java.util.Map;
import java.util.Random;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 *
 * @author james
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic"); 
        
        /*registry.enableStompBrokerRelay("/topic", "/queue")
        .setRelayHost("rabbitmqnamed")
        .setRelayPort(61613)
        .setClientLogin("guest")
        .setClientPasscode("guest");
        registry.setApplicationDestinationPrefixes("/app");*/
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(new RandomUsernameHandshakeHandler())
                .withSockJS();
    }
    
    public class CustomHandshakeHandler extends DefaultHandshakeHandler {

        @Override
        protected Principal determineUser(ServerHttpRequest request, 
                WebSocketHandler wsHandler, 
                Map<String, Object> attributes) {
            
            ChatMessage chatMessage = new ChatMessage();
            System.out.println(chatMessage.getSender());
            return new StompPrincipal("James");
        }
        
    }
    
   
    private class RandomUsernameHandshakeHandler extends DefaultHandshakeHandler {
    
    private final String[] ADJECTIVES = { "stupid", "clever", "bright", "dull" };
    private final String[] NOUNS = { "Oreango", "Marwa", "Omera", "James" };
    
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
    String username = this.getRandom(NOUNS) + "-" + this.getRandom(ADJECTIVES) + "-" + getRandomInt(14567);
    
    return new StompPrincipal(username);
    }
    
    private String getRandom(String[] array){
    int random = getRandomInt(array.length);
    return array[random];
    }
    
    private Integer getRandomInt(Integer value){
    Random random = new Random();
    int randomNumber = random.nextInt(value);
    return randomNumber;
    }
    
    }
    
}
