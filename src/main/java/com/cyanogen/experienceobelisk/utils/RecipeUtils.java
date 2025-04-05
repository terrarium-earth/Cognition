package com.cyanogen.experienceobelisk.utils;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.recipe.MolecularMetamorpherRecipe;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.google.common.collect.ImmutableMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeUtils {

    public static List<Item> getValidDyes(){
        List<Item> validDyes = new ArrayList<>();
        validDyes.add(Items.WHITE_DYE);
        validDyes.add(Items.ORANGE_DYE);
        validDyes.add(Items.MAGENTA_DYE);
        validDyes.add(Items.LIGHT_BLUE_DYE);
        validDyes.add(Items.YELLOW_DYE);
        validDyes.add(Items.LIME_DYE);
        validDyes.add(Items.PINK_DYE);
        validDyes.add(Items.GRAY_DYE);
        validDyes.add(Items.LIGHT_GRAY_DYE);
        validDyes.add(Items.CYAN_DYE);
        validDyes.add(Items.PURPLE_DYE);
        validDyes.add(Items.BLUE_DYE);
        validDyes.add(Items.BROWN_DYE);
        validDyes.add(Items.GREEN_DYE);
        validDyes.add(Items.RED_DYE);
        validDyes.add(Items.BLACK_DYE);

        return validDyes;
    }

    public static int dyeColorToTextColor(int dyeColor){

        return switch (dyeColor) {
            case 0 -> 15; //white -> white
            case 1 -> 6; //orange -> gold
            case 2 -> 5; //magenta -> dark purple
            case 3 -> 11; //light blue -> aqua
            case 4 -> 14; //yellow -> yellow
            case 5 -> 10; //lime -> green
            case 6 -> 12; //pink -> red
            case 7 -> 7; //gray -> gray
            case 8 -> 9; //light gray -> blue
            case 9 -> 3; //cyan -> dark aqua
            case 10 -> 13; //purple -> light purple
            case 11 -> 1; //blue -> dark blue
            case 12 -> 8; //brown -> dark gray
            case 13 -> 2; //green -> dark green
            case 14 -> 4; //red -> dark red
            case 15 -> 0; //black -> black
            default -> 15;
        };
    }

    public static List<Item> getValidFormattingItems(){
        List<Item> validFormattingItems = new ArrayList<>();
        validFormattingItems.add(Items.END_CRYSTAL);
        validFormattingItems.add(Items.ECHO_SHARD);
        validFormattingItems.add(Items.TRIDENT);
        validFormattingItems.add(Items.NETHER_STAR);
        validFormattingItems.add(Items.TNT);

        return validFormattingItems;
    }

    public static char itemToFormat(int index){

        return switch (index) {
            case 0 -> 'k'; //end crystal -> obfuscated
            case 1 -> 'l'; //echo shard -> bold
            case 2 -> 'm'; //trident -> strikethrough
            case 3 -> 'n'; //nether star -> underline
            case 4 -> 'r'; //tnt -> reset
            default -> 'f';
        };

    }

    public static List<ItemStack> convertItemListToItemStackList(List<Item> itemList){
        List<ItemStack> itemStackList = new ArrayList<>();
        for(Item item : itemList){
            itemStackList.add(item.getDefaultInstance());
        }
        return itemStackList;
    }

    public static MolecularMetamorpherRecipe getEmptyNameFormattingRecipe(){
        Map<Ingredient, Tuple<Integer, Integer>> ingredientMap = new HashMap<>();
        ingredientMap.put(Ingredient.EMPTY, new Tuple<>(1,0));
        ingredientMap.put(Ingredient.of(Items.AIR), new Tuple<>(2,0));
        ingredientMap.put(Ingredient.of(Items.BEDROCK), new Tuple<>(3,0));
        ResourceLocation id = new ResourceLocation(ExperienceObelisk.MOD_ID, "item_name_formatting");
        return new MolecularMetamorpherRecipe(ImmutableMap.copyOf(ingredientMap), ItemStack.EMPTY, 315, 60, id);

    }

    public static List<ItemStack> getItemListWithCounts(Ingredient ingredient, int count){
        List<ItemStack> list = new ArrayList<>();

        for(ItemStack stack : ingredient.getItems()){
            ItemStack stack2 = stack.copy();
            stack2.setCount(count);
            list.add(stack2);
        }

        return list;
    }


}
