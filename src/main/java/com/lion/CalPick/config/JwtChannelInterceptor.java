package com.lion.CalPick.config;

import com.lion.CalPick.service.CustomUserDetailsService;
import com.lion.CalPick.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class JwtChannelInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtChannelInterceptor.class);

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtChannelInterceptor(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            logger.info("WebSocket Authorization Header: {}", authorizationHeader);

            if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                logger.info("Extracted JWT from WebSocket: {}", jwt);

                if (tokenProvider.validateToken(jwt)) {
                    String userId = tokenProvider.getUserIdFromJWT(jwt);
                    logger.info("User ID from WebSocket JWT: {}", userId);

                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    accessor.setUser(authentication); // Set the authenticated user in the accessor
                    logger.info("WebSocket user authenticated: {}", userId);
                } else {
                    logger.warn("Invalid JWT token for WebSocket connection: {}", jwt);
                }
            } else {
                logger.warn("No valid Authorization header found for WebSocket connection.");
            }
        }
        return message;
    }
}
