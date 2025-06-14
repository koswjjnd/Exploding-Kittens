package explodingkittens.util;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class ChainedInputStreamTest {

    @Test
    void testEmptyStream() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testSingleSegment() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            
            byte[] buffer = new byte[5];
            int bytesRead = stream.read(buffer, 0, 5);
            
            assertEquals(5, bytesRead);
            assertEquals("Hello", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            assertEquals(-1, stream.read()); // End of stream
        }
    }

    @Test
    void testMultipleSegments() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            stream.addSegment("World");
            
            // Read first segment
            byte[] buffer = new byte[5];
            int bytesRead = stream.read(buffer, 0, 5);
            assertEquals(5, bytesRead);
            assertEquals("Hello", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // Read second segment
            bytesRead = stream.read(buffer, 0, 5);
            assertEquals(5, bytesRead);
            assertEquals("World", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // End of stream
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadWithOffset() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            
            byte[] buffer = new byte[7];
            buffer[0] = 'X';
            buffer[1] = 'Y';
            
            int bytesRead = stream.read(buffer, 2, 5);
            
            assertEquals(5, bytesRead);
            assertEquals("XYHello", new String(buffer, 0, 7, StandardCharsets.UTF_8));
        }
    }

    @Test
    void testReadPartialBuffer() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            
            byte[] buffer = new byte[3];
            int bytesRead = stream.read(buffer, 0, 3);
            
            assertEquals(3, bytesRead);
            assertEquals("Hel", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // Read remaining bytes
            bytesRead = stream.read(buffer, 0, 3);
            assertEquals(2, bytesRead);
            assertEquals("lo", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }
    }

    @Test
    void testReadAcrossSegments() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            stream.addSegment("World");
            
            byte[] buffer = new byte[3];
            
            // Read first segment
            int bytesRead = stream.read(buffer, 0, 3);
            assertEquals(3, bytesRead);
            assertEquals("Hel", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // Read rest of first segment
            bytesRead = stream.read(buffer, 0, 3);
            assertEquals(2, bytesRead);
            assertEquals("lo", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // Read second segment
            bytesRead = stream.read(buffer, 0, 3);
            assertEquals(3, bytesRead);
            assertEquals("Wor", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // Read last bytes
            bytesRead = stream.read(buffer, 0, 3);
            assertEquals(2, bytesRead);
            assertEquals("ld", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // End of stream
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadWithEmptySegments() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("");
            stream.addSegment("Hello");
            stream.addSegment("");
            stream.addSegment("World");
            
            // Skip empty segment
            byte[] buffer = new byte[5];
            int bytesRead = stream.read(buffer, 0, 5);
            assertEquals(5, bytesRead);
            assertEquals("Hello", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // Skip empty segment
            bytesRead = stream.read(buffer, 0, 5);
            assertEquals(5, bytesRead);
            assertEquals("World", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // End of stream
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadWithLargeBuffer() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            stream.addSegment("World");
            
            // Read first segment
            byte[] buffer = new byte[20];
            int bytesRead = stream.read(buffer, 0, 20);
            assertEquals(5, bytesRead);
            assertEquals("Hello", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // Read second segment
            bytesRead = stream.read(buffer, 0, 20);
            assertEquals(5, bytesRead);
            assertEquals("World", new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            
            // End of stream
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadWithZeroLength() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            
            byte[] buffer = new byte[5];
            int bytesRead = stream.read(buffer, 0, 0);
            
            assertEquals(0, bytesRead);
        }
    }

    @Test
    void testReadWithNegativeOffset() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            
            byte[] buffer = new byte[5];
            try {
                @SuppressWarnings("unused")
                int bytesRead = stream.read(buffer, -1, 5);
                fail("Expected IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
                // Expected exception
            }
        }
    }

    @Test
    void testReadWithNegativeLength() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            
            byte[] buffer = new byte[5];
            try {
                @SuppressWarnings("unused")
                int bytesRead = stream.read(buffer, 0, -1);
                fail("Expected IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
                // Expected exception
            }
        }
    }

    @Test
    void testReadWithBufferTooSmall() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            
            byte[] buffer = new byte[3];
            try {
                @SuppressWarnings("unused")
                int bytesRead = stream.read(buffer, 0, 5);
                fail("Expected IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException e) {
                // Expected exception
            }
        }
    }

    @Test
    void testReadAfterAllSegmentsExhausted() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("Hello");
            
            // Read all data
            byte[] buffer = new byte[5];
            int bytesRead = stream.read(buffer, 0, 5);
            assertEquals(5, bytesRead);
            
            // Try to read again with buffer
            bytesRead = stream.read(buffer, 0, 5);
            assertEquals(-1, bytesRead);
            
            // Try to read again with single byte
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadWithEmptyStream() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            // Try to read with buffer
            byte[] buffer = new byte[5];
            int bytesRead = stream.read(buffer, 0, 5);
            assertEquals(-1, bytesRead);
            
            // Try to read single byte
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadSingleByte() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("A");
            
            // Read single byte
            int result = stream.read();
            assertEquals('A', result);
            
            // End of stream
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadSingleByteWithEmptySegment() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("");
            stream.addSegment("A");
            
            // Should skip empty segment and read 'A'
            int result = stream.read();
            assertEquals('A', result);
            
            // End of stream
            assertEquals(-1, stream.read());
        }
    }

    @Test
    void testReadSingleByteWithMultipleEmptySegments() throws IOException {
        try (ChainedInputStream stream = new ChainedInputStream()) {
            stream.addSegment("");
            stream.addSegment("");
            stream.addSegment("A");
            stream.addSegment("");
            stream.addSegment("B");
            
            // Should skip empty segments and read 'A'
            int result = stream.read();
            assertEquals('A', result);
            
            // Should skip empty segment and read 'B'
            result = stream.read();
            assertEquals('B', result);
            
            // End of stream
            assertEquals(-1, stream.read());
        }
    }
} 