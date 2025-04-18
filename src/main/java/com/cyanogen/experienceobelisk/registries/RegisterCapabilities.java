package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.block_entities.MolecularMetamorpherEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class RegisterCapabilities {

    //----- BLOCKS -----//

    //fluids

    public static final BlockCapability<IFluidHandler, Direction> EXPERIENCE_OBELISK_FLUID_HANDLER =
            BlockCapability.create(ResourceLocation.fromNamespaceAndPath(ExperienceObelisk.MOD_ID, "experience_obelisk"), IFluidHandler.class, Direction.class);

    //items

    public static final BlockCapability<IItemHandler, Direction> MOLECULAR_METAMORPHER_ITEM_HANDLER =
            BlockCapability.create(ResourceLocation.fromNamespaceAndPath(ExperienceObelisk.MOD_ID, "molecular_metamorpher"), IItemHandler.class, Direction.class);

    //----- REGISTER -----//

    public static void registerBlocks(RegisterCapabilitiesEvent event) {
        event.registerBlock(EXPERIENCE_OBELISK_FLUID_HANDLER, ExperienceObeliskEntity::getCapability, RegisterBlocks.EXPERIENCE_OBELISK.get());
        event.registerBlock(MOLECULAR_METAMORPHER_ITEM_HANDLER, MolecularMetamorpherEntity::getCapability, RegisterBlocks.MOLECULAR_METAMORPHER.get());
    }

    public static void registerBlockEntities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(EXPERIENCE_OBELISK_FLUID_HANDLER, RegisterBlockEntities.EXPERIENCE_OBELISK.get(), ExperienceObeliskEntity::getCapability);
        event.registerBlockEntity(MOLECULAR_METAMORPHER_ITEM_HANDLER, RegisterBlockEntities.MOLECULAR_METAMORPHER.get(), MolecularMetamorpherEntity::getCapability);
    }

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        registerBlocks(event);
        registerBlockEntities(event);
    }

}
