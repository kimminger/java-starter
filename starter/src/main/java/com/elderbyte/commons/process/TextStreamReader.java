package com.elderbyte.commons.process;


import java.io.InputStream;

/**
 * Reads a stream and turns it into text
 */
public class TextStreamReader implements StreamReader<String> {

    private final ByteStreamReader sr = new ByteStreamReader();

    @Override
    public void read(InputStream stream) {
        sr.read(stream);
    }

    @Override
    public String getValue() {
        return new String(sr.getValue());
    }
}
