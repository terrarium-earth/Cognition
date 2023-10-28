package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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

import java.util.List;

public class EnlightenedAmuletItem extends Item{

    public EnlightenedAmuletItem(Properties p) {
        super(p);
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
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.2f,1f);
        }

        return super.use(level, player, hand);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("isActive");
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isCurrentItem) {

        boolean isActive = stack.getOrCreateTag().getBoolean("isActive");

        if(entity instanceof Player player && isActive && !level.isClientSide && level.getGameTime() % 5 ==0){

            final double radius = Config.COMMON.range.get();

            BlockPos pos = player.blockPosition();
            AABB area = new AABB(
                    pos.getX() - radius,
                    pos.getY() - radius,
                    pos.getZ() - radius,
                    pos.getX() + radius,
                    pos.getY() + radius,
                    pos.getZ() + radius);

            List<ExperienceOrb> list = level.getEntitiesOfClass(ExperienceOrb.class, area);
            int totalValue = 0;

            for(ExperienceOrb orb : list){
                if(orb.isAlive() && (totalValue + orb.value) <= 32767){
                    totalValue += orb.value;
                    orb.discard();
                }
            }

            if(totalValue > 0){
                ServerLevel server = (ServerLevel) level;
                ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, totalValue);
                server.addFreshEntity(orb);
            }


        }
        super.inventoryTick(stack, level, entity, slot, isCurrentItem);
    }

    //-----CUSTOM HOVER TEXT-----//

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> tooltip, TooltipFlag flag) {

        boolean isActive = pStack.getOrCreateTag().getBoolean("isActive");

        if(isActive){
            tooltip.add(new TranslatableComponent("tooltip.experienceobelisk.enlightened_amulet.active"));
        }
        else{
            tooltip.add(new TranslatableComponent("tooltip.experienceobelisk.enlightened_amulet.inactive"));
        }

        super.appendHoverText(pStack, pLevel, tooltip, flag);

    }
}
