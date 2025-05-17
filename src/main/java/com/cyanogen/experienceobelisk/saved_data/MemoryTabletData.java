package com.cyanogen.experienceobelisk.saved_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nullable;

public class MemoryTabletData extends SavedData {

    private boolean hasLinkedObelisk = false;
    private int xpLevelsToRecover = 0;
    private float xpProgressToRecover = 0.0f;

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("HasLinkedObelisk", hasLinkedObelisk);
        tag.putInt("XPLevelsToRecover", xpLevelsToRecover);
        tag.putFloat("XProgressToRecover", xpProgressToRecover);
        return tag;
    }

    public MemoryTabletData load(CompoundTag tag){
        hasLinkedObelisk = tag.getBoolean("HasLinkedObelisk");
        xpLevelsToRecover = tag.getInt("XPLevelsToRecover");
        xpProgressToRecover = tag.getFloat("XProgressToRecover");
        return this;
    }

    public void set(boolean hasLinkedObelisk, int xpLevelsToRecover, float xpProgressToRecover){
        setLinkedObelisk(hasLinkedObelisk);
        setXpLevelsToRecover(xpLevelsToRecover);
        setXpProgressToRecover(xpProgressToRecover);
    }

    public void setLinkedObelisk(boolean hasLinkedObelisk){
        this.hasLinkedObelisk = hasLinkedObelisk;
        setDirty();
    }

    public void setXpLevelsToRecover(int levels){
        this.xpLevelsToRecover = levels;
        setDirty();
    }

    public void setXpProgressToRecover(float progress){
        this.xpProgressToRecover = progress;
        setDirty();
    }

    public boolean hasLinkedObelisk(){
        return hasLinkedObelisk;
    }

    public int getXpLevelsToRecover() {
        return xpLevelsToRecover;
    }

    public float getXpProgressToRecover() {
        return xpProgressToRecover;
    }

    public static MemoryTabletData loadStatic(CompoundTag tag){
        MemoryTabletData data = new MemoryTabletData();
        data.load(tag);
        return data;
    }

    public static @Nullable MemoryTabletData getFromStorage(Player player){
        Level level = player.level();

        if(level.getServer() != null) {
            DimensionDataStorage overworldStorage = level.getServer().overworld().getDataStorage();
            return overworldStorage.get(MemoryTabletData::loadStatic, "memoryTabletData_" + player.getStringUUID());
        }
        return null;
    }

    public static void createAndSaveToStorage(Player player, MemoryTabletData data){
        Level level = player.level();

        if(level.getServer() != null) {
            DimensionDataStorage overworldStorage = level.getServer().overworld().getDataStorage();
            overworldStorage.computeIfAbsent(MemoryTabletData::loadStatic, () -> data, "memoryTabletData_" + player.getStringUUID());
        }
    }

}
