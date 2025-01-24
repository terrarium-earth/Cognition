package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.utils.ExperienceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class FlagonPoseidonItem extends Item{

    public FlagonPoseidonItem(Properties p) {
        super(p);
    }

    //-----------BEHAVIOR-----------//

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace(), 1);
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);

        if(player != null && (player.isCreative() || ExperienceUtils.getTotalXp(player) >= 7) && !player.getCooldowns().isOnCooldown(this)){

            int k = player.isCreative() ? 0 : 1;

            if(state.isAir() || state.canBeReplaced(Fluids.WATER)){
                if(level.dimensionType().ultraWarm()){
                    Fluids.WATER.getFluidType().onVaporize(player, level, pos, null);
                }
                else{
                    level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                }

                player.getCooldowns().addCooldown(this, 10);
                player.giveExperiencePoints(-7 * k);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else if(state.getBlock() instanceof LiquidBlockContainer container && container.canPlaceLiquid(level, pos, state, Fluids.WATER)){
                container.placeLiquid(level, pos, state, Fluids.WATER.defaultFluidState());

                player.getCooldowns().addCooldown(this, 10);
                player.giveExperiencePoints(-7 * k);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

        }

        return super.useOn(context);
    }

}
