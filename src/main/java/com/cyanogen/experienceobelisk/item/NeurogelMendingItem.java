package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.registries.RegisterTags;
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

    public int maxRepairPoints(){
        return 320;
    }

    public double maxRepairPercentage(){
        return 0.25;
    }

    public static void handleItem(ItemStackedOnOtherEvent event){
        //not a mistake:
        //in this version the items are inverted for some reason
        //(mojang fixed it in 1.21.1)

        ItemStack holding = event.getStackedOnItem();
        ItemStack itemToRepair = event.getCarriedItem();
        Player player = event.getPlayer();

        if(holding.getItem() instanceof NeurogelMendingItem neurogel){

            if(itemToRepair.isDamaged() && !itemToRepair.is(RegisterTags.Items.NEUROGEL_BLACKLISTED)){
                int maxDurability = itemToRepair.getMaxDamage();
                int damage = itemToRepair.getDamageValue();
                float repairFactor = itemToRepair.is(RegisterTags.Items.COGNITIVE_SET) ? 1.25f : 1.0f;
                int repairAmount = (int) (Math.max(maxDurability * neurogel.maxRepairPercentage(), neurogel.maxRepairPoints()) * repairFactor);

                holding.shrink(1);
                itemToRepair.setDamageValue(Math.max(damage - repairAmount, 0));
                player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));
                event.setCanceled(true);
            }
            else if(itemToRepair.is(Items.CHIPPED_ANVIL) || itemToRepair.is(Items.DAMAGED_ANVIL)){
                neurogel.handleAnvilItem(event.getSlot(), player, holding, itemToRepair, event);
            }
        }

    }

    public void handleAnvilItem(Slot slot, Player player, ItemStack stackedOn, ItemStack itemToRepair, ItemStackedOnOtherEvent event){

        stackedOn.shrink(1);
        player.playSound(RegisterSounds.NEUROGEL_APPLY.get(), 0.75f, MiscUtils.randomInRange(0.8f, 1.2f));

        ItemStack result = itemToRepair.is(Items.CHIPPED_ANVIL) ? Items.ANVIL.getDefaultInstance() : Items.CHIPPED_ANVIL.getDefaultInstance();

        if(itemToRepair.getCount() == 1){
            slot.set(result);
        }
        else{
            player.addItem(result);
            itemToRepair.shrink(1);
        }
        event.setCanceled(true);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        BlockState state = level.getBlockState(context.getClickedPos());

        if(player != null && (state.is(Blocks.CHIPPED_ANVIL) || state.is(Blocks.DAMAGED_ANVIL))){

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

            return InteractionResult.CONSUME;
        }

        return super.onItemUseFirst(stack, context);
    }

}
