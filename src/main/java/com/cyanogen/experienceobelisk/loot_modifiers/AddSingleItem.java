package com.cyanogen.experienceobelisk.loot_modifiers;

import com.cyanogen.experienceobelisk.utils.MiscUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddSingleItem extends LootModifier {

    public static final Codec<AddSingleItem> CODEC =
            RecordCodecBuilder.create(instance -> codecStart(instance).and(instance.group(
                    ForgeRegistries.ITEMS.getCodec().fieldOf("id").forGetter(o -> o.item),
                    Codec.FLOAT.fieldOf("appear_chance").forGetter(o -> o.appearChance),
                    Codec.INT.fieldOf("min_quantity").forGetter(o -> o.min),
                    Codec.INT.fieldOf("max_quantity").forGetter(o -> o.max),
                    Codec.list(Codec.STRING).fieldOf("paths").forGetter(o -> o.paths)
                    ))
                    .apply(instance, AddSingleItem::new));

    public final Item item;
    public final float appearChance;
    public final int min;
    public final int max;
    public final List<String> paths;

    protected AddSingleItem(LootItemCondition[] conditionsIn, Item item, float appearChance, int min, int max, List<String> paths) {
        super(conditionsIn);
        this.item = item;
        this.appearChance = appearChance;
        this.min = min;
        this.max = max;
        this.paths = paths;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

        String contextPath = context.getQueriedLootTableId().getPath();

        for(String path : paths){
            if(contextPath.contains(path)){
                int count = MiscUtils.randomIntInclusive(min, max);
                for(int i = 0; i< count; i++){
                    generatedLoot.add(new ItemStack(item,1));
                }
            }
        }

        return generatedLoot;
    }

    @Override
    public Codec<AddSingleItem> codec() {
        return CODEC;
    }


}
