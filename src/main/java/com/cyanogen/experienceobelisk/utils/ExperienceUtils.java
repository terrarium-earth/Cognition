package com.cyanogen.experienceobelisk.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;

public class ExperienceUtils {

    public static int levelsToXP(int levels){
        if (levels <= 16) {
            return (int) (Math.pow(levels, 2) + 6 * levels);
        } else if (levels <= 31) {
            return (int) (2.5 * Math.pow(levels, 2) - 40.5 * levels + 360);
        } else {
            return (int) (4.5 * Math.pow(levels, 2) - 162.5 * levels + 2220);
        }
    }

    public static int xpToLevels(long xp){
        if (xp < 394) {
            return (int) (Math.sqrt(xp + 9) - 3);
        } else if (xp < 1628) {
            return (int) ((Math.sqrt(40 * xp - 7839) + 81) * 0.1);
        } else {
            return (int) ((Math.sqrt(72 * xp - 54215) + 325) / 18); //when xp >~2980k, breaks int value limit
        }
    }

    public static long getTotalXP(Player player){
        return levelsToXP(player.experienceLevel) + Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
    }

    public static long getTotalXP(int levels, float progress) {
        return levelsToXP(levels) + Math.round(progress * getXpNeededForNextLevel(levels));
    }

    public static double getProgressToNextLevel(int experiencePoints, int experienceLevels){
        int n = experiencePoints - levelsToXP(experienceLevels); //remaining xp after levels are removed
        int m = levelsToXP(experienceLevels + 1) - levelsToXP(experienceLevels); //total xp to get to next level

        return (double) n/m;
    }

    public static long getXpNeededForNextLevel(int experienceLevel) {
        if (experienceLevel >= 30) {
            return 112 + (experienceLevel - 30) * 9L;
        } else {
            return experienceLevel >= 15 ? 37 + (experienceLevel - 15) * 5 : 7 + experienceLevel * 2L;
        }
    }

    public static int getOrbValue(ExperienceOrb orb){
        CompoundTag tag = new CompoundTag();
        orb.addAdditionalSaveData(tag);

        return orb.value * tag.getInt("Count");
    }

    public static int getClumpedOrbValue(ExperienceOrb orb){
        //gets orb values directly from clumpedMap rather than orb.value if Clumps is installed
        CompoundTag tag = new CompoundTag();
        orb.addAdditionalSaveData(tag);

        int totalValue = 0;

        if(tag.contains("clumpedMap")){
            CompoundTag clumpedMap = tag.getCompound("clumpedMap");

            for(String value : clumpedMap.getAllKeys()){
                totalValue += clumpedMap.getInt(value) * Integer.parseInt(value);
            }
        }
        else{
            totalValue = orb.value * tag.getInt("Count");
        }
        return totalValue;
    }

}
