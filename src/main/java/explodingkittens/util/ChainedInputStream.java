package explodingkittens.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChainedInputStream extends InputStream {
    private final List<ByteArrayInputStream> segments;
    private int currentSegment;

    public ChainedInputStream() {
        this.segments = new ArrayList<>();
        this.currentSegment = 0;
    }

    /**
     * Adds a new segment to the input stream chain.
     * @param input The string to be added as a new segment in the input stream
     */
    public void addSegment(String input) {
        segments.add(new ByteArrayInputStream(input.getBytes()));
    }

    @Override
    public int read() throws IOException {
        if (currentSegment >= segments.size()) {
            return -1;
        }

        ByteArrayInputStream current = segments.get(currentSegment);
        int result = current.read();
        
        if (result == -1) {
            currentSegment++;
            return read();
        }
        
        return result;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (currentSegment >= segments.size()) {
            return -1;
        }

        ByteArrayInputStream current = segments.get(currentSegment);
        int result = current.read(b, off, len);
        
        if (result == -1) {
            currentSegment++;
            return read(b, off, len);
        }
        
        return result;
    }
} 