package com.cyanogen.experienceobelisk.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

public class MiscUtils {

    public static double straightLineDistance(BlockPos a, BlockPos b){

        double deltaX = Math.abs(a.getX() - b.getX());
        double deltaY = Math.abs(a.getY() - b.getY());
        double deltaZ = Math.abs(a.getZ() - b.getZ());

        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2) + Math.pow(deltaZ,2));
    }

    public static List<String> getLinesFromString(String input, int lineWidth, Font font){

        List<FormattedText> lines = font.getSplitter().splitLines(input, lineWidth, Style.EMPTY);
        List<String> outputLines = new ArrayList<>();

        for(FormattedText line : lines){
            outputLines.add(line.getString());
        }

        return outputLines;
    }

    public static float randomInRange(float min, float max){
        return (float) (min + Math.random() * (max - min));
    }

}
