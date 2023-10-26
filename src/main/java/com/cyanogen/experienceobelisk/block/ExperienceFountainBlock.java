package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExperienceFountainBlock extends Block implements EntityBlock {

    public ExperienceFountainBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(8f)
                .noOcclusion()
                .lightLevel(pLightEmission -> 5)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);

        List<Item> acceptedItems = new ArrayList<>();
        acceptedItems.add(Items.BUCKET);
        acceptedItems.add(RegisterItems.COGNITIUM_BUCKET.get());
        acceptedItems.add(Items.EXPERIENCE_BOTTLE);
        acceptedItems.add(Items.GLASS_BOTTLE);

        if(entity instanceof ExperienceFountainEntity fountain){

            if(fountain.isBound){

                BlockPos boundPos = fountain.getBoundPos();
                BlockEntity e = level.getBlockEntity(boundPos);

                if(heldItem.is(RegisterItems.ATTUNEMENT_STAFF.get())){
                    player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.reveal_bound_pos",
                            new TextComponent(boundPos.toShortString()).withStyle(ChatFormatting.GREEN)), true);

                    return InteractionResult.sidedSuccess(true);
                }
                else if(acceptedItems.contains(heldItem.getItem()) && e instanceof ExperienceObeliskEntity obelisk){
                    handleExperienceItem(heldItem, player, hand, obelisk);
                    return InteractionResult.sidedSuccess(true);
                }
            }

            fountain.cycleActivityState();
            TextComponent message = new TextComponent("Experience Fountain set to: ");

            switch (fountain.getActivityState()) {
                case 0 -> message.append(new TextComponent("Slow").withStyle(ChatFormatting.RED));
                case 1 -> message.append(new TextComponent("Moderate").withStyle(ChatFormatting.YELLOW));
                case 2 -> message.append(new TextComponent("Fast").withStyle(ChatFormatting.GREEN));
                case 3 -> message.append(new TextComponent("Hyperspeed").withStyle(ChatFormatting.AQUA));
            }
            player.displayClientMessage(message, true);
            level.sendBlockUpdated(pos, state, state, 2);

        }
        return InteractionResult.CONSUME;
    }

    public void handleExperienceItem(ItemStack heldItem, Player player, InteractionHand hand, ExperienceObeliskEntity obelisk){

        Item cognitiumBucketItem = RegisterItems.COGNITIUM_BUCKET.get();
        ItemStack cognitiumBucket = new ItemStack(cognitiumBucketItem, 1);
        ItemStack experienceBottle = new ItemStack(Items.EXPERIENCE_BOTTLE, 1);
        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE, 1);

        if(heldItem.is(Items.BUCKET) && obelisk.getFluidAmount() >= 1000){

            if(!player.isCreative()){
                heldItem.shrink(1);

                if(heldItem.isEmpty()){
                    player.setItemInHand(hand, cognitiumBucket);
                }
                else if(!player.addItem(cognitiumBucket)){     //if player inventory is full
                    player.drop(cognitiumBucket, false);
                }

            }

            obelisk.drain(1000);
            player.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
        }
        else if(heldItem.is(cognitiumBucketItem) && obelisk.getSpace() >= 1000){

            if(!player.isCreative()){
                heldItem.shrink(1);
                player.setItemInHand(hand, new ItemStack(Items.BUCKET, 1));
            }

            obelisk.fill(1000);
            player.playSound(SoundEvents.BUCKET_EMPTY, 1f, 1f);
        }
        else if(heldItem.is(Items.GLASS_BOTTLE) && obelisk.getFluidAmount() >= 140){

            if(!player.isCreative()){
                heldItem.shrink(1);

                if(heldItem.isEmpty()){
                    player.setItemInHand(hand, experienceBottle);
                }
                else if(!player.addItem(experienceBottle)){     //if player inventory is full
                    player.drop(experienceBottle, false);
                }

            }

            obelisk.drain(140);
            player.playSound(SoundEvents.BOTTLE_FILL, 1f, 1f);
        }
        else if(heldItem.is(Items.EXPERIENCE_BOTTLE) && obelisk.getSpace() >= 140){

            if(!player.isCreative()){
                heldItem.shrink(1);

                if(heldItem.isEmpty()){
                    player.setItemInHand(hand, glassBottle);
                }
                else if(!player.addItem(glassBottle)){     //if player inventory is full
                    player.drop(glassBottle, false);
                }
            }

            obelisk.fill(140);
            player.playSound(SoundEvents.BOTTLE_EMPTY, 1f, 1f);
        }

        //potentially wanna make it so any fluid container item works

    }


    VoxelShape center = Shapes.create(new AABB(4.5 / 16D,0 / 16D,4.5 / 16D,11.5 / 16D,8.5 / 16D,11.5 / 16D));
    VoxelShape shape1 = Shapes.create(new AABB(2 / 16D,1.3 / 16D,4.6 / 16D,14 / 16D,2.3 / 16D,11.4 / 16D));
    VoxelShape shape2 = Shapes.create(new AABB(4.6 / 16D,1.3 / 16D,2 / 16D,11.4 / 16D,2.3 / 16D,14 / 16D));
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.join(Shapes.join(center, shape1, BooleanOp.OR), shape2, BooleanOp.OR).optimize();
    }


    public ItemStack stack;
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof ExperienceFountainEntity entity && player.hasCorrectToolForDrops(state)) {

                stack = new ItemStack(RegisterItems.EXPERIENCE_FOUNTAIN_ITEM.get(), 1);
                entity.saveToItem(stack);
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        if(stack != null){
            drops.add(stack);
        }
        return drops;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }


    //-----BLOCK ENTITY-----//

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == RegisterBlockEntities.EXPERIENCEFOUNTAIN_BE.get() ? ExperienceFountainEntity::tick : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.EXPERIENCEFOUNTAIN_BE.get().create(pos, state);
    }

}