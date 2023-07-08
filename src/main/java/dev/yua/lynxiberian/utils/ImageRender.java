package dev.yua.lynxiberian.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageRender {
    public static File toFile(BufferedImage b) throws IOException {
        File f = File.createTempFile("foxes/", ".png");
        f.deleteOnExit();
        ImageIO.write(b, "PNG", f);
        return f;
    }

    public static BufferedImage renderVertical(BufferedImage originalImage) {
        int y = originalImage.getHeight();
        int x = y*9/16;
        BufferedImage finalImage = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = finalImage.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, finalImage.getWidth(), finalImage.getHeight());
        int cx = x/2-originalImage.getWidth()/2;

        g.drawImage(originalImage, cx, 0, originalImage.getWidth(), originalImage.getHeight(), null);
        g.setColor(Color.WHITE);
        return finalImage;
    }

    public static BufferedImage renderText(BufferedImage originalImage, String text) throws IOException, FontFormatException {
        BufferedImage finalImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = finalImage.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, finalImage.getWidth(), finalImage.getHeight());
        g.drawImage(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
        g.setColor(Color.BLACK);
        g.fillRect(0, finalImage.getHeight()-40, finalImage.getWidth(), 40);
        g.setColor(Color.WHITE);
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classloader.getResourceAsStream("static/fonts/UbuntuMono-Regular.ttf")){
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            g.setFont(customFont);
            g.drawString(text, 10, finalImage.getHeight()-14);
        }
        return finalImage;
    }

}
