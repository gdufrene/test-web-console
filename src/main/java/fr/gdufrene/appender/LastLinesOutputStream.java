package fr.gdufrene.appender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;

public class LastLinesOutputStream extends OutputStream {
    
    LinkedList<byte[]> lines = new LinkedList<>();
    int maxSize = 500;
    int bufferSize = 1000;
    int currentIndex;
    byte[] currentBuffer;
    
    public LastLinesOutputStream() {
        addLine();
    }

    @Override
    public void write(int b) throws IOException {
        currentBuffer[currentIndex++] = (byte) b;
        if ( currentIndex >= currentBuffer.length ) addLine();
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        int todo = Math.min(len, currentBuffer.length - currentIndex);
        System.arraycopy(b, off, currentBuffer, currentIndex, todo);
        currentIndex += todo;
        if ( currentIndex >= currentBuffer.length ) {
            addLine();
            int bytesLeft = len-todo;
            if (bytesLeft > 0) {
                write(b, off+todo, bytesLeft);
            }
        } else {
            if (b[off+len-1] == 10) write(13);
        }
    }
    
    private void addLine() {
        /*
        if (currentBuffer != null) {
            lines.add( currentBuffer );
        }
        */
        currentBuffer = new byte[bufferSize];
        lines.add( currentBuffer );
        currentIndex = 0;
        if (lines.size() > maxSize) {
            lines.remove(0);
        }
        System.out.println("Add Line ("+lines.size()+")");
    }
    
    @Override
    public String toString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Iterator<byte[]> it = lines.iterator();
        try {
            while(it.hasNext()) {
                byte[] data = it.next();
                // boolean last = it.hasNext();
                if (it.hasNext()) out.write(data);
                else out.write( data, 0, currentIndex );
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new String(out.toByteArray());
    }

}
