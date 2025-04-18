package com.cyanogen.experienceobelisk.network.experience_obelisk;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.block_entities.ExperienceObeliskEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;


public class UpdateRadius implements CustomPacketPayload {

    /**
     * This is sent from the client to the server whenever a request to change the Experience Obelisk radius is made in the Experience Obelisk GUI.
     */

    public final int posX;
    public final int posY;
    public final int posZ;
    public final double changeInRadius;

    public static final StreamCodec<ByteBuf, UpdateRadius> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            UpdateRadius::getPosX,
            ByteBufCodecs.VAR_INT,
            UpdateRadius::getPosY,
            ByteBufCodecs.VAR_INT,
            UpdateRadius::getPosZ,
            ByteBufCodecs.DOUBLE,
            UpdateRadius::getChangeInRadius,
            UpdateRadius::new
    );

    public static final Type<UpdateRadius> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ExperienceObelisk.MOD_ID,
            "experience_obelisk_update_radius"));

    public UpdateRadius(@Nullable BlockPos pos, double changeInRadius) {
        if(pos == null){
            this.posX = 0; this.posY = 0; this.posZ = 0;
        }
        else{
            this.posX = pos.getX();
            this.posY = pos.getY();
            this.posZ = pos.getZ();
        }
        this.changeInRadius = changeInRadius;
    }

    public UpdateRadius(int posX, int posY, int posZ, double changeInRadius) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.changeInRadius = changeInRadius;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public double getChangeInRadius() {
        return changeInRadius;
    }

    public static void handleServer(UpdateRadius packet, IPayloadContext context) {

        context.enqueueWork(() -> {

            if(!context.player().level().isClientSide){
                ServerPlayer player = (ServerPlayer) context.player();
                BlockEntity entity = player.level().getBlockEntity(new BlockPos(packet.posX, packet.posY, packet.posZ));

                if(entity instanceof ExperienceObeliskEntity obelisk){

                    double finalRadius = obelisk.getRadius() + packet.changeInRadius;

                    if(packet.changeInRadius == 0){
                        obelisk.setRadius(2.5); //set to default
                    }
                    else if(finalRadius >= 1 && finalRadius <= 5){
                        obelisk.setRadius(finalRadius);
                    }

                }
            }

        });
    }

}
