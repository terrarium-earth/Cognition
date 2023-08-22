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
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnlightenedAmuletItem extends Item{

    public EnlightenedAmuletItem(Properties pProperties) {
        super(pProperties);
    }

    public double radius = Config.COMMON.range.get();

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
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {

        boolean isActive = stack.getOrCreateTag().getBoolean("isActive");

        if(entity instanceof Player player && isActive && !level.isClientSide && level.getGameTime() % 3 ==0){

            ServerLevel server = (ServerLevel) level;

            BlockPos pos = player.blockPosition();
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
                    server.addFreshEntity(new ExperienceOrb(server, player.getX(), player.getY(), player.getZ(), orb.value));
                }
                orb.discard();
            }
        }
        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }

    //-----CUSTOM HOVER TEXT-----//

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        boolean isActive = pStack.getOrCreateTag().getBoolean("isActive");

        if(isActive){
            pTooltip.add(new TranslatableComponent("tooltip.experienceobelisk.enlightened_amulet.active"));
        }
        else{
            pTooltip.add(new TranslatableComponent("tooltip.experienceobelisk.enlightened_amulet.inactive"));
        }

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);

    }
}
