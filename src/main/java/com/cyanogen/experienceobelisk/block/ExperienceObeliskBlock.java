package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import com.cyanogen.experienceobelisk.gui.ExperienceObeliskMenu;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
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
                .explosionResistance(9f)
                .noOcclusion()
                .lightLevel(pLightEmission -> 5)
        );
    }

    //rotating cube
    VoxelShape shape1 = Shapes.create(new AABB(6.7 / 16D,14 / 16D,6.7 / 16D,9.3 / 16D,15.5 / 16D,9.3 / 16D));
    //base glowy bit
    VoxelShape shape2 = Shapes.create(new AABB(1 / 16D,0 / 16D,1 / 16D,15 / 16D,4.3 / 16D,15 / 16D));
    //base
    VoxelShape shape3 = Shapes.create(new AABB(5 / 16D,4 / 16D,5 / 16D,11 / 16D,5 / 16D,11 / 16D));

    VoxelShape shape = Shapes.join(Shapes.join(shape1, shape2, BooleanOp.OR), shape3, BooleanOp.OR).optimize();
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return shape;
    }

    public ItemStack stack;
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof ExperienceObeliskEntity entity && player.hasCorrectToolForDrops(state)) {

                stack = new ItemStack(RegisterItems.EXPERIENCE_OBELISK_ITEM.get(), 1);
                entity.saveToItem(stack);
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof ExperienceObeliskEntity entity) {

                stack = new ItemStack(RegisterItems.EXPERIENCE_OBELISK_ITEM.get(), 1);
                entity.saveToItem(stack);
            }
        }


        super.onBlockExploded(state, level, pos, explosion);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            NetworkHooks.openScreen((ServerPlayer) player, state.getMenuProvider(level,pos), pos);
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {

        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Experience Obelisk");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                return new ExperienceObeliskMenu(id, pos);
            }
        };
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == RegisterBlockEntities.EXPERIENCEOBELISK_BE.get() ? ExperienceObeliskEntity::tick : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.EXPERIENCEOBELISK_BE.get().create(pos, state);
    }

}
