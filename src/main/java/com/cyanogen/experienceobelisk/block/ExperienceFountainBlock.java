package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.recipe.EmptyingRecipe;
import com.cyanogen.experienceobelisk.recipe.FillingRecipe;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExperienceFountainBlock extends ExperienceReceivingBlock implements EntityBlock {

    public ExperienceFountainBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(9f)
                .destroyTime(1.2f)
                .explosionResistance(9f)
                .noOcclusion()
                .sound(SoundType.METAL)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if(super.use(state, level, pos, player, hand, hit) != InteractionResult.PASS){
            return InteractionResult.CONSUME;
        }

        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);
        @Nullable IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldItem, 1)).orElse(null);

        if(entity instanceof ExperienceFountainEntity fountain){

            if(fountain.isBound && level.getBlockEntity(fountain.getBoundPos()) instanceof ExperienceObeliskEntity obelisk){

                if(fluidHandler != null){
                    handleExperienceContainer(heldItem, fluidHandler, player, hand, obelisk);
                    return InteractionResult.sidedSuccess(true);
                }

                FillingRecipe fillingRecipe = FillingRecipe.getRecipe(level, heldItem);
                if(fillingRecipe != null){
                    handleFillingRecipe(heldItem, fillingRecipe, player, hand, obelisk, false);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }

                EmptyingRecipe emptyingRecipe = EmptyingRecipe.getRecipe(level, heldItem);
                if(emptyingRecipe != null){
                    handleEmptyingRecipe(heldItem, emptyingRecipe, player, hand, obelisk, false);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }

            fountain.cycleActivityState();
            MutableComponent message = getMutableComponent(fountain);
            player.displayClientMessage(message, true);
            level.sendBlockUpdated(pos, state, state, 2);

        }

        if(!level.isClientSide){
            return InteractionResult.CONSUME;
        }
        else{
            return InteractionResult.SUCCESS;
        }

    }

    private static @NotNull MutableComponent getMutableComponent(ExperienceFountainEntity fountain) {
        MutableComponent message = Component.empty();

        switch (fountain.getActivityState()) {
            case 0 -> message = Component.translatable("message.experienceobelisk.experience_fountain.slow");
            case 1 -> message = Component.translatable("message.experienceobelisk.experience_fountain.moderate");
            case 2 -> message = Component.translatable("message.experienceobelisk.experience_fountain.fast");
            case 3 -> message = Component.translatable("message.experienceobelisk.experience_fountain.hyper");
        }
        return message;
    }

    public void handleExperienceContainer(ItemStack heldItem, IFluidHandlerItem fluidHandler, Player player, InteractionHand hand, ExperienceObeliskEntity obelisk){

        FluidStack cognitium = new FluidStack(RegisterFluids.COGNITIUM.get(), 1000);

        if(obelisk.getFluidAmount() >= 1000 && fluidHandler.fill(cognitium, IFluidHandler.FluidAction.SIMULATE) >= 1000){

            if(!player.isCreative()){
                heldItem.shrink(1);
                fluidHandler.fill(cognitium, IFluidHandler.FluidAction.EXECUTE);

                ItemStack fluidItem = fluidHandler.getContainer();

                if(heldItem.isEmpty()){
                    player.setItemInHand(hand, fluidItem);
                }
                else if(!player.addItem(fluidItem)){
                    player.drop(fluidItem, false); //in case player inventory is full
                }

            }

            obelisk.drain(1000);
            player.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
        }
        else if(obelisk.getSpace() >= 1000 && fluidHandler.drain(cognitium, IFluidHandler.FluidAction.SIMULATE).getAmount() >= 1000){

            if(!player.isCreative()){
                heldItem.shrink(1);
                fluidHandler.drain(cognitium, IFluidHandler.FluidAction.EXECUTE);

                ItemStack fluidItem = fluidHandler.getContainer();

                if(heldItem.isEmpty()){
                    player.setItemInHand(hand, fluidItem);
                }
                else if(!player.addItem(fluidItem)){
                    player.drop(fluidItem, false);
                }
            }

            obelisk.fill(1000);
            player.playSound(SoundEvents.BUCKET_EMPTY, 1f, 1f);
        }
    }

    public void handleFillingRecipe(ItemStack heldItem, FillingRecipe recipe, Player player, InteractionHand hand,
                                    ExperienceObeliskEntity obelisk, boolean shiftKeyDown){

        int drainAmount = recipe.getCognitiumCost();
        ItemStack result = recipe.getResultItem(null);
        int resultCount = result.getCount();

        if(!shiftKeyDown){
            if(obelisk.getFluidAmount() >= drainAmount){
                obelisk.drain(drainAmount);
                heldItem.shrink(1);
            }
            else{
                return;
            }
        }
        else{
            int maxDrainCount = obelisk.getFluidAmount() / drainAmount;

            if(obelisk.getFluidAmount() < drainAmount){
                return;
            }
            else if(maxDrainCount >= heldItem.getCount()){
                obelisk.drain(drainAmount * heldItem.getCount());
                resultCount = result.getCount() * heldItem.getCount();
                heldItem.setCount(0);
            }
            else{
                obelisk.drain(drainAmount * maxDrainCount);
                resultCount = result.getCount() * maxDrainCount;
                heldItem.shrink(maxDrainCount);
            }
        }

        result.setCount(resultCount);
        if(heldItem.isEmpty() && result.getCount() <= result.getMaxStackSize()){
            player.setItemInHand(hand, result);
        }
        else if(!player.addItem(result)){
            player.drop(result, false);
        }

        player.playSound(SoundEvents.BOTTLE_FILL, 1f, 1f);
    }

    public void handleEmptyingRecipe(ItemStack heldItem, EmptyingRecipe recipe, Player player, InteractionHand hand,
                                     ExperienceObeliskEntity obelisk, boolean shiftKeyDown){

        int fillAmount = recipe.getCognitiumGain();
        ItemStack result = recipe.hasResultStack() ? recipe.getResultItem(null) : ItemStack.EMPTY;
        int resultCount = result.getCount();

        if(!shiftKeyDown){
            if(obelisk.getSpace() >= fillAmount){
                obelisk.fill(fillAmount);
                heldItem.shrink(1);
            }
            else{
                return;
            }
        }
        else{
            int maxFillCount = obelisk.getSpace() / fillAmount;

            if(obelisk.getSpace() < fillAmount){
                return;
            }
            if(maxFillCount >= heldItem.getCount()){
                obelisk.fill(fillAmount * heldItem.getCount());
                resultCount = result.getCount() * heldItem.getCount();
                heldItem.setCount(0);
            }
            else{
                obelisk.fill(fillAmount * maxFillCount);
                resultCount = result.getCount() * maxFillCount;
                heldItem.shrink(maxFillCount);
            }
        }

        if(recipe.hasResultStack()){
            result.setCount(resultCount);
            if(heldItem.isEmpty() && result.getCount() <= result.getMaxStackSize()){
                player.setItemInHand(hand, result);
            }
            else if(!player.addItem(result)){
                player.drop(result, false);
            }
        }

        player.playSound(SoundEvents.BOTTLE_EMPTY, 1f, 1f);
    }

    public static void handleExperienceItemStack(PlayerInteractEvent.RightClickBlock event){

        //Shift right click functionality
        //This is to step around useItemOn() not being called when the shift key is held down

        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack heldItem = player.getItemInHand(hand);
        BlockPos pos = event.getPos();
        Level level = event.getLevel();

        if(level.getBlockEntity(pos) instanceof ExperienceFountainEntity fountain
                && fountain.isBound && fountain.getBoundObelisk() != null && !heldItem.isEmpty()
                && event.getEntity().isShiftKeyDown()){

            ExperienceFountainBlock block = (ExperienceFountainBlock) level.getBlockState(pos).getBlock();

            FillingRecipe fillingRecipe = FillingRecipe.getRecipe(level, heldItem);
            if(fillingRecipe != null){
                block.handleFillingRecipe(heldItem, fillingRecipe, player, hand, fountain.getBoundObelisk(), true);
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
                event.setCanceled(true);
            }
            else{
                EmptyingRecipe emptyingRecipe = EmptyingRecipe.getRecipe(level, heldItem);
                if(emptyingRecipe != null){
                    block.handleEmptyingRecipe(heldItem, emptyingRecipe, player, hand, fountain.getBoundObelisk(), true);
                    event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
                    event.setCanceled(true);
                }
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
        return blockEntityType == RegisterBlockEntities.EXPERIENCE_FOUNTAIN_BE.get() ? ExperienceFountainEntity::tick : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.EXPERIENCE_FOUNTAIN_BE.get().create(pos, state);
    }

}