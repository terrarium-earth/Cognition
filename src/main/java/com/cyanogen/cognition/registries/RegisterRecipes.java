package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.recipe.EmptyingRecipe;
import com.cyanogen.cognition.recipe.FillingRecipe;
import com.cyanogen.cognition.recipe.InfectingRecipe;
import com.cyanogen.cognition.recipe.MolecularMetamorpherRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterRecipes{

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Cognition.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Cognition.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MolecularMetamorpherRecipe>> MOLECULAR_METAMORPHER_SERIALIZER =
            SERIALIZERS.register("molecular_metamorphosis", () -> MolecularMetamorpherRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<MolecularMetamorpherRecipe>> MOLECULAR_METAMORPHER_TYPE =
            TYPES.register("molecular_metamorphosis", () -> MolecularMetamorpherRecipe.Type.INSTANCE);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<InfectingRecipe>> INFECTING_SERIALIZER =
            SERIALIZERS.register("infecting", () -> InfectingRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<InfectingRecipe>> INFECTING_TYPE =
            TYPES.register("infecting", () -> InfectingRecipe.Type.INSTANCE);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FillingRecipe>> FILLING_SERIALIZER =
            SERIALIZERS.register("filling", () -> FillingRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<FillingRecipe>> FILLING_TYPE =
            TYPES.register("filling", () -> FillingRecipe.Type.INSTANCE);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<EmptyingRecipe>> EMPTYING_SERIALIZER =
            SERIALIZERS.register("emptying", () -> EmptyingRecipe.Serializer.INSTANCE);

    public static final DeferredHolder<RecipeType<?>, RecipeType<EmptyingRecipe>> EMPTYING_TYPE =
            TYPES.register("emptying", () -> EmptyingRecipe.Type.INSTANCE);

    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
