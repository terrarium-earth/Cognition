package com.cyanogen.experienceobelisk.block_entities.bibliophage;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FluorescentAgarEntity extends BlockEntity {

    public FluorescentAgarEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.FLUORESCENT_AGAR_BE.get(), pos, state);
    }

    int infectionProgress = 0;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(level.getGameTime() % 20 == 0 && blockEntity instanceof FluorescentAgarEntity agarEntity){
            if(agarEntity.infectionProgress >= 4){
                level.setBlockAndUpdate(pos, RegisterBlocks.NUTRIENT_AGAR.get().defaultBlockState());
            }
        }
    }

    public void incrementInfectionProgress(){
        this.infectionProgress++;
        setChanged();
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
