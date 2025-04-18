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

public class UpdateContents implements CustomPacketPayload {

    /**
    * This is sent from the client to the server whenever a request to fill or drain XP is made in the Experience Obelisk GUI.
     * Upon receiving the packet serverside, logic is handled in ExperienceObeliskEntity
     */

    public final int posX;
    public final int posY;
    public final int posZ;
    public final int levels;
    public final String request;
    public static final String FILL = "FILL";
    public static final String FILL_ALL = "FILL_ALL";
    public static final String DRAIN = "DRAIN";
    public static final String DRAIN_ALL = "DRAIN_ALL";

    public static final StreamCodec<ByteBuf, UpdateContents> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            UpdateContents::getPosX,
            ByteBufCodecs.VAR_INT,
            UpdateContents::getPosY,
            ByteBufCodecs.VAR_INT,
            UpdateContents::getPosZ,
            ByteBufCodecs.VAR_INT,
            UpdateContents::getLevels,
            ByteBufCodecs.STRING_UTF8,
            UpdateContents::getRequest,
            UpdateContents::new
    );

    public static final Type<UpdateContents> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ExperienceObelisk.MOD_ID,
            "experience_obelisk_update_contents"));

    public UpdateContents(BlockPos pos, int levels, String request) {
        this.posX = pos.getX();
        this.posY = pos.getY();
        this.posZ = pos.getZ();
        this.levels = levels;
        this.request = request;
    }

    public UpdateContents(int posX, int posY, int posZ, int levels, String request) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.levels = levels;
        this.request = request;
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

    public int getLevels() {
        return levels;
    }

    public String getRequest() {
        return request;
    }

    public static void handle(final UpdateContents packet, final IPayloadContext context) {

        context.enqueueWork(() -> {

            ServerPlayer player = (ServerPlayer) context.player();
            BlockEntity entity = player.level().getBlockEntity(new BlockPos(packet.posX, packet.posY, packet.posZ));

            if(entity instanceof ExperienceObeliskEntity obelisk){
                obelisk.handleRequest(packet.request, packet.levels, player);

            }
        });
    }

}
