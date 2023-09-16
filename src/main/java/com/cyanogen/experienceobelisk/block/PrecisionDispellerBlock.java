package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.gui.PrecisionDispellerMenu;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PrecisionDispellerBlock extends Block implements EntityBlock {

    public PrecisionDispellerBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(8f)
                .noOcclusion()
                .emissiveRendering((state, getter, pos) -> true)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {

        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TextComponent("Precision Dispeller");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                return new PrecisionDispellerMenu(pContainerId, pPlayerInventory, pPlayer, pos);
            }
        };

    }

    //spinny bit
    VoxelShape shape1 = Shapes.create(new AABB(2 / 16D,4 / 16D,4 / 16D,14 / 16D,16 / 16D,12 / 16D));
    //leg 1
    VoxelShape shape2a = Shapes.create(new AABB(5 / 16D,0 / 16D,11.5 / 16D,11 / 16D,7 / 16D,14.5 / 16D));
    VoxelShape shape2b = Shapes.create(new AABB(6 / 16D,7 / 16D,11.5 / 16D,6 / 16D,13 / 16D,14 / 16D));
    VoxelShape shape2 = Shapes.join(shape2a, shape2b, BooleanOp.OR);
    //leg 2
    VoxelShape shape3a = Shapes.create(new AABB(5 / 16D,0 / 16D,1.5 / 16D,11 / 16D,7 / 16D,4.5 / 16D));
    VoxelShape shape3b = Shapes.create(new AABB(6 / 16D,7 / 16D,2 / 16D,6 / 16D,13 / 16D,4.5 / 16D));
    VoxelShape shape3 = Shapes.join(shape3a, shape3b, BooleanOp.OR);

    VoxelShape shape = Shapes.join(Shapes.join(shape1, shape2, BooleanOp.OR), shape3, BooleanOp.OR).optimize();
    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegisterBlockEntities.PRECISIONDISPELLER_BE.get().create(pPos, pState);
    }
}
