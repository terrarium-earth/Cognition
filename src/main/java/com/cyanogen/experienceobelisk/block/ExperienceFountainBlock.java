package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

public class ExperienceFountainBlock extends ExperienceReceivingBlock implements EntityBlock {

    public ExperienceFountainBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(9f)
                .destroyTime(1.2f)
                .explosionResistance(9f)
                .noOcclusion()
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if(super.useItemOn(stack, state, level, pos, player, hand, hitResult) != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION){
            return ItemInteractionResult.CONSUME;
        }

        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);

        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(heldItem.copy()).orElse(null);

        if(entity instanceof ExperienceFountainEntity fountain){

            if(fountain.isBound && level.getBlockEntity(fountain.getBoundPos()) instanceof ExperienceObeliskEntity obelisk){

                if(heldItem.getItem() == Items.EXPERIENCE_BOTTLE || heldItem.getItem() == Items.GLASS_BOTTLE){
                    handleExperienceBottle(heldItem, player, hand, obelisk);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                else if(fluidHandler != null){
                    handleExperienceItem(heldItem, fluidHandler, player, hand, obelisk);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
            }

            fountain.cycleActivityState();
            MutableComponent message = getMessage(fountain);
            player.displayClientMessage(message, true);
            level.sendBlockUpdated(pos, state, state, 2);

        }

        if(!level.isClientSide){
            return ItemInteractionResult.CONSUME;
        }
        else{
            return ItemInteractionResult.sidedSuccess(true);
        }
    }

    private MutableComponent getMessage(ExperienceFountainEntity fountain) {
        MutableComponent message = Component.empty();

        switch (fountain.getActivityState()) {
            case 0 -> message = Component.translatable("message.experienceobelisk.experience_fountain.slow");
            case 1 -> message = Component.translatable("message.experienceobelisk.experience_fountain.moderate");
            case 2 -> message = Component.translatable("message.experienceobelisk.experience_fountain.fast");
            case 3 -> message = Component.translatable("message.experienceobelisk.experience_fountain.hyper");
        }
        return message;
    }

    private static final FluidStack cognitium = new FluidStack(RegisterFluids.COGNITIUM_SOURCE.get(), 1000);

    public void handleExperienceItem(ItemStack heldItem, IFluidHandlerItem fluidHandler, Player player, InteractionHand hand, ExperienceObeliskEntity obelisk){

        if(obelisk.getFluidAmount() >= 1000 && fluidHandler.fill(cognitium, IFluidHandler.FluidAction.SIMULATE) >= 1000){

            replaceFluidHandlerItem(heldItem, fluidHandler, player, hand, true);
            obelisk.drain(1000);
            player.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
        }
        else if(obelisk.getSpace() >= 1000 && fluidHandler.drain(cognitium, IFluidHandler.FluidAction.SIMULATE).getAmount() >= 1000){

            replaceFluidHandlerItem(heldItem, fluidHandler, player, hand, false);
            obelisk.fill(1000);
            player.playSound(SoundEvents.BUCKET_EMPTY, 1f, 1f);
        }
    }

    public void handleExperienceBottle(ItemStack heldItem, Player player, InteractionHand hand, ExperienceObeliskEntity obelisk){

        ItemStack experienceBottle = new ItemStack(Items.EXPERIENCE_BOTTLE, 1);
        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE, 1);

        if(heldItem.is(Items.GLASS_BOTTLE) && obelisk.getFluidAmount() >= 250){

            replaceBottle(heldItem, experienceBottle, player, hand);
            obelisk.drain(250);
            player.playSound(SoundEvents.BOTTLE_FILL, 1f, 1f);
        }
        else if(heldItem.is(Items.EXPERIENCE_BOTTLE) && obelisk.getSpace() >= 250){

            replaceBottle(heldItem, glassBottle, player, hand);
            obelisk.fill(250);
            player.playSound(SoundEvents.BOTTLE_EMPTY, 1f, 1f);
        }
    }

    public void replaceBottle(ItemStack heldItem, ItemStack bottle, Player player, InteractionHand hand){
        if(!player.isCreative()){
            heldItem.shrink(1);

            if(heldItem.isEmpty()){
                player.setItemInHand(hand, bottle);
            }
            else if(!player.addItem(bottle)){
                player.drop(bottle, false);
            }
        }
    }

    public void replaceFluidHandlerItem(ItemStack heldItem, IFluidHandlerItem fluidHandler, Player player, InteractionHand hand, boolean fill){
        if(!player.isCreative()){
            heldItem.shrink(1);

            if(fill){
                fluidHandler.fill(cognitium, IFluidHandler.FluidAction.EXECUTE);
            }
            else{
                fluidHandler.drain(cognitium, IFluidHandler.FluidAction.EXECUTE);
            }
            ItemStack fluidItem = fluidHandler.getContainer();

            if(heldItem.isEmpty()){
                player.setItemInHand(hand, fluidItem);
            }
            else if(!player.addItem(fluidItem)){
                player.drop(fluidItem, false);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape center = Shapes.create(new AABB(4.5 / 16D,0 / 16D,4.5 / 16D,11.5 / 16D,8.5 / 16D,11.5 / 16D));
        VoxelShape shape1 = Shapes.create(new AABB(2 / 16D,1.3 / 16D,4.6 / 16D,14 / 16D,2.3 / 16D,11.4 / 16D));
        VoxelShape shape2 = Shapes.create(new AABB(4.6 / 16D,1.3 / 16D,2 / 16D,11.4 / 16D,2.3 / 16D,14 / 16D));
        return Shapes.join(Shapes.join(center, shape1, BooleanOp.OR), shape2, BooleanOp.OR).optimize();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos) {

        BlockEntity entity = getter.getBlockEntity(pos);

        if(entity instanceof ExperienceFountainEntity fountain && fountain.isBound){
            Level level = fountain.getLevel();

            if(level != null && level.hasNeighborSignal(pos) || fountain.hasPlayerAbove){
                return 7;
            }
        }

        return 0;
    }

    //-----BLOCK ENTITY-----//

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == RegisterBlockEntities.EXPERIENCE_FOUNTAIN.get() ? ExperienceFountainEntity::tick : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.EXPERIENCE_FOUNTAIN.get().create(pos, state);
    }

}