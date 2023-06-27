package com.cyanogen.experienceobelisk.block_entities;

import net.minecraft.core.BlockPos;
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

    private boolean isActive = false;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(level.getBlockEntity(pos) instanceof AuralProjectorEntity projector){

            BlockEntity e = level.getBlockEntity(projector.getBoundPos());

            projector.isActive = level.hasNeighborSignal(pos)
                    && projector.isBound
                    && e instanceof ExperienceObeliskEntity obelisk
                    && obelisk.getFluidAmount() > 0;

            if(level.getGameTime() % 10 == 0 && projector.isActive && e != null){

                ExperienceObeliskEntity obelisk = (ExperienceObeliskEntity) e;
                obelisk.drain(4);
                level.sendBlockUpdated(obelisk.getBlockPos(), obelisk.getBlockState(), obelisk.getBlockState(), 2);
            }
        }
    }

    public static void livingDropsEvent(LivingDropsEvent event){
        LivingEntity deceased = event.getEntityLiving();
        Collection<ItemEntity> c = event.getDrops();
        Level level = deceased.getLevel();
        BlockPos pos = deceased.blockPosition();

        if(!c.isEmpty() && level.isLoaded(pos) && !level.isClientSide){

            int radiusX = 4;
            int radiusY = 2;
            int radiusZ = 4;

            BlockPos firstPos = pos.west(radiusX).below(radiusY).north(radiusZ);
            BlockPos secondPos = pos.east(radiusX).above(radiusY).south(radiusZ);

            Iterable<BlockPos> iterable = BlockPos.betweenClosed(firstPos, secondPos);

            for(BlockPos position : iterable){
                BlockEntity e = level.getBlockEntity(position);
                if(e instanceof AuralProjectorEntity projector && projector.isActive){
                    event.setCanceled(true);
                    int size = c.size();

                    ServerLevel server = (ServerLevel) level;
                    server.addFreshEntity(new ExperienceOrb(server, deceased.getX(), deceased.getY(), deceased.getZ(), 10 * size));
                    break;
                }
            }
        }
    }

}
