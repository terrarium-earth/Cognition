package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ItemStackedOnOtherEvent;

public class NeurogelMendingItem extends Item {

    public NeurogelMendingItem(Properties p) {
        super(p);
    }

    final static int repairAmount = 200;

    public static ItemStack chippedAnvil = new ItemStack(Items.CHIPPED_ANVIL, 1);
    public static ItemStack anvil = new ItemStack(Items.ANVIL, 1);

    public static void handleItem(ItemStackedOnOtherEvent event){
        ItemStack itemToRepair = event.getCarriedItem();
        ItemStack stackedOn = event.getStackedOnItem();
        Player player = event.getPlayer();

        if(stackedOn.is(RegisterItems.MENDING_NEUROGEL.get()) && itemToRepair.isDamaged()){

            if(itemToRepair.isDamaged()){
                int damage = itemToRepair.getDamageValue();
                stackedOn.shrink(1);
                itemToRepair.setDamageValue(Math.max(damage - repairAmount, 0));
                player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));
            }
            else if(itemToRepair.is(Items.CHIPPED_ANVIL)){
                setItem(anvil, event.getSlot(), player, stackedOn, itemToRepair);
            }
            else if(itemToRepair.is(Items.DAMAGED_ANVIL)){
                setItem(chippedAnvil, event.getSlot(), player, stackedOn, itemToRepair);
            }

            event.setCanceled(true);
        }

    }

    public static void setItem(ItemStack item, Slot slot, Player player, ItemStack stackedOn, ItemStack itemToRepair){

        stackedOn.shrink(1);

        if(itemToRepair.getCount() == 1){
            slot.set(item);
            player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));
        }
        else{
            itemToRepair.shrink(1);
            player.addItem(item);
            player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        BlockState state = level.getBlockState(context.getClickedPos());

        if(player != null && player.isShiftKeyDown() &&
                (state.is(Blocks.CHIPPED_ANVIL) || state.is(Blocks.DAMAGED_ANVIL))){

            stack.shrink(1);
            if(state.is(Blocks.CHIPPED_ANVIL)){
                level.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState());
            }
            else if(state.is(Blocks.DAMAGED_ANVIL)){
                level.setBlockAndUpdate(pos, Blocks.CHIPPED_ANVIL.defaultBlockState());
            }

            player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));
        }

        return super.onItemUseFirst(stack, context);
    }

    //Repairs items by a set amount of durability points when applied
    //Can be applied to items you wouldn't be able to otherwise mend, such as those with conflicting enchants, or those that don't accept mending
}
