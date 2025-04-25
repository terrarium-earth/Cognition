package com.cyanogen.cognition.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class ExperienceReceivingEntity extends BlockEntity {

    //Generic block entity for appliances that use XP

    public ExperienceReceivingEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //-----------BINDING-----------//

    public int boundX;
    public int boundY;
    public int boundZ;
    public boolean isBound = false;

    public void setBoundPos(BlockPos pos){
        this.isBound = true;
        this.boundX = pos.getX();
        this.boundY = pos.getY();
        this.boundZ = pos.getZ();
        setChanged();
    }

    public void clearBoundPos(){
        this.isBound = false;
        this.boundX = 0;
        this.boundY = 0;
        this.boundZ = 0;
        setChanged();
    }

    public BlockPos getBoundPos(){
        return new BlockPos(boundX, boundY, boundZ);
    }

    public @Nullable ExperienceObeliskEntity getBoundObelisk(){
        if(isBound && this.level != null && this.level.getBlockEntity(getBoundPos()) instanceof ExperienceObeliskEntity obelisk){
            return obelisk;
        }
        else{
            return null;
        }
    }

    //-----------REDSTONE-----------//

    public boolean redstoneEnabled = false;

    public boolean isRedstoneEnabled(){
        return redstoneEnabled;
    }

    public void setRedstoneEnabled(boolean enabled){
        this.redstoneEnabled = enabled;
        if(level != null) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
        setChanged();
    }

    //-----------NBT-----------//

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);

        this.isBound = tag.getBoolean("isBound");
        this.boundX = tag.getInt("BoundX");
        this.boundY = tag.getInt("BoundY");
        this.boundZ = tag.getInt("BoundZ");
        this.redstoneEnabled = tag.getBoolean("RedstoneEnabled");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);

        tag.putBoolean("isBound", isBound);
        tag.putInt("BoundX", boundX);
        tag.putInt("BoundY", boundY);
        tag.putInt("BoundZ", boundZ);
        tag.putBoolean("RedstoneEnabled", redstoneEnabled);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);

        this.isBound = tag.getBoolean("isBound");
        this.boundX = tag.getInt("BoundX");
        this.boundY = tag.getInt("BoundY");
        this.boundZ = tag.getInt("BoundZ");
        this.redstoneEnabled = tag.getBoolean("RedstoneEnabled");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);

        tag.putBoolean("isBound", isBound);
        tag.putInt("BoundX", boundX);
        tag.putInt("BoundY", boundY);
        tag.putInt("BoundZ", boundZ);
        tag.putBoolean("RedstoneEnabled", redstoneEnabled);

        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
