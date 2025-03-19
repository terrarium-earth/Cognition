package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.utils.MiscUtils;
import net.minecraft.core.BlockPos;
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

    public static void handleItem(ItemStackedOnOtherEvent event){
        ItemStack itemToRepair = event.getCarriedItem();
        ItemStack stackedOn = event.getStackedOnItem();
        Player player = event.getPlayer();

        ItemStack chippedAnvil = new ItemStack(Items.CHIPPED_ANVIL, 1);
        ItemStack anvil = new ItemStack(Items.ANVIL, 1);

        if(stackedOn.is(RegisterItems.MENDING_NEUROGEL.get())){

            if(itemToRepair.isDamaged()){
                int maxDurability = itemToRepair.getMaxDamage();
                int damage = itemToRepair.getDamageValue();
                int repairAmount = Math.max(maxDurability / 5, 200); //restores 20% of item max durability or 200 pts, whichever is higher

                stackedOn.shrink(1);
                itemToRepair.setDamageValue(Math.max(damage - repairAmount, 0));
                player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));
                event.setCanceled(true);
            }
            else if(itemToRepair.is(Items.CHIPPED_ANVIL)){
                setItem(anvil, event.getSlot(), player, stackedOn, itemToRepair);
                event.setCanceled(true);
            }
            else if(itemToRepair.is(Items.DAMAGED_ANVIL)){
                setItem(chippedAnvil, event.getSlot(), player, stackedOn, itemToRepair);
                event.setCanceled(true);
            }
        }

    }

    public static void setItem(ItemStack item, Slot slot, Player player, ItemStack stackedOn, ItemStack itemToRepair){

        stackedOn.shrink(1);
        player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));

        if(itemToRepair.getCount() == 1){
            slot.set(item);
        }
        else{
            player.addItem(item);
            itemToRepair.shrink(1);
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

            BlockState anvil = Blocks.ANVIL.withPropertiesOf(state);
            BlockState chipped = Blocks.CHIPPED_ANVIL.withPropertiesOf(state);

            if(state.is(Blocks.CHIPPED_ANVIL)){
                level.setBlockAndUpdate(pos, anvil);
            }
            else if(state.is(Blocks.DAMAGED_ANVIL)){
                level.setBlockAndUpdate(pos, chipped);
            }

            stack.shrink(1);
            player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));
        }

        return super.onItemUseFirst(stack, context);
    }

    //Repairs items by a set percentage of durability points when applied
    //Can be applied to items you wouldn't be able to otherwise mend, such as those with conflicting enchants, or those that don't accept mending
}
