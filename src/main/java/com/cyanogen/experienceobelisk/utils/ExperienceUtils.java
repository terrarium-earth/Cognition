package com.cyanogen.experienceobelisk.utils;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
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

    public static long getTotalXp(Player player){
        return ExperienceObeliskEntity.getTotalXP(player);
    }

    public static double getProgressToNextLevel(int experiencePoints, int experienceLevels){
        int n = experiencePoints - levelsToXP(experienceLevels); //remaining xp after levels are removed
        int m = levelsToXP(experienceLevels + 1) - levelsToXP(experienceLevels); //total xp to get to next level

        return (double) n/m;
    }
}
