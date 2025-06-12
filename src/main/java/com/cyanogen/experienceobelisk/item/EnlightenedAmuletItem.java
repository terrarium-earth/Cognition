package com.cyanogen.experienceobelisk.item;

import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.Objects;

import static com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity.FROM_FOUNTAIN;
import static com.cyanogen.experienceobelisk.block_entities.bibliophage.bookshelves.AbstractInfectedBookshelfEntity.FROM_BOOKSHELF;
import static com.cyanogen.experienceobelisk.utils.ExperienceUtils.*;

public class EnlightenedAmuletItem extends ActivatableItem{

    private final boolean clumpsIsLoaded;

    public EnlightenedAmuletItem(Properties p) {
        super(p);
        clumpsIsLoaded = ModList.get().isLoaded("clumps");
    }

    @Override
    public void playActivationSound(Player player) {
        player.playSound(RegisterSounds.ENLIGHTENED_AMULET_ACTIVATE.get(), 0.2f,1f);
    }

    @Override
    public void playDeactivationSound(Player player) {
        player.playSound(RegisterSounds.ENLIGHTENED_AMULET_DEACTIVATE.get(), 0.2f,0.8f);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isCurrentItem) {

        if(entity instanceof Player player && isActive(stack) && !level.isClientSide && level.getGameTime() % 10 == 0){

            final double radius = Config.COMMON.amuletRange.get();

            Vec3 pos = player.position();
            AABB area = new AABB(
                    pos.x() - radius,
                    pos.y() - radius,
                    pos.z() - radius,
                    pos.x() + radius,
                    pos.y() + radius,
                    pos.z() + radius);

            List<ExperienceOrb> list = level.getEntitiesOfClass(ExperienceOrb.class, area);

            int totalValue = 0;
            int valueLimit = Integer.MAX_VALUE - 1;
            int collectionLimit = 64;
            int collect = Math.min(collectionLimit, list.size());

            if(!list.isEmpty()){
                for(int i = 0; i < collect; i++) {

                    ExperienceOrb orb = list.get(i);
                    CompoundTag tag = new CompoundTag();
                    orb.addAdditionalSaveData(tag);

                    boolean spawnedFromFountain = orb.hasCustomName() && Objects.equals(orb.getCustomName(), FROM_FOUNTAIN);
                    boolean ignoreFountain = Config.COMMON.amuletIgnoresFountainOrbs.get();
                    boolean spawnedFromBookshelf = orb.hasCustomName() && Objects.equals(orb.getCustomName(), FROM_BOOKSHELF);
                    boolean ignoreBookshelf = Config.COMMON.amuletIgnoresBookshelfOrbs.get();
                    boolean shouldCollect = !(ignoreFountain && spawnedFromFountain) && !(ignoreBookshelf && spawnedFromBookshelf);

                    if(shouldCollect && !orb.isRemoved()){
                        int value = clumpsIsLoaded ? getClumpedOrbValue(orb) : getOrbValue(orb);
                        if(totalValue + value > valueLimit) break;
                        totalValue += value;
                        orb.discard();
                    }
                }

                ServerLevel server = (ServerLevel) level;

                if(totalValue > 0){
                    ExperienceOrb orb = new ExperienceOrb(server, pos.x(), pos.y(), pos.z(), totalValue);
                    server.addFreshEntity(orb);
                }

                if(stack.getHoverName().getString().equals("Debug")){
                    sendDebugMessage(player, collect, totalValue);
                }
            }
        }

        super.inventoryTick(stack, level, entity, slot, isCurrentItem);
    }

    public void sendDebugMessage(Player player, int orbsCollected, int totalValue){
        player.sendSystemMessage(Component.literal(
                "----- [Amulet] -----" + "\n"
                        + "Clumps installed: " + clumpsIsLoaded + "\n"
                        + orbsCollected + " orbs collected with total value " + totalValue + " (" + xpToLevels(totalValue) + " levels)" + "\n"
                        + "Player levels: " + player.experienceLevel
        ));
    }

}
