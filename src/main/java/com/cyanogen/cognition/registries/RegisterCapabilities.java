package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.block_entities.ExperienceObeliskEntity;
import com.cyanogen.cognition.block_entities.MolecularMetamorpherEntity;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class RegisterCapabilities {

    public static void register(RegisterCapabilitiesEvent event){

        event.registerBlockEntity(ExperienceObeliskEntity.FLUID_HANDLER, RegisterBlockEntities.EXPERIENCE_OBELISK.get(),
                ExperienceObeliskEntity::getCapability);

        event.registerBlockEntity(MolecularMetamorpherEntity.ITEM_HANDLER, RegisterBlockEntities.MOLECULAR_METAMORPHER.get(),
                MolecularMetamorpherEntity::getCapability);
    }

}
