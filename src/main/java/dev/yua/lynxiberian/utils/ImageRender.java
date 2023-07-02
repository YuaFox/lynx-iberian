package dev.yua.lynxiberian.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageRender {

    public static File render(File original, String text) throws IOException, FontFormatException {
        BufferedImage originalImage = ImageIO.read(original);
        BufferedImage finalImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight() + 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = finalImage.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, finalImage.getWidth(), finalImage.getHeight());
        g.drawImage(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
        g.setColor(Color.WHITE);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classloader.getResourceAsStream("static/fonts/UbuntuMono-Regular.ttf")){
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            g.setFont(customFont);
            g.drawString(text, 10, finalImage.getHeight()-14);
        }
        File f = File.createTempFile("foxes/", ".png");
        f.deleteOnExit();
        ImageIO.write(finalImage, "PNG", f);
        return f;
    }
}
