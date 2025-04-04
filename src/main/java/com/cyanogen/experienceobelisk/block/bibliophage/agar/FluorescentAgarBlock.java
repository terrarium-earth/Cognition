package com.cyanogen.experienceobelisk.block.bibliophage.agar;

import com.cyanogen.experienceobelisk.block_entities.bibliophage.agar.FluorescentAgarEntity;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FluorescentAgarBlock extends AbstractAgarBlock implements EntityBlock {

    public FluorescentAgarBlock() {
        super(0, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        if(player.getItemInHand(hand).is(RegisterItems.ATTUNEMENT_STAFF.get()) && level.getBlockEntity(pos) instanceof FluorescentAgarEntity agar){

            Component message = Component.translatable("message.experienceobelisk.binding_wand.query_fluorescent_agar", agar.getInfectionProgress());
            if(!level.isClientSide){
                player.displayClientMessage(message, true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(state, level, pos, player, hand, result);
    }

    //-----BLOCK ENTITY-----//

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.FLUORESCENT_AGAR_BE.get().create(pos, state);
    }
}
