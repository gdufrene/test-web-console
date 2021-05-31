package tu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.gdufrene.appender.CycleOutputStream;

public class CycleBufferTest {

	CycleOutputStream out;
	
	@BeforeEach
	public void initBuffer() {
		out = new CycleOutputStream(10);
	}
	
	@Test
	public void testWriteOne() throws IOException {
		out.write(10);
		byte[] res = getBufferContent();
		assertEquals(1, res.length);
		assertEquals(10, res[0]);
	}
	
	private byte[] getBufferContent() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		out.transfertContentTo(bos);
		byte[] res = bos.toByteArray();
		return res;
	}
	
	@Test
	public void testWriteMoreThanCapacity() throws IOException {
		out.write(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
		byte[] res = getBufferContent();
		assertEquals(10, res.length);
		assertEquals(3, res[0]);
		assertEquals(12, res[9]);
	}
	
	@Test
	public void testGetLastOverCapacity() throws IOException {
		out.write(new byte[]{1, 2, 3, 4, 5, 6});
		out.write(new byte[]{7, 8, 9, 10, 11, 12});
		byte[] res = getBufferContent();
		assertEquals(10, res.length);
		assertEquals(3, res[0]);
		assertEquals(12, res[9]);
	}
}
