package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.gui.ExperienceObeliskMenu;
import com.cyanogen.experienceobelisk.gui.PrecisionDispellerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExperienceObelisk.MOD_ID);

    public static final RegistryObject<MenuType<PrecisionDispellerMenu>> PRECISION_DISPELLER_MENU = MENUS.register("precision_dispeller_menu",
            ()-> IForgeMenuType.create(PrecisionDispellerMenu::new));

    public static final RegistryObject<MenuType<ExperienceObeliskMenu>> EXPERIENCE_OBELISK_MENU = MENUS.register("experience_obelisk_menu",
            ()-> IForgeMenuType.create(ExperienceObeliskMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
