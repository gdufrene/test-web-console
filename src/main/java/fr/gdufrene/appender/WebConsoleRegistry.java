package fr.gdufrene.appender;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WebConsoleRegistry {
	
	private final static WebConsoleRegistry registry = new WebConsoleRegistry();
	
	private Map<String, WebConsoleAppender<?>> appenders = new HashMap<>();
	
	private WebConsoleRegistry() {}
	
	public static WebConsoleRegistry getInstance() {
		return registry;
	}
	
	public void register(String name, WebConsoleAppender<?> appender) {
		appenders.put(name, appender);
	}
	
	public Optional<WebConsoleAppender<?>> getAppender(String name) {
		return Optional.ofNullable(appenders.get(name));
	}
	
	public Optional<WebConsoleAppender<?>> getAnyAppender() {
		return appenders.values()
				.stream()
				.findFirst();
	}

}
