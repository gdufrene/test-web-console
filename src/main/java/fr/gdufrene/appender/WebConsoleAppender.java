package fr.gdufrene.appender;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

import ch.qos.logback.core.ConsoleAppender;

public class WebConsoleAppender<E> extends ConsoleAppender<E> {
	
	public static ByteArrayOutputStream bos;
	
	
	private Set<Consumer<String>> consumers = new CopyOnWriteArraySet<>();
	
	public WebConsoleAppender() {
		System.out.println("[Log] new WebConsoleAppender");
	}
	
	@Override
	public void start() {
		super.start();
		
		bos = new ByteArrayOutputStream(80000);
		OutputStream original = getOutputStream();
		FilterOutputStream out = new FilterOutputStream(original) {
			@Override
			public void write(int b) throws IOException {
				original.write(b);
				bos.write(b);
			}
			/*
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				original.write(b, off, len);
				bos.write(b, off, len);
			}
			*/
			@Override
			public void close() throws IOException {
				super.close();
				bos.close();
			}
		};
		setOutputStream(out);
	}
	
	public void onData(Consumer<String> client) {
		consumers.add(client);
	}

}
