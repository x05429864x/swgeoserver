package it.geosolutions.swgeoserver.comm.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DrawPic {

    public static void main(String[] args) {

        BufferedImage bi = new BufferedImage(200, 200, BufferedImage.TYPE_INT_BGR);

        final File file = new File("c:\\javaPic.png");

        try {
            if(file.exists()) {
                file.delete();
                file.createNewFile();
            }
        }catch(IOException e) {
            e.printStackTrace();
        }


        writeImage(bi, "png", file);
        System.out.println("绘图成功");

    }

    /** 通过指定参数写一个图片  */
    public static boolean writeImage(BufferedImage bi, String picType, File file) {

        Graphics g = bi.getGraphics();

        g.setColor(new Color(12, 123, 88));
        g.drawLine(0, 100, 100, 100);
        g.dispose();
        boolean val = false;
        try {
            val = ImageIO.write(bi, picType, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return val;
    }

}