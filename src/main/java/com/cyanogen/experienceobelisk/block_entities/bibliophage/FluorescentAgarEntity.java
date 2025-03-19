package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterBlocks;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FluorescentAgarEntity extends BlockEntity {

    public FluorescentAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.FLUORESCENT_AGAR_BE.get(), pos, state);
    }

    int infectionProgress = 0;

    public void incrementInfectionProgress(){

        infectionProgress++;
        BlockPos pos = getBlockPos();

        if(level != null){
            level.levelEvent(null, 2001, pos, Block.getId(RegisterBlocks.FLUORESCENT_AGAR.get().defaultBlockState()));

            if(infectionProgress >= 4){
                level.playSound(null, pos, RegisterSounds.FLUORESCENT_AGAR_INFECT.get(), SoundSource.BLOCKS, 0.45f,0.5f);
                level.setBlockAndUpdate(getBlockPos(), RegisterBlocks.NUTRIENT_AGAR.get().defaultBlockState());
            }
        }

        setChanged();
    }

    public int getInfectionProgress(){
        return infectionProgress;
    }

    //-----------NBT-----------//

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        this.infectionProgress = tag.getInt("InfectionProgress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tag.putInt("InfectionProgress", infectionProgress);
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("InfectionProgress", infectionProgress);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
