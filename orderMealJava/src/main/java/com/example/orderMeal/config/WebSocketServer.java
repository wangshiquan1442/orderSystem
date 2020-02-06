package com.example.orderMeal.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@ServerEndpoint(value = "/WebSocket")
@Component
@Slf4j
public class WebSocketServer {
    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("新的WebSocket连接：{}", session.getId());
        clients.put(session.getId(), session);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("断开WebSocket连接：{}", session.getId());
        clients.remove((session.getId()));
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("{}", message);
        this.sendAll(message);
    }

    private void sendAll(String message) {
        for(Map.Entry<String, Session> sessionEntry: clients.entrySet()) {
            sessionEntry.getValue().getAsyncRemote().sendText(message);
        }
    }
}
