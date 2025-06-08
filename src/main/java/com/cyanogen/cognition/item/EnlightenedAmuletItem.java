package com.cyanogen.cognition.item;

import com.cyanogen.cognition.config.Config;
import com.cyanogen.cognition.registries.RegisterSounds;
import com.cyanogen.cognition.utils.ItemUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.Objects;

import static com.cyanogen.cognition.block_entities.ExperienceFountainEntity.FROM_FOUNTAIN;
import static com.cyanogen.cognition.block_entities.bibliophage.bookshelves.AbstractInfectedBookshelfEntity.FROM_BOOKSHELF;

public class EnlightenedAmuletItem extends ActivatableItem{

    public final boolean clumpsIsLoaded;

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

        boolean isActive = ItemUtils.getCustomDataTag(stack).getBoolean("isActive");

        if(entity instanceof Player player && isActive && !level.isClientSide && level.getGameTime() % 10 == 0){

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
            if(!list.isEmpty()){

                for(int i = 0; i < Math.min(30,list.size()); i++) {

                    ExperienceOrb orb = list.get(i);
                    CompoundTag tag = new CompoundTag();
                    orb.addAdditionalSaveData(tag);

                    boolean spawnedFromFountain = orb.hasCustomName() && Objects.equals(orb.getCustomName(), FROM_FOUNTAIN);
                    boolean ignoreFountain = Config.COMMON.amuletIgnoresFountainOrbs.get();
                    boolean spawnedFromBookshelf = orb.hasCustomName() && Objects.equals(orb.getCustomName(), FROM_BOOKSHELF);
                    boolean ignoreBookshelf = Config.COMMON.amuletIgnoresBookshelfOrbs.get();
                    boolean shouldCollect = !(ignoreFountain && spawnedFromFountain) && !(ignoreBookshelf && spawnedFromBookshelf);

                    if(shouldCollect){
                        int value = clumpsIsLoaded ? getClumpedOrbValue(orb, tag) :
                                orb.value * tag.getInt("Count");

                        totalValue += value;
                        orb.discard();
                    }
                }

                ServerLevel server = (ServerLevel) level;

                if(totalValue <= 32767 && totalValue != 0){
                    ExperienceOrb orb = new ExperienceOrb(server, pos.x(), pos.y(), pos.z(), totalValue);
                    server.addFreshEntity(orb);
                }
                else if(totalValue > 32767){ //edge case if total value of orbs exceeds 32767
                    while(totalValue > 0){
                        int v = Math.min(totalValue, 32767);
                        ExperienceOrb orb = new ExperienceOrb(server, pos.x(), pos.y(), pos.z(), v);
                        server.addFreshEntity(orb);
                        totalValue = totalValue - v;
                    }
                }
            }
        }

        super.inventoryTick(stack, level, entity, slot, isCurrentItem);
    }

    public int getClumpedOrbValue(ExperienceOrb orb, CompoundTag tag){

        //gets orb values directly from clumpedMap rather than orb.value if Clumps is installed

        int totalValue = 0;

        if(tag.contains("clumpedMap")){
            CompoundTag clumpedMap = tag.getCompound("clumpedMap");

            for(String value : clumpedMap.getAllKeys()){
                totalValue += clumpedMap.getInt(value) * Integer.parseInt(value);
            }
        }
        else{
            totalValue = orb.value * tag.getInt("Count");
        }
        return totalValue;
    }

}
