package fr.gdufrene.appender;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/console")
public class WSConsole {
	
	private static final Set<WSConsole> connections = new CopyOnWriteArraySet<>();
	
	private Session session;

	@OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
        
        session.getAsyncRemote().sendBinary(data);
    }


    @OnClose
    public void end() {
        connections.remove(this);
    }


    @OnMessage
    public void incoming(String message) {
    	// nothing
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        t.printStackTrace();
    }
	
}
