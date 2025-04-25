package com.cyanogen.cognition.registries;

import com.cyanogen.cognition.Cognition;
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

    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
