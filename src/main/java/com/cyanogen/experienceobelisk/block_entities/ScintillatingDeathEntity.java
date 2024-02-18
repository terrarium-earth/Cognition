package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ScintillatingDeathEntity extends BlockEntity {

    public ScintillatingDeathEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.PRECISIONDISPELLER_BE.get(), pos, state);
    }

    public Player owner;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof ScintillatingDeathEntity deathCloud){

            int radius = 1;
            AABB area = new AABB(
                    pos.getX() - radius,
                    pos.getY() - radius,
                    pos.getZ() - radius,
                    pos.getX() + radius,
                    pos.getY() + radius,
                    pos.getZ() + radius);

            List<Entity> list = level.getEntities(null, area);

            if(!list.isEmpty()){
                for(Entity e : list){
                    if(e instanceof LivingEntity){
                        if(deathCloud.owner != null){
                            e.hurt(e.damageSources().playerAttack(deathCloud.owner), 2.0f);
                        }
                        else{
                            e.hurt(e.damageSources().cactus(), 2.0f);
                        }
                    }
                }
            }
        }

    }

    public void setOwner(Player player){
        this.owner = player;
        setChanged();
    }

}
