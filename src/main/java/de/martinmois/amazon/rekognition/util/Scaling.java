package de.martinmois.amazon.rekognition.util;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class Scaling {

    public static byte[] scaleImage(Path imagePath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imagePath.toFile());
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        boolean landscape = true;
        if (w < h) {
            landscape = false;
        }
        boolean needScaling = false;
        if (landscape) {
            if (w > 1920) {
                needScaling = true;
            }
        } else {
            if (h > 1920) {
                needScaling = false;
            }
        }
        BufferedImage scaled = bufferedImage;
        if (needScaling) {
            if (landscape) {
                scaled = Scalr.resize(bufferedImage, Scalr.Method.BALANCED, 1920, 1080);
            } else {
                scaled = Scalr.resize(bufferedImage, Scalr.Method.BALANCED, 1080, 1920);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(scaled, "jpg", baos);
        return baos.toByteArray();
    }
}
