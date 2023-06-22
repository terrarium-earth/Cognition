package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.AuralProjectorEntity;
import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.item.ModItemsInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AuralProjectorBlock extends Block implements EntityBlock {

    public AuralProjectorBlock() {
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);

        if(entity instanceof AuralProjectorEntity projector && heldItem.is(ModItemsInit.BINDING_WAND.get())){

            player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.reveal_bound_pos",
                    new TextComponent(projector.getBoundPos().toShortString()).withStyle(ChatFormatting.GREEN)), true);
        }
        return InteractionResult.CONSUME;
    }

    public ItemStack stack;
    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof AuralProjectorEntity entity && pPlayer.hasCorrectToolForDrops(pState)) {

                stack = new ItemStack(ModItemsInit.AURAL_PROJECTOR_ITEM.get(), 1);
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

    //-----BLOCK ENTITY-----//

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTileEntitiesInit.AURALPROJECTOR_BE.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModTileEntitiesInit.AURALPROJECTOR_BE.get() ? AuralProjectorEntity::tick : null;
    }
}
