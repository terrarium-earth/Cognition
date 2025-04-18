package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.block_entities.PrecisionDispellerEntity;
import com.cyanogen.experienceobelisk.registries.RegisterMenus;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PrecisionDispellerMenu extends AbstractContainerMenu {

    public final SimpleContainer container = new SimpleContainer(2);
    private ContainerData blockPositionData;
    public final Player player;

    //constructor used by client
    public PrecisionDispellerMenu(int id, Inventory inventory) {
        this(id, inventory, null);

        //data slots (client)
        this.blockPositionData = new SimpleContainerData(3);
        this.addDataSlots(blockPositionData);
    }

    //constructor used by server
    public PrecisionDispellerMenu(int id, Inventory inventory, @Nullable BlockPos pos) {
        super(RegisterMenus.PRECISION_DISPELLER_MENU.get(), id);
        this.player = inventory.player;

        //data slots (server)
        if(pos != null){
            blockPositionData = new SimpleContainerData(3);
            blockPositionData.set(0, pos.getX());
            blockPositionData.set(1, pos.getY());
            blockPositionData.set(2, pos.getZ());
            this.addDataSlots(blockPositionData);

            sendAllDataToRemote();
        }
        else{
            blockPositionData = new SimpleContainerData(3);
        }

        //item slots
        this.addSlot(new Slot(this.container, 0, 17, 18){
            @Override
            public void onTake(Player player, ItemStack stack) {
                container.setItem(1, ItemStack.EMPTY);
                super.onTake(player, stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.container, 1, 17, 52){

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                Level level = player.level();

                if(!level.isClientSide) {
                    assert pos != null;
                    if (level.getBlockEntity(pos) instanceof PrecisionDispellerEntity dispeller) {
                        handleExperience(container.getItem(0), stack, level, player, dispeller);
                        handleAnimation(level, pos);
                    }
                }
                player.playSound(SoundEvents.GRINDSTONE_USE, 1, 1);

                container.setItem(0, ItemStack.EMPTY);
                super.onTake(player, stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public BlockPos getBlockPos(){
        return new BlockPos(blockPositionData.get(0), blockPositionData.get(1), blockPositionData.get(2));
    }

    //-----BEHAVIOR-----//

    public void handleExperience(ItemStack inputItem, ItemStack outputItem, Level level, Player player, PrecisionDispellerEntity dispellerServer){

        player.playSound(SoundEvents.GRINDSTONE_USE, 0.7f, 1);

        if(!level.isClientSide){
            ServerLevel server = (ServerLevel) level;
            Holder<Enchantment> removed = null;
            int enchLevel = 0;

            ItemEnchantments inputEnchantments = inputItem.getTagEnchantments();
            ItemEnchantments outputEnchantments = outputItem.getTagEnchantments();

            for(Object2IntMap.Entry<Holder<Enchantment>> entry : inputEnchantments.entrySet()){

                if(!outputEnchantments.keySet().contains(entry.getKey())){
                    removed = entry.getKey();
                    enchLevel = entry.getIntValue();
                    break;
                }
            }

            if(removed != null){
                if(dispellerServer.isBound && server.getBlockEntity(dispellerServer.getBoundPos()) instanceof ExperienceObeliskEntity obelisk){
                    handleExperienceBound(removed, enchLevel, server, obelisk, player);
                }
                else{
                    handleExperienceUnbound(removed, enchLevel, server, player);
                }

            }
        }
    }

    public void handleExperienceBound(Holder<Enchantment> removed, int enchLevel, ServerLevel server, ExperienceObeliskEntity obelisk, Player player){

        BlockPos pos = getBlockPos();

        if(removed.is(EnchantmentTags.CURSE)){
            if(obelisk.getFluidAmount() >= 1395 * 20){
                obelisk.drain(1395 * 20);
            }
            else{
                int remainder = 1395 - obelisk.getFluidAmount() / 20;
                obelisk.setFluid(0);
                player.giveExperiencePoints(-remainder);
            }
        }
        else{
            int points = removed.value().getMinCost(enchLevel);
            if(obelisk.getSpace() / 20 < points){
                int remainder = points - obelisk.getSpace() / 20;
                obelisk.setFluid(ExperienceObeliskEntity.capacity);

                ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, remainder);
                server.addFreshEntity(orb);
            }
            else{
                obelisk.fill(points * 20);
            }
        }
    }

    public void handleExperienceUnbound(Holder<Enchantment> removed, int enchLevel, ServerLevel server, Player player){

        BlockPos pos = getBlockPos();

        if(removed.is(EnchantmentTags.CURSE)){
            player.giveExperiencePoints(-1395); //30 base levels
        }
        else{
            int points = removed.value().getMinCost(enchLevel);
            ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, points);
            server.addFreshEntity(orb);
        }
    }

    public void handleAnimation(Level level, BlockPos pos){
        if(level.getBlockEntity(pos) instanceof PrecisionDispellerEntity dispeller){
            dispeller.triggerUseAnimation();
        }
    }

    @Override
    public void removed(Player player) {

        ItemStack item = container.getItem(0);
        if(!player.addItem(item)){
            player.drop(item, false);
        }
        super.removed(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.position().distanceTo(Vec3.atCenterOf(getBlockPos())) < 7;
    }

    public ItemStack quickMoveStack(Player player, int index) {

        BlockPos pos = getBlockPos();

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            if(index == 0){
                container.setItem(1, ItemStack.EMPTY);
            }
            else if(index == 1){
                if(!player.level().isClientSide && player.level().getBlockEntity(pos) instanceof PrecisionDispellerEntity dispeller){
                    handleExperience(slots.get(0).getItem(),slots.get(1).getItem(), player.level(), player, dispeller);
                    container.setItem(0, ItemStack.EMPTY);
                    handleAnimation(player.level(), pos);
                }
            }

            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

}
