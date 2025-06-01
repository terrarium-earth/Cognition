package com.cyanogen.experienceobelisk.network.molecular_metamorpher;

import com.cyanogen.experienceobelisk.block_entities.MolecularMetamorpherEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class UpdateLockedStatus {

    public static BlockPos pos;

    public UpdateLockedStatus(BlockPos pos){
        UpdateLockedStatus.pos = pos;
    }

    public UpdateLockedStatus(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {

        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            assert sender != null;
            BlockEntity serverEntity = sender.level().getBlockEntity(pos);

            if(serverEntity instanceof MolecularMetamorpherEntity metamorpher){
                metamorpher.lockInputs(!metamorpher.inputsAreLocked());
                success.set(true);

            }

        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }

}
