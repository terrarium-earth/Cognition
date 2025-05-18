package com.cyanogen.cognition.saved_data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nullable;
import java.io.IOException;

public class MemoryTabletData extends SavedData {

    //Each instance is specific to every player using a Memory Tablet
    //Contains:
    // - the position of each player's linked obelisk (dynamic, will change if obelisk is moved)
    // - experience points to recover on the event of death

    private boolean hasLinkedObelisk = false;
    private BlockPos linkedObelisk = new BlockPos(0,0,0);
    private long experienceToRecover = 0L;

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {

        tag.putBoolean("HasLinkedObelisk", hasLinkedObelisk);
        tag.putIntArray("LinkedObelisk", new int[]{linkedObelisk.getX(), linkedObelisk.getY(), linkedObelisk.getZ()});
        tag.putLong("ExperienceToRecover", experienceToRecover);
        return tag;
    }

    public MemoryTabletData load(CompoundTag tag){

        hasLinkedObelisk = tag.getBoolean("HasLinkedObelisk");
        int[] pos = tag.getIntArray("LinkedObelisk");
        linkedObelisk = new BlockPos(pos[0], pos[1], pos[2]);
        experienceToRecover = tag.getLong("ExperienceToRecover");
        return this;
    }

    public static MemoryTabletData loadStatic(CompoundTag tag, HolderLookup.Provider provider){
        MemoryTabletData data = new MemoryTabletData();
        return data.load(tag);
    }

    public void setLinkedObelisk(BlockPos pos, boolean hasLinkedObelisk){
        this.linkedObelisk = pos;
        this.hasLinkedObelisk = hasLinkedObelisk;
        setDirty();
    }

    public BlockPos getLinkedObelisk(){
        return linkedObelisk;
    }

    public boolean hasLinkedObelisk(){
        return hasLinkedObelisk;
    }

    public void setExperienceToRecover(long points){
        this.experienceToRecover = points;
        setDirty();
    }

    public long getExperienceToRecover() {
        return experienceToRecover;
    }

    public static Factory<MemoryTabletData> factory(@Nullable MemoryTabletData data){

        if(data != null){
            return new Factory<>(() -> data, MemoryTabletData::loadStatic);
        }
        else{
            return new Factory<>(MemoryTabletData::new, MemoryTabletData::loadStatic);
        }
    }

    public static @Nullable MemoryTabletData getFromStorage(Level level, String uuid){
        if(level.getServer() != null) {

            try(ServerLevel overworld = level.getServer().overworld()){
                DimensionDataStorage overworldStorage = overworld.getDataStorage();
                MemoryTabletData data = overworldStorage.get(factory(null), "MemoryTabletData=" + uuid);
                if(data != null && data.hasLinkedObelisk){
                    return data;
                }
            }
            catch(IOException exception){
                System.out.println("[Cognition] Unable to load overworld data storage for Memory Tablet");
                System.out.println(exception.getMessage());
            }
        }
        return null;
    }

    public static @Nullable MemoryTabletData getFromStorage(Player player){
        return getFromStorage(player.level(), player.getStringUUID());
    }

    public static void createAndSaveToStorage(Level level, String uuid, MemoryTabletData data){
        if(level.getServer() != null) {

            try(ServerLevel overworld = level.getServer().overworld()){
                DimensionDataStorage overworldStorage = overworld.getDataStorage();
                overworldStorage.computeIfAbsent(factory(data), "MemoryTabletData=" + uuid);
            }
            catch(IOException exception){
                System.out.println("[Cognition] Unable to load overworld data storage for Memory Tablet");
                System.out.println(exception.getMessage());
            }
        }
    }

    public static void createAndSaveToStorage(Player player, MemoryTabletData data){
        createAndSaveToStorage(player.level(), player.getStringUUID(), data);
    }

}
