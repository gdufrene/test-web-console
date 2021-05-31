package fr.gdufrene.appender;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.util.Assert;

public class CycleOutputStream extends OutputStream {
	
	private final static int DEFAULT_BUFFER_SIZE = 80000;
	
	private byte[] buffer; 
	private int start = 0;
	private int end = -1;
	private boolean empty = true;
	
	public CycleOutputStream() {
		this(DEFAULT_BUFFER_SIZE);
	}
	
	public CycleOutputStream(int bufferSize) {
		Assert.isTrue(bufferSize > 0, "Buffer size can't be negative");
		this.buffer = new byte[bufferSize];
	}
	
	@Override
	public void write(int b) throws IOException {
		end = (end + 1) % buffer.length;
		buffer[end] = (byte) b;
		if ( !empty && end == start ) {
			start = (start + 1) % buffer.length;
		}
		empty = false;
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		empty = false;
		if ( len > buffer.length ) {
			System.arraycopy(b, len - buffer.length, buffer, 0, buffer.length);
			start = 0;
			end = buffer.length - 1;
			return;
		}
		int available = buffer.length - end - 1;
		int written = Math.min(available, len);
		System.arraycopy(b, 0, buffer, end + 1, written);
		if ( written < len ) {
			end = len - written;
			System.arraycopy(b, written, buffer, 0, end);
			end--;
			if ( end > start ) {
				start = (end + 1) % buffer.length;
			}
		} else {
			end = end + written;
			if ( start > end ) {
				start = (end + 1) % buffer.length;
			}
		}
	}
	
	public void transfertContentTo(OutputStream out) throws IOException {
		if ( end < 0 ) {
			return;
		}
		if ( end > start || end == start ) {
			out.write(buffer, start, end - start + 1);
			return;
		}
		
		out.write(buffer, start, buffer.length - start);
		out.write(buffer, 0, end + 1);
	}

}
