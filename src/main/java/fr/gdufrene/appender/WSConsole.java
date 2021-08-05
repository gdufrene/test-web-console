package fr.gdufrene.appender;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import fr.gdufrene.api.SessionRegistry;

@ServerEndpoint(value = "/stream")
@Component
public class WSConsole {
	
	@OnOpen
    public void start(Session session) {
	    SessionRegistry.getInstance().add(session);
    }

    @OnClose
    public void end(Session session) {
        SessionRegistry.getInstance().remove(session);
    }

    @OnMessage
    public void incoming(String message) {
    	// nothing
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
	
}
