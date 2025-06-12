package com.cyanogen.experienceobelisk.registries;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.recipe.EmptyingRecipe;
import com.cyanogen.experienceobelisk.recipe.FillingRecipe;
import com.cyanogen.experienceobelisk.recipe.InfectingRecipe;
import com.cyanogen.experienceobelisk.recipe.MolecularMetamorpherRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterRecipes{

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ExperienceObelisk.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ExperienceObelisk.MOD_ID);

    public static final RegistryObject<RecipeSerializer<MolecularMetamorpherRecipe>> MOLECULAR_METAMORPHER_SERIALIZER =
            SERIALIZERS.register("molecular_metamorphosis", () -> MolecularMetamorpherRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeType<MolecularMetamorpherRecipe>> MOLECULAR_METAMORPHER_TYPE =
            TYPES.register("molecular_metamorphosis", () -> MolecularMetamorpherRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<InfectingRecipe>> INFECTING_SERIALIZER =
            SERIALIZERS.register("infecting", () -> InfectingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeType<InfectingRecipe>> INFECTING_TYPE =
            TYPES.register("infecting", () -> InfectingRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<FillingRecipe>> FILLING_SERIALIZER =
            SERIALIZERS.register("filling", () -> FillingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeType<FillingRecipe>> FILLING_TYPE =
            TYPES.register("filling", () -> FillingRecipe.Type.INSTANCE);

    public static final RegistryObject<RecipeSerializer<EmptyingRecipe>> EMPTYING_SERIALIZER =
            SERIALIZERS.register("emptying", () -> EmptyingRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeType<EmptyingRecipe>> EMPTYING_TYPE =
            TYPES.register("emptying", () -> EmptyingRecipe.Type.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
