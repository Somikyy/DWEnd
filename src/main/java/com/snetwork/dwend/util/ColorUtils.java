package com.snetwork.dwend.util;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String colorize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String color = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.of("#" + color).toString());
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public static String stripColor(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        text = HEX_PATTERN.matcher(text).replaceAll("");
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', text));
    }

    public static String gradient(String text, String fromHex, String toHex) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        fromHex = fromHex.replace("#", "");
        toHex = toHex.replace("#", "");

        int fromR = Integer.parseInt(fromHex.substring(0, 2), 16);
        int fromG = Integer.parseInt(fromHex.substring(2, 4), 16);
        int fromB = Integer.parseInt(fromHex.substring(4, 6), 16);

        int toR = Integer.parseInt(toHex.substring(0, 2), 16);
        int toG = Integer.parseInt(toHex.substring(2, 4), 16);
        int toB = Integer.parseInt(toHex.substring(4, 6), 16);

        char[] chars = text.toCharArray();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                builder.append(' ');
                continue;
            }

            double ratio = (double) i / (chars.length - 1);
            int r = (int) (fromR + (toR - fromR) * ratio);
            int g = (int) (fromG + (toG - fromG) * ratio);
            int b = (int) (fromB + (toB - fromB) * ratio);

            builder.append(String.format("&#%02x%02x%02x", r, g, b)).append(chars[i]);
        }

        return colorize(builder.toString());
    }

    public static String rainbow(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String[] colors = {
                "#ff0000", "#ff7f00", "#ffff00", "#00ff00",
                "#0000ff", "#4b0082", "#9400d3"
        };

        char[] chars = text.toCharArray();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                builder.append(' ');
                continue;
            }

            String color = colors[i % colors.length];
            builder.append("&").append(color).append(chars[i]);
        }

        return colorize(builder.toString());
    }
}