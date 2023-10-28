package com.cyanogen.experienceobelisk.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ExperienceReceivingEntity extends BlockEntity {

    //Generic block entity for appliances that use XP
    //Currently only used by the fountain, but will be used for other blocks in the future

    public ExperienceReceivingEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //-----------BINDING-----------//

    public int boundX;
    public int boundY;
    public int boundZ;
    public boolean isBound = false;

    public void setBound(){
        this.isBound = true;
        setChanged();
    }

    public void setUnbound(){
        this.isBound = false;
        setChanged();
    }

    public void setBoundPos(BlockPos pos){
        this.boundX = pos.getX();
        this.boundY = pos.getY();
        this.boundZ = pos.getZ();
        setChanged();
    }

    public BlockPos getBoundPos(){
        return new BlockPos(boundX, boundY, boundZ);
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
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);

        tag.putBoolean("isBound", isBound);
        tag.putInt("BoundX", boundX);
        tag.putInt("BoundY", boundY);
        tag.putInt("BoundZ", boundZ);
    }

    //sends CompoundTag out with nbt data
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();

        tag.putBoolean("isBound", isBound);
        tag.putInt("BoundX", boundX);
        tag.putInt("BoundY", boundY);
        tag.putInt("BoundZ", boundZ);

        return tag;
    }

    //gets packet to send to client
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
