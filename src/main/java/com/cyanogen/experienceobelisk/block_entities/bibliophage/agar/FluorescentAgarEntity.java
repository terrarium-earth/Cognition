package com.cyanogen.experienceobelisk.block_entities.bibliophage.agar;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterBlocks;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
        super(RegisterBlockEntities.FLUORESCENT_AGAR.get(), pos, state);
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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);

        this.infectionProgress = tag.getInt("InfectionProgress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);

        tag.putInt("InfectionProgress", infectionProgress);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);

        this.infectionProgress = tag.getInt("InfectionProgress");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);

        tag.putInt("InfectionProgress", infectionProgress);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
