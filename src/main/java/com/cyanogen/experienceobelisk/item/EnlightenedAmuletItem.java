package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.Vec3;
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
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        if(player.isShiftKeyDown()){
            boolean isActive = tag.getBoolean("isActive");
            if(isActive){
                tag.putBoolean("isActive", false);
                player.playSound(RegisterSounds.ENLIGHTENED_AMULET_DEACTIVATE.get(), 0.2f,0.8f);
            }
            else{
                tag.putBoolean("isActive", true);
                player.playSound(RegisterSounds.ENLIGHTENED_AMULET_ACTIVATE.get(), 0.2f,1f);
            }

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

            Vec3 pos = player.position();
            AABB area = new AABB(
                    pos.x() - radius,
                    pos.y() - radius,
                    pos.z() - radius,
                    pos.x() + radius,
                    pos.y() + radius,
                    pos.z() + radius);


            if(level.getGameTime() % 5 == 0){
                List<ExperienceOrb> list = level.getEntitiesOfClass(ExperienceOrb.class, area);
                int totalValue = 0;

                if(!list.isEmpty()){
                    for(ExperienceOrb orb : list){
                        totalValue += orb.value;
                        orb.discard();
                    }

                    ServerLevel server = (ServerLevel) level;

                    if(totalValue < 32768){
                        ExperienceOrb orb = new ExperienceOrb(server, pos.x(), pos.y(), pos.z(), totalValue);
                        server.addFreshEntity(orb);
                    }
                    else{ //kinda ridiculous edge case but wtv
                        while(totalValue > 0){
                            int v = Math.min(totalValue, 32767);
                            ExperienceOrb orb = new ExperienceOrb(server, pos.x(), pos.y(), pos.z(), v);
                            server.addFreshEntity(orb);
                            totalValue = totalValue - v;
                        }
                    }

                }
            }
        }

        super.inventoryTick(stack, level, entity, slot, isCurrentItem);
    }

    //-----CUSTOM HOVER TEXT-----//

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {

        boolean isActive = stack.getOrCreateTag().getBoolean("isActive");

        if(isActive){
            tooltip.add(Component.translatable("tooltip.experienceobelisk.enlightened_amulet.active"));
        }
        else{
            tooltip.add(Component.translatable("tooltip.experienceobelisk.enlightened_amulet.inactive"));
        }

        super.appendHoverText(stack, level, tooltip, flag);

    }
}
