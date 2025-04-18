package com.cyanogen.experienceobelisk.block.bibliophage.agar;

import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractAgarBlock extends HalfTransparentBlock {

    private final int lightLevel;

    public AbstractAgarBlock(int lightLevel, boolean emissiveRendering) {
        super(Properties.ofFullCopy(Blocks.SLIME_BLOCK) //todo: check if this overrides drops
                .noOcclusion()
                .isViewBlocking((state,getter,pos)->false)
                .lightLevel(value -> 0)
                .emissiveRendering((state,getter,pos)-> emissiveRendering));

        this.lightLevel = lightLevel;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return Config.COMMON.agarEmitsLight.get() ? lightLevel : 0;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float damage) { }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {

        VoxelShape shape = super.getCollisionShape(state, getter, pos, context);

        if(context instanceof EntityCollisionContext entityCollisionContext){
            Entity e = entityCollisionContext.getEntity();
            if(e instanceof ExperienceOrb){
                return Shapes.empty();
            }
            else if(e instanceof ItemEntity item && Config.COMMON.agarPermeableToDust.get()){
                return item.getItem().is(RegisterItems.FORGOTTEN_DUST.get()) ? Shapes.empty() : shape;
            }
        }
        return shape;
    }

}
