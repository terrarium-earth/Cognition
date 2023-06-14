package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.block_entities.ExperienceFountainEntity;
import com.cyanogen.experienceobelisk.block_entities.ModTileEntitiesInit;
import com.cyanogen.experienceobelisk.item.ModItemsInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ExperienceFountainBlock extends Block implements EntityBlock {

    public ExperienceFountainBlock() {
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

        if(entity instanceof ExperienceFountainEntity fountain){

            if(heldItem.is(ModItemsInit.BINDING_WAND.get())){
                player.displayClientMessage(new TranslatableComponent("message.experienceobelisk.binding_wand.reveal_bound_pos",
                        new TextComponent(fountain.getBoundPos().toShortString()).withStyle(ChatFormatting.GREEN)), true);
            }
            else{
                fountain.cycleActivityState();
                TextComponent message = new TextComponent("Experience Fountain set to: ");

                switch (fountain.getActivityState()) {
                    case 0 -> message.append(new TextComponent("Off").withStyle(ChatFormatting.RED));
                    case 1 -> message.append(new TextComponent("Slow").withStyle(ChatFormatting.YELLOW));
                    case 2 -> message.append(new TextComponent("Fast").withStyle(ChatFormatting.GREEN));
                }
                player.displayClientMessage(message, true);
                level.sendBlockUpdated(pos, state, state, 2);
            }

        }
        return InteractionResult.CONSUME;
    }


    VoxelShape shape = Shapes.create(new AABB(0 / 16D,0 / 16D,0 / 16D,16 / 16D,9 / 16D,16 / 16D));
    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shape;
    }


    //-----BLOCK ENTITY-----//

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == ModTileEntitiesInit.EXPERIENCEFOUNTAIN_BE.get() ? ExperienceFountainEntity::tick : null;
    }


    //block entity
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTileEntitiesInit.EXPERIENCEFOUNTAIN_BE.get().create(pPos, pState);
    }

}