package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GrindstoneBlock;

public class AstuteArtifactItem extends Item {

    public AstuteArtifactItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack item = context.getItemInHand();

        if(level.getBlockState(pos).getBlock() instanceof GrindstoneBlock && player != null){
            level.setBlockAndUpdate(pos, RegisterBlocks.PRECISION_DISPELLER.get().defaultBlockState());

            player.playSound(SoundEvents.ARMOR_EQUIP_DIAMOND, 1, 1);
            item.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else{
            return super.useOn(context);
        }
    }
}
