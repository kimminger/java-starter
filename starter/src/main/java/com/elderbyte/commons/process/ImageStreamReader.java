package com.elderbyte.commons.process;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageStreamReader implements StreamReader<BufferedImage>
{

    private final ByteStreamReader sr = new ByteStreamReader();

    @Override
    public void read(InputStream stream) {
        sr.read(stream);
    }

    @Override
    public BufferedImage getValue() {
        try {
            byte[] buffer = sr.getValue();
            if(buffer.length > 0){
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(buffer));
                if(image == null){
                    throw new IOException("Failed to parse image from byte stream! Length was: " + buffer.length);
                }
                return image;
            }else{
                throw new IOException("Image data buffer was empty!");
            }
        } catch (Exception e) {
           throw new RuntimeException("Failed to parse image from byte buffer!",e);
        }
    }
}
