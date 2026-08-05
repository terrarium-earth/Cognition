package com.cyanogen.cognition.block_entities.void_garden;

import com.cyanogen.cognition.registries.RegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractClusterEntity extends BlockEntity {

    public final boolean isResonance;

    public AbstractClusterEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, boolean isResonance) {
        super(type, pos, blockState);
        this.isResonance = isResonance;
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof AbstractClusterEntity cluster && !level.isClientSide && (level.getGameTime() + 3) % 20 == 0){

            if(cluster.willDecay(cluster.isResonance)){
                cluster.decay(level, pos, cluster.isResonance);
            }
            else{
                cluster.incrementAge();
            }

        }
    }

    public boolean willDecay(boolean isResonance){
        return isResonance ? age >= VoidAltarEntity.resonanceLifespan : age >= VoidAltarEntity.interferenceLifespan;
    }


    public void decay(Level level, BlockPos pos, boolean isResonance){

        Block type = isResonance ? RegisterBlocks.RESONANCE_CLUSTER.get() : RegisterBlocks.INTERFERENCE_CLUSTER.get();

        level.playSound(null, pos, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1f,1f); //play break sound
        level.levelEvent(null, 2001, pos, Block.getId(type.defaultBlockState())); //spawn destroy particles
        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);
    }

    //-----------NBT-----------//

    private int age = 0; //the age of the cluster in seconds (not ticks!)

    public void incrementAge(){
        age++;
        setChanged();
    }

    public int getAge(){
        return age;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);
        this.age = tag.getInt("Age");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);
        tag.putInt("Age", age);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);
        this.age = tag.getInt("Age");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);
        tag.putInt("Age", age);

        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
        CompoundTag tag = pkt.getTag();
        this.age = tag.getInt("Age");
        super.onDataPacket(net, pkt, provider);
    }
}
