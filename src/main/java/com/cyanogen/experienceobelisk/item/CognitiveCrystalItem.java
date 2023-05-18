package com.cyanogen.experienceobelisk.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

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
            double radius = 3.5;
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


}
