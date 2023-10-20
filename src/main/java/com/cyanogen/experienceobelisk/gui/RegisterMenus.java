package com.cyanogen.experienceobelisk.gui;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, ExperienceObelisk.MOD_ID);

    public static final RegistryObject<MenuType<ExperienceObeliskMenu>> EXPERIENCE_OBELISK_MENU = MENUS.register("experience_obelisk_menu",
            () -> IForgeMenuType.create(ExperienceObeliskMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
