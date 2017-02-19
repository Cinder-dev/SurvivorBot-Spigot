package com.fireblazerrr.survivorbot.utils;

import org.bukkit.ChatColor;

import java.awt.*;
import java.util.ArrayList;

/**
 * Java Code to get a color name from rgb/hex value/awt color
 * <p>
 * The part of looking up a color name from the rgb values is edited from
 * https://gist.github.com/nightlark/6482130#file-gistfile1-java (that has some errors) by Ryan Mast (nightlark)
 *
 * @author Xiaoxiao Li
 */
public class ColorUtils {

    /**
     * Initialize the color list that we have.
     */
    private static ArrayList<ColorName> initColorList() {
        ArrayList<ColorName> colorList = new ArrayList<ColorName>();
        colorList.add(new ColorName(ChatColor.BLACK, 0, 0, 0));
        colorList.add(new ColorName(ChatColor.DARK_BLUE, 0, 0, 170));
        colorList.add(new ColorName(ChatColor.DARK_GREEN, 0, 170, 0));
        colorList.add(new ColorName(ChatColor.DARK_AQUA, 0, 170, 170));
        colorList.add(new ColorName(ChatColor.DARK_RED, 170, 0, 0));
        colorList.add(new ColorName(ChatColor.DARK_PURPLE, 170, 0, 170));
        colorList.add(new ColorName(ChatColor.GOLD, 255, 170, 0));
        colorList.add(new ColorName(ChatColor.GRAY, 170, 170, 170));
        colorList.add(new ColorName(ChatColor.DARK_GRAY, 85, 85, 85));
        colorList.add(new ColorName(ChatColor.BLUE, 85, 85, 255));
        colorList.add(new ColorName(ChatColor.GREEN, 85, 255, 85));
        colorList.add(new ColorName(ChatColor.AQUA, 85, 255, 255));
        colorList.add(new ColorName(ChatColor.RED, 255, 85, 85));
        colorList.add(new ColorName(ChatColor.LIGHT_PURPLE, 255, 58, 255));
        colorList.add(new ColorName(ChatColor.YELLOW, 255, 255, 85));
        colorList.add(new ColorName(ChatColor.WHITE, 255, 255, 255));
        return colorList;
    }

    /**
     * Get the closest color name from our list
     *
     * @param r
     * @param g
     * @param b
     *
     * @return
     */
    public static ChatColor getColorNameFromRgb(int r, int g, int b) {
        ArrayList<ColorName> colorList = initColorList();
        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
        for (ColorName c : colorList) {
            mse = c.computeMSE(r, g, b);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }

        if (closestMatch != null) {
            return closestMatch.getName();
        } else {
            return ChatColor.WHITE;
        }
    }

    /**
     * Convert hexColor to rgb, then call getColorNameFromRgb(r, g, b)
     *
     * @param hexColor
     *
     * @return
     */
    public ChatColor getColorNameFromHex(int hexColor) {
        int r = (hexColor & 0xFF0000) >> 16;
        int g = (hexColor & 0xFF00) >> 8;
        int b = (hexColor & 0xFF);
        return getColorNameFromRgb(r, g, b);
    }

    public int colorToHex(Color c) {
        return Integer.decode("0x"
                + Integer.toHexString(c.getRGB()).substring(2));
    }

    public static ChatColor getColorNameFromColor(Color color) {
        if (color == null)
            return ChatColor.WHITE;
        return getColorNameFromRgb(color.getRed(), color.getGreen(),
                color.getBlue());
    }

    /**
     * SubClass of ColorUtils. In order to lookup color name
     *
     * @author Xiaoxiao Li
     */
    public static class ColorName {
        public int r, g, b;
        public ChatColor name;

        public ColorName(ChatColor name, int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = name;
        }

        public int computeMSE(int pixR, int pixG, int pixB) {
            return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
                    * (pixB - b)) / 3);
        }

        public int getR() {
            return r;
        }

        public int getG() {
            return g;
        }

        public int getB() {
            return b;
        }

        public ChatColor getName() {
            return name;
        }
    }
}
