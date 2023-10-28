package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import com.cyanogen.experienceobelisk.gui.ExperienceObeliskMenu;
import com.cyanogen.experienceobelisk.item.ModItemsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
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
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExperienceObeliskBlock extends Block implements EntityBlock {

    public ExperienceObeliskBlock() {
        super(Properties.of(Material.METAL)
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(1200f)
                .lightLevel(value -> 10)
                .noOcclusion()
                .emissiveRendering((state, getter, pos) -> true)
        );
    }

    //crystal
    VoxelShape shape1 = Shapes.create(new AABB(6 / 16D,0 / 16D,6 / 16D,10 / 16D,16 / 16D,10 / 16D));
    //base
    VoxelShape shape2 = Shapes.create(new AABB(0 / 16D,0 / 16D,0 / 16D,16 / 16D,2 / 16D,16 / 16D));
    VoxelShape shape = Shapes.join(shape1, shape2, BooleanOp.OR);

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        shape = shape.optimize();
        return shape;
    }

    public ItemStack stack;
    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof ExperienceObeliskEntity entity && pPlayer.hasCorrectToolForDrops(pState)) {

                stack = new ItemStack(ModItemsInit.EXPERIENCE_OBELISK_ITEM.get(), 1);
                entity.saveToItem(stack);
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
        List<ItemStack> drops = new ArrayList<>();
        if(stack != null){
            drops.add(stack);
        }
        return drops;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        ItemStack heldItem = pPlayer.getItemInHand(pHand);
        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldItem, 1)).orElse(null);
        FluidStack cognitium = new FluidStack(ModFluidsInit.COGNITIUM.get(), 1000);

        if(pLevel.getBlockEntity(pPos) instanceof ExperienceObeliskEntity obelisk && fluidHandler != null){

            if(obelisk.getFluidAmount() >= 1000 && fluidHandler.fill(cognitium, IFluidHandler.FluidAction.SIMULATE) >= 1000){

                if(!pPlayer.isCreative()){
                    heldItem.shrink(1);
                    fluidHandler.fill(cognitium, IFluidHandler.FluidAction.EXECUTE);
                    if(heldItem.isEmpty()){
                        pPlayer.setItemInHand(pHand, fluidHandler.getContainer());
                    }
                    else if(!pPlayer.addItem(fluidHandler.getContainer())){     //if player inventory is full
                        pPlayer.drop(fluidHandler.getContainer(), false);
                    }

                }

                obelisk.drain(1000);
                pPlayer.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
                return InteractionResult.sidedSuccess(true);
            }
            else if(obelisk.getSpace() >= 1000 && fluidHandler.drain(cognitium, IFluidHandler.FluidAction.SIMULATE).getAmount() >= 1000){

                if(!pPlayer.isCreative()){
                    heldItem.shrink(1);
                    fluidHandler.drain(cognitium, IFluidHandler.FluidAction.EXECUTE);
                    pPlayer.setItemInHand(pHand, fluidHandler.getContainer());
                }

                obelisk.fill(1000);
                pPlayer.playSound(SoundEvents.BUCKET_EMPTY, 1f, 1f);
                return InteractionResult.sidedSuccess(true);
            }
        }

        if(!pLevel.isClientSide()){
            NetworkHooks.openGui((ServerPlayer) pPlayer, pState.getMenuProvider(pLevel,pPos), pPos);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level level, BlockPos pos) {

        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TextComponent("Experience Obelisk");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                return new ExperienceObeliskMenu(id, pos);
            }
        };
    }



    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModTileEntitiesInit.XPOBELISK_BE.get() ? ExperienceObeliskEntity::tick : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    //block entity
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTileEntitiesInit.XPOBELISK_BE.get().create(pPos, pState);
    }

}
