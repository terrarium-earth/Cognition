package com.cyanogen.cognition.loot_modifiers;

import com.cyanogen.cognition.utils.MiscUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddSingleItem extends LootModifier {

    //Adds a single kind of item to the specified loot table

    //id -- the id of the item to add
    //appear_chance -- the chance of the item appearing in a given chest
    //min_quantity -- the minimum number of items to add
    //max_quantity -- the maximum number of items to add
    //bias -- whether the distribution is skewed towards more or less items. See com.cyanogen.experienceobelisk.utils.MiscUtils.weightedRandInt.
    // Set this to 0 for a uniform distribution
    //path -- the loot table to add the item to

    public static final MapCodec<AddSingleItem> CODEC =
            RecordCodecBuilder.mapCodec(instance -> codecStart(instance).and(instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(o -> o.item),
                    Codec.FLOAT.fieldOf("appear_chance").forGetter(o -> o.appearChance),
                    Codec.INT.fieldOf("min_quantity").forGetter(o -> o.min),
                    Codec.INT.fieldOf("max_quantity").forGetter(o -> o.max),
                    Codec.FLOAT.fieldOf("bias").forGetter(o -> o.bias),
                    Codec.STRING.fieldOf("path").forGetter(o -> o.path)
                    )).apply(instance, AddSingleItem::new));

    public final Item item;
    public final float appearChance;
    public final int min;
    public final int max;
    public final float bias;
    public final String path;

    public AddSingleItem(LootItemCondition[] conditionsIn, Item item, float appearChance, int min, int max, float bias, String path) {
        super(conditionsIn);
        this.item = item;
        this.appearChance = appearChance;
        this.min = min;
        this.max = max;
        this.bias = bias;
        this.path = path;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        String contextPath = context.getQueriedLootTableId().getPath();
        if(contextPath.contains(path) && Math.random() <= appearChance){
            int count = MiscUtils.weightedRandInt(min, max, bias);
            for(int i = 0; i < count; i++){
                generatedLoot.add(new ItemStack(item,1));
            }

            return generatedLoot;
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<AddSingleItem> codec() {
        return CODEC;
    }

}
