package com.cyanogen.cognition.network.molecular_metamorpher;

import com.cyanogen.cognition.Cognition;
import com.cyanogen.cognition.block_entities.MolecularMetamorpherEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateLockedStatus(int posX, int posY, int posZ) implements CustomPacketPayload {

    public static final StreamCodec<ByteBuf, UpdateLockedStatus> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            UpdateLockedStatus::posX,
            ByteBufCodecs.VAR_INT,
            UpdateLockedStatus::posY,
            ByteBufCodecs.VAR_INT,
            UpdateLockedStatus::posZ,
            UpdateLockedStatus::new
    );

    public static final Type<UpdateLockedStatus> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Cognition.MOD_ID,
            "molecular_metamorpher_update_locked_status"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(UpdateLockedStatus packet, IPayloadContext context) {

        context.enqueueWork(() -> {

            if (!context.player().level().isClientSide) {
                ServerLevel server = (ServerLevel) context.player().level();
                BlockPos pos = new BlockPos(packet.posX, packet.posY, packet.posZ);

                if(server.getBlockEntity(pos) instanceof MolecularMetamorpherEntity metamorpher){
                    metamorpher.lockInputs(!metamorpher.inputsAreLocked());
                }
            }

        });
    }

}
