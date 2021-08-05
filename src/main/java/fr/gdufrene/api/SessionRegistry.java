package fr.gdufrene.api;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.gdufrene.appender.WebConsoleRegistry;

@Component
public class SessionRegistry {
    
    private final static Logger log = LoggerFactory.getLogger(SessionRegistry.class);
    private static SessionRegistry instance;
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    
    public SessionRegistry() {
        WebConsoleRegistry.getInstance()
            .getAnyAppender()
            .ifPresent( appender -> appender.onData(this::broadcast) );
        instance = this;
    }
    
    public static SessionRegistry getInstance() {
        return instance;
    }
    
    public void broadcast(String str) {
        try {
            for ( Session session : sessionMap.values() ) {
                session.getBasicRemote() 
                .sendText(str);
            }
        } catch (IOException e) {
            log.error("Unable to send broadcast", e);
        }
    }

    public void remove(Session session) {
        log.info("End session '{}'", session.getId());
        sessionMap.remove(session.getId());
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(Session session) {
        log.info("New session '{}'", session.getId());
        sessionMap.put(session.getId(), session);
    }
    
    
}
