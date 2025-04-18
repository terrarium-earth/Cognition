package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.gui.ExperienceObeliskMenu;
import com.cyanogen.experienceobelisk.gui.MolecularMetamorpherMenu;
import com.cyanogen.experienceobelisk.gui.PrecisionDispellerMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, ExperienceObelisk.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<PrecisionDispellerMenu>> PRECISION_DISPELLER_MENU = MENUS.register("precision_dispeller_menu",
            () -> new MenuType<>(PrecisionDispellerMenu::new, FeatureFlagSet.of()));

    public static final DeferredHolder<MenuType<?>, MenuType<ExperienceObeliskMenu>> EXPERIENCE_OBELISK_MENU = MENUS.register("experience_obelisk_menu",
            ()-> new MenuType<>(ExperienceObeliskMenu::new, FeatureFlagSet.of()));

    public static final DeferredHolder<MenuType<?>, MenuType<MolecularMetamorpherMenu>> MOLECULAR_METAMORPHER_MENU = MENUS.register("molecular_metamorpher_menu",
            ()-> new MenuType<>(MolecularMetamorpherMenu::new, FeatureFlagSet.of()));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
