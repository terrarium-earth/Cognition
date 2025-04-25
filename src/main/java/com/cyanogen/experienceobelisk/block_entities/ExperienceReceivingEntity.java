package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import net.minecraft.core.BlockPos;
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

    public void setUnbound(){
        this.boundX = 0;
        this.boundY = 0;
        this.boundZ = 0;
        this.isBound = false;
        setChanged();
    }

    public void setBoundPos(BlockPos pos){
        this.boundX = pos.getX();
        this.boundY = pos.getY();
        this.boundZ = pos.getZ();
        this.isBound = true;
        setChanged();
    }

    public BlockPos getBoundPos(){
        return new BlockPos(boundX, boundY, boundZ);
    }

    public ExperienceObeliskEntity getBoundObelisk(){
        if(this.isBound && this.level != null && this.level.getBlockEntity(getBoundPos()) instanceof ExperienceObeliskEntity obelisk){
            return obelisk;
        }
        else{
            return null;
        }
    }

    //-----------SCREEN-----------//

    public boolean obeliskStillExists = false;
    public int obeliskLevels = 0;
    public int obeliskPoints = 0;
    public double obeliskProgress = 0;

    public void sendObeliskInfoToScreen(){

        @Nullable ExperienceObeliskEntity obelisk = getBoundObelisk();
        boolean exists = obelisk != null;

        this.obeliskStillExists = exists;
        this.obeliskLevels = exists ? obelisk.getLevels() : 0;
        this.obeliskPoints = exists ? obelisk.getExperiencePoints() : 0;
        this.obeliskProgress = ExperienceUtils.getProgressToNextLevel(obeliskPoints, obeliskLevels);

        //used to send data from the bound obelisk to the GUI
        //remember to fill in the tick behavior and pass it into getTicker
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
    public void load(CompoundTag tag)
    {
        super.load(tag);

        this.isBound = tag.getBoolean("isBound");
        this.boundX = tag.getInt("BoundX");
        this.boundY = tag.getInt("BoundY");
        this.boundZ = tag.getInt("BoundZ");
        this.redstoneEnabled = tag.getBoolean("RedstoneEnabled");
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);

        tag.putBoolean("isBound", isBound);
        tag.putInt("BoundX", boundX);
        tag.putInt("BoundY", boundY);
        tag.putInt("BoundZ", boundZ);
        tag.putBoolean("RedstoneEnabled", redstoneEnabled);
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();

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
