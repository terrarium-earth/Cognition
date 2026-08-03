package com.cyanogen.cognition.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiscUtils {

    //----- MATH -----//

    public static double straightLineDistance(BlockPos a, BlockPos b){

        double deltaX = Math.abs(a.getX() - b.getX());
        double deltaY = Math.abs(a.getY() - b.getY());
        double deltaZ = Math.abs(a.getZ() - b.getZ());

        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2) + Math.pow(deltaZ,2));
    }

    public static float randomInRange(float min, float max){
        return (float) (min + Math.random() * (max - min));
    }

    public static int randomIntInRange(int min, int max){
        return (int) Math.floor(randomInRange(min, max + 1));
    }

    public static int weightedRandInt(int min, int max, float bias){

        // P(max) / P(min) = 2 ^ bias
        //bias of 1 --> P(max) = 2 * P(min)
        //bias of -1 --> P(max) = 0.5 * P(min)
        //bias of 0 --> P(max) = P(min)

        boolean passthrough = (bias == 0) || (min == max);
        boolean fail = (min > max) || (max - min >= 100) || (Math.abs(bias) >= 10);
        String message = "[Cognition] Unable to use weighted randInt function, defaulting to unweighted ver. Check that all parameters are within bounds.";
        if(fail || passthrough){
            if(fail) System.out.println(message);
            return randomIntInRange(min, max);
        }

        int range = max - min;
        float step = (float) ((Math.pow(2, bias) - 1) / range);
        float cumSum = 0;

        for(int i = 0; i <= range; i++){
            float weight = 1 + step * i;
            cumSum += weight;
        }

        float cumRand = randomInRange(0, cumSum);

        for(int j = 0; j <= range; j++){
            float weight = 1 + step * j;
            cumRand -= weight;

            if(cumRand <= 0){
                return j + min;
            }
        }

        System.out.println(message);
        return randomIntInRange(min, max);
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

    public static List<BlockPos> get2DAreaOfEffect(BlockPos pos, int boxBound, float circularBound){

        int x1 = pos.getX() - boxBound;
        int x2 = pos.getX() + boxBound;
        int z1 = pos.getZ() - boxBound;
        int z2 = pos.getZ() + boxBound;
        int y = pos.getY();

        List<BlockPos> posList = new ArrayList<>();

        for(int x = x1; x <= x2; x++){
            for(int z = z1; z <= z2; z++){

                BlockPos target = new BlockPos(x,y,z);
                if(MiscUtils.straightLineDistance(target, pos) <= circularBound){
                    posList.add(target);
                }
            }

        }
        return posList;
    }

    //----- DATA CONVERSION -----//

    public static List<String> getLinesFromString(String input, int lineWidth, Font font){

        List<FormattedText> lines = font.getSplitter().splitLines(input, lineWidth, Style.EMPTY);
        List<String> outputLines = new ArrayList<>();

        for(FormattedText line : lines){
            outputLines.add(line.getString());
        }

        return outputLines;
    }

    public static Map<String, Float> getExperienceItemMapFromList(List<String> list){
        Map<String, Float> map = new HashMap<>();

        for(String element : list){
            String[] substrings = element.split(" = ");
            if(substrings.length == 2){
                String itemName = substrings[0];
                String xp = substrings[1];

                if(isValidFloat(xp)){
                    map.put(itemName, Float.valueOf(xp));
                }
            }
        }
        return map;
    }

    public static boolean isValidFloat(String n){
        try{
            Float.valueOf(n);
            return true;
        }
        catch(NumberFormatException exception){
            return false;
        }
    }

}
