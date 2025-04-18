package com.cyanogen.experienceobelisk.utils;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.recipe.MolecularMetamorpherRecipe;
import com.cyanogen.experienceobelisk.recipe.jei.MolecularMetamorpherCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Creates a dummy recipe to pass into JEI's recipe handling. Actual population of the slots will occur in the category class.
     * See {@link MolecularMetamorpherCategory#setNameFormattingRecipe}
     */
    public static MolecularMetamorpherRecipe getEmptyNameFormattingRecipe(){
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ExperienceObelisk.MOD_ID, "item_name_formatting");
        return new MolecularMetamorpherRecipe(Ingredient.EMPTY, 0, Ingredient.EMPTY, 0, Ingredient.EMPTY, 0,
                ItemStack.EMPTY, 315, 60, id);

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
