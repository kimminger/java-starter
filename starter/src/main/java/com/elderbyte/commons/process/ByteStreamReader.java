package com.elderbyte.commons.process;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteStreamReader implements StreamReader<byte[]>{

    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private Exception e;

    @Override
    public void read(InputStream stream) {
        try {
            copy(stream, out);
        }catch (Exception e){
            this.e = e;
        }
    }

    @Override
    public byte[] getValue(){
        if(e != null){
            throw new RuntimeException("There was an error while reading the input stream.", e);
        }
        return out.toByteArray();
    }


    private static void copy(InputStream input, ByteArrayOutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            out.write(buffer, 0, bytesRead);
        }
    }

}
