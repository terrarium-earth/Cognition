package com.cyanogen.experienceobelisk.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import java.util.Collection;

public class AuralProjectorEntity extends ExperienceReceivingEntity {

    public AuralProjectorEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModTileEntitiesInit.AURALPROJECTOR_BE.get(), pPos, pBlockState);
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(level.getBlockEntity(pos) instanceof AuralProjectorEntity projector){

            if(projector.isBound
                    && level.getBlockEntity(projector.getBoundPos()) instanceof ExperienceObeliskEntity obelisk
                    && obelisk.getFluidAmount() > 0
                    && projector.isActive){

                obelisk.drain(1);
                level.sendBlockUpdated(obelisk.getBlockPos(), obelisk.getBlockState(), obelisk.getBlockState(), 2);
                //1mB per tick, 20mB = 1xp per second
            }
        }
    }

    public static void livingDropsEvent(LivingDropsEvent event){
        LivingEntity deceased = event.getEntityLiving();
        Collection<ItemEntity> c = event.getDrops();
        Level level = deceased.getLevel();
        BlockPos pos = deceased.blockPosition();

        int radiusX = 5;
        int radiusY = 3;
        int radiusZ = 5;

        BlockPos firstPos = pos.west(radiusX).below(radiusY).north(radiusZ);
        BlockPos secondPos = pos.east(radiusX).above(radiusY).south(radiusZ);

        Iterable<BlockPos> iterable = BlockPos.betweenClosed(firstPos, secondPos);

        for(BlockPos position : iterable){
            BlockEntity e = level.getBlockEntity(position);
            if(e instanceof AuralProjectorEntity projector && projector.isActive){
                event.setCanceled(true);
                int size = c.size();

                ServerLevel server = (ServerLevel) level;
                server.addFreshEntity(new ExperienceOrb(server, deceased.getX(), deceased.getY(), deceased.getZ(), 4 * size));
                break;
           }
        }
    }

    //-----------NBT-----------//

    private boolean isActive = true;

    public boolean isActive(){
        return isActive;
    }

    public void toggleActivity(){
        isActive = !isActive;
        setChanged();
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        this.isActive = tag.getBoolean("IsActive");
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tag.putBoolean("IsActive", isActive);
    }

    //sends CompoundTag out with nbt data
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();
        tag.putBoolean("IsActive", isActive);

        return tag;
    }
}
