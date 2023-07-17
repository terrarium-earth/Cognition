package com.cyanogen.experienceobelisk.enchantment;

import com.cyanogen.experienceobelisk.registries.RegisterEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import java.util.Map;

public class AthenaBlessingEnchant extends Enchantment {

    public AthenaBlessingEnchant(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public Component getFullname(int pLevel) {
        MutableComponent mutablecomponent = new TranslatableComponent(this.getDescriptionId());
        mutablecomponent.withStyle(ChatFormatting.GREEN);

        if (pLevel != 1 || this.getMaxLevel() != 1) {
            mutablecomponent.append(" ").append(new TranslatableComponent("enchantment.level." + pLevel));
        }

        return mutablecomponent;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTradeable() { return false; }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {

        if(pStack.getItem() instanceof ArmorItem){
            return false;
        }
        else{
            return canApplyAtEnchantingTable(pStack);
        }
    }

    @Override
    protected boolean checkCompatibility(Enchantment pOther) {
        return pOther != this;
    }

    public static void itemDestroyed(PlayerDestroyItemEvent event) {

        //generates XP orbs when item is broken
        //Amount and value of orbs are random but scale with item durability + number of enchantments

        ItemStack item = event.getOriginal();
        Player player = event.getPlayer();
        Level l = player.getLevel();

        BlockPos pos = event.getPlayer().blockPosition();

        int level = EnchantmentHelper.getItemEnchantmentLevel(RegisterEnchantments.ATHENA_BLESSING.get(), item);

        if(level == 1 && !l.isClientSide){
            ServerLevel server = (ServerLevel) l;

            int durabilityBonus = Math.min(item.getMaxDamage(), 8000);
            int totalLevels = 0;

            for(Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(item).entrySet()){
                if(entry.getKey() != RegisterEnchantments.ATHENA_BLESSING.get()){
                    totalLevels = totalLevels + entry.getValue();
                }
            }

            double enchantmentBonus = Math.pow(1.177, Math.min(totalLevels, 50)) / 4 + 1;
            double xpReturn = durabilityBonus * (0.5 + Math.random() / 2) * enchantmentBonus;

            int count = (int) Math.floor(3 + Math.random()*8);
            int value = (int) Math.round(xpReturn / count);

            for(int i = 0; i <= count; i++){
                ExperienceOrb orb = new ExperienceOrb(server, pos.getX(), pos.getY(), pos.getZ(), value);
                server.addFreshEntity(orb);
            }
        }
    }
}
