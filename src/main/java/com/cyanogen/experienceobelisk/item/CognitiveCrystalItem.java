package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CognitiveCrystalItem extends Item {

    //Vacuums up xp orbs in a similar fashion to obelisks, ignoring obstacles in the way
    //Also used as a crafting ingredient

    public CognitiveCrystalItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putBoolean("isActive",false);
        return stack;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        if(player.isShiftKeyDown()){
            tag.putBoolean("isActive", !tag.getBoolean("isActive"));
        }

        return super.use(level, player, hand);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("isActive");
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {

        boolean isActive = stack.getOrCreateTag().getBoolean("isActive");

        if(entity instanceof Player player && isActive){

            BlockPos pos = player.blockPosition();
            double radius = Config.COMMON.range.get();
            AABB area = new AABB(
                    pos.getX() - radius,
                    pos.getY() - radius,
                    pos.getZ() - radius,
                    pos.getX() + radius,
                    pos.getY() + radius,
                    pos.getZ() + radius);

            List<ExperienceOrb> list = level.getEntitiesOfClass(ExperienceOrb.class, area);

            for(ExperienceOrb orb : list){
                if(orb.isAlive()){
                    player.giveExperiencePoints(orb.getValue());
                }
                orb.discard();
            }
        }
        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }

    //-----CUSTOM HOVER TEXT-----//

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        boolean isActive = pStack.getOrCreateTag().getBoolean("isActive");

        if(isActive){
            pTooltip.add(new TranslatableComponent("tooltip.experienceobelisk.cognitive_crystal.active"));
        }
        else{
            pTooltip.add(new TranslatableComponent("tooltip.experienceobelisk.cognitive_crystal.inactive"));
        }

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);

    }

}
