package com.cyanogen.experienceobelisk.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.phys.Vec3;

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

    public static float coinflip(float a, float b){
        return Math.random() <= 0.5 ? a : b;
    }

    public static Vec3 generateRandomBlockSurfacePos(BlockPos pos, float distanceFromCtr){

        double x = pos.getCenter().x;
        double y = pos.getCenter().y;
        double z = pos.getCenter().z;
        double rand = Math.random();

        if(rand <= 0.33){ //lock to x faces
            x = x + coinflip(-distanceFromCtr, distanceFromCtr);
            y = y + randomInRange(-distanceFromCtr, distanceFromCtr);
            z = z + randomInRange(-distanceFromCtr, distanceFromCtr);
        }
        else if(rand <= 0.66){ //lock to y faces
            x = x + randomInRange(-distanceFromCtr, distanceFromCtr);
            y = y + coinflip(-distanceFromCtr, distanceFromCtr);
            z = z + randomInRange(-distanceFromCtr, distanceFromCtr);
        }
        else{ //lock to z faces
            x = x + randomInRange(-distanceFromCtr, distanceFromCtr);
            y = y + randomInRange(-distanceFromCtr, distanceFromCtr);
            z = z + coinflip(-distanceFromCtr, distanceFromCtr);
        }

        return new Vec3(x,y,z);
    }

}
