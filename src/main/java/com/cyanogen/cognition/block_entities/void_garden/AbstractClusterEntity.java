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

import java.util.Optional;

import static com.cyanogen.cognition.block.void_garden.AbstractClusterBlock.STAGE;

public abstract class AbstractClusterEntity extends BlockEntity {

    public final boolean isResonance;
    public int clusterStep = 20;

    public AbstractClusterEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, boolean isResonance) {
        super(type, pos, blockState);
        this.isResonance = isResonance;
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof AbstractClusterEntity cluster && !level.isClientSide && (level.getGameTime() + 3) % cluster.clusterStep == 0){

            if(cluster.willDecay()){
                cluster.decay(level, pos);
            }
            else{
                cluster.incrementAge();

                if(cluster.getBlockstateStage(level) != cluster.getStageFromAge()){
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, cluster.getStageFromAge()));

                }
            }

        }
    }

    public int getStageFromAge(){
        int lifespan = getLifespan();
        int age = getAge();

        int stage = 10 * age / lifespan;
        if(stage == 0) return 1;
        if (stage == 1) return 2;
        if (stage == 2) return 3;
        if (stage == 9) return 5;
        return 4;
    }

    public int getBlockstateStage(Level level){
        Optional<Integer> stage = level.getBlockState(getBlockPos()).getOptionalValue(STAGE);
        return stage.orElse(4);
    }

    public boolean willDecay(){
        return age >= getLifespan();
    }


    public void decay(Level level, BlockPos pos){

        Block type = this.isResonance ? RegisterBlocks.RESONANCE_CLUSTER.get() : RegisterBlocks.INTERFERENCE_CLUSTER.get();

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

    public int getLifespan(){
        return isResonance ? VoidAltarEntity.resonanceLifespan : VoidAltarEntity.interferenceLifespan;
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
