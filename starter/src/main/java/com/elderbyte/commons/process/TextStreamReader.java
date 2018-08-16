package com.elderbyte.commons.process;


/**
 * Reads a stream and turns it into text
 */
public class TextStreamReader extends BufferedStreamReader<String> {
    @Override
    public String getValue() {
        return new String(getBuffer());
    }
}
