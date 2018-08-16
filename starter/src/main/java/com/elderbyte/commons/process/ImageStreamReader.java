package com.elderbyte.commons.process;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public class ImageStreamReader extends BufferedStreamReader<BufferedImage>
{
    @Override
    public BufferedImage getValue() {
        try {
            byte[] buffer = getBuffer();
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
