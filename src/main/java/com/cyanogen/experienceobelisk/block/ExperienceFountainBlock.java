package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class ExperienceFountainBlock extends ExperienceReceivingBlock implements EntityBlock {

    public ExperienceFountainBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(9f)
                .noOcclusion()
                .lightLevel(pLightEmission -> 7)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if(super.use(state, level, pos, player, hand, hit) != InteractionResult.PASS){
            return InteractionResult.CONSUME;
        }

        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);
        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldItem, 1)).orElse(null);

        if(entity instanceof ExperienceFountainEntity fountain){

            if(fountain.isBound && level.getBlockEntity(fountain.getBoundPos()) instanceof ExperienceObeliskEntity obelisk){

                if(heldItem.getItem() == Items.EXPERIENCE_BOTTLE || heldItem.getItem() == Items.GLASS_BOTTLE){
                    handleExperienceBottle(heldItem, player, hand, obelisk);
                    return InteractionResult.sidedSuccess(true);
                }
                else if(fluidHandler != null){
                    handleExperienceItem(heldItem, fluidHandler, player, hand, obelisk);
                    return InteractionResult.sidedSuccess(true);
                }
            }

            fountain.cycleActivityState();
            TranslatableComponent message = new TranslatableComponent("");

            switch (fountain.getActivityState()) {
                case 0 -> message = new TranslatableComponent("message.experienceobelisk.experience_fountain.slow");
                case 1 -> message = new TranslatableComponent("message.experienceobelisk.experience_fountain.moderate");
                case 2 -> message = new TranslatableComponent("message.experienceobelisk.experience_fountain.fast");
                case 3 -> message = new TranslatableComponent("message.experienceobelisk.experience_fountain.hyper");
            }
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

    public void handleExperienceItem(ItemStack heldItem, IFluidHandlerItem fluidHandler, Player player, InteractionHand hand, ExperienceObeliskEntity obelisk){

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

    public void handleExperienceBottle(ItemStack heldItem, Player player, InteractionHand hand, ExperienceObeliskEntity obelisk){

        ItemStack experienceBottle = new ItemStack(Items.EXPERIENCE_BOTTLE, 1);
        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE, 1);

        if(heldItem.is(Items.GLASS_BOTTLE) && obelisk.getFluidAmount() >= 140){

            if(!player.isCreative()){
                heldItem.shrink(1);

                if(heldItem.isEmpty()){
                    player.setItemInHand(hand, experienceBottle);
                }
                else if(!player.addItem(experienceBottle)){
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
                else if(!player.addItem(glassBottle)){
                    player.drop(glassBottle, false);
                }
            }

            obelisk.fill(140);
            player.playSound(SoundEvents.BOTTLE_EMPTY, 1f, 1f);
        }
    }


    VoxelShape center = Shapes.create(new AABB(4.5 / 16D,0 / 16D,4.5 / 16D,11.5 / 16D,8.5 / 16D,11.5 / 16D));
    VoxelShape shape1 = Shapes.create(new AABB(2 / 16D,1.3 / 16D,4.6 / 16D,14 / 16D,2.3 / 16D,11.4 / 16D));
    VoxelShape shape2 = Shapes.create(new AABB(4.6 / 16D,1.3 / 16D,2 / 16D,11.4 / 16D,2.3 / 16D,14 / 16D));
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.join(Shapes.join(center, shape1, BooleanOp.OR), shape2, BooleanOp.OR).optimize();
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