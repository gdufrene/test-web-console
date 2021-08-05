package fr.gdufrene.appender;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

import ch.qos.logback.core.ConsoleAppender;

public class WebConsoleAppender<E> extends ConsoleAppender<E> {
	
	public static OutputStream bos;
	
	
	private Set<Consumer<String>> consumers = new CopyOnWriteArraySet<>();
	
	public WebConsoleAppender() {
		System.out.println("[Log] new WebConsoleAppender");
	}
	
	@Override
	public void start() {
		super.start();
		
		WebConsoleRegistry.getInstance().register(getName(), this);
		
		// bos = new CycleOutputStream(80000);
		bos = new LastLinesOutputStream();
		OutputStream original = getOutputStream();
		FilterOutputStream out = new FilterOutputStream(original) {
			@Override
			public void write(int b) throws IOException {
				original.write(b);
				bos.write(b);
				for (Consumer<String> consumer : consumers) {
				    consumer.accept(new String(new char[] {(char)b}) );
				}
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				original.write(b, off, len);
				bos.write(b, off, len);
				String str = new String(b, off, len);
				for (Consumer<String> consumer : consumers) {
                    consumer.accept(str);
                }
			}
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
