package com.cyanogen.cognition.block_entities;

import com.cyanogen.cognition.registries.RegisterBlockEntities;
import com.cyanogen.cognition.utils.MiscUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

@SuppressWarnings("FieldCanBeLocal")
public class VoidAltarEntity extends BlockEntity {

    private final int base = 1;
    private final int max = 10;

    public VoidAltarEntity(BlockPos pos, BlockState blockState) {
        super(RegisterBlockEntities.VOID_ALTAR.get(), pos, blockState);
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof VoidAltarEntity altar && !level.isClientSide){
            int yLevel = pos.getY();

            if(level.getGameTime() % 20 == 0){

                System.out.println("=================");
                System.out.println("stored value: " + altar.orbValue);
                System.out.println("base rate: " + altar.getBaseRate(yLevel));
                System.out.println("boost: " + altar.getBoost(level, pos));
                System.out.println("increment: " + altar.getIncrement(yLevel, level, pos));
                System.out.println("=================");

                if(altar.orbValue >= 2048){

                    ServerLevel server = (ServerLevel) level;
                    ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2048);
                    server.addFreshEntity(orb);
                    altar.setOrbValue(Math.max(0, altar.orbValue - 2048));
                }
                else{
                    //calculate boosts here
                    altar.incrementOrbValue(altar.getIncrement(yLevel, level, pos));
                }

            }
        }

    }

    public float getIncrement(int yLevel, Level level, BlockPos pos){
        return Math.min(2048, getBaseRate(yLevel) * getBoost(level, pos));
    }

    public float getBaseRate(int yLevel){
        yLevel = Math.clamp(yLevel, -63, 0);
        return base + (max - base) * (-yLevel / 63f);
    }

    public float getBoost(Level level, BlockPos pos){
        //evaluates blocks in the lower hemisphere centered around the altar

        int radius = 4;
        int x1 = pos.getX() - radius;
        int x2 = pos.getX() + radius + 1;
        int y1 = pos.getY() - radius;
        int y2 = pos.getY() - 1;
        int z1 = pos.getZ() - radius;
        int z2 = pos.getZ() + radius + 1;

        HashMap<Block, Float> multiplierMap = getMultiplierMap();
        float boost = 1;

        for(int i = x1; i <= x2; i++){
            for(int j = y1; j <= y2; j++){
                for(int k = z1; k <= z2; k++){

                    BlockPos posToCheck = new BlockPos(i,j,k);
                    if(MiscUtils.straightLineDistance(pos, posToCheck) <= 4){
                        Block block = level.getBlockState(posToCheck).getBlock();

                        if(multiplierMap.containsKey(block)){
                            boost = boost * multiplierMap.get(block);

                            if(boost >= 2048f / base) return boost;
                        }
                    }

                }
            }
        }

        return boost;
    }

    public HashMap<Block, Float> getMultiplierMap(){
        HashMap<Block, Float> map = new HashMap<>();

        map.put(Blocks.OBSIDIAN, 1.008f);
        map.put(Blocks.CRYING_OBSIDIAN, 1.0085f);
        map.put(Blocks.BEDROCK, 1.01f);
        map.put(Blocks.REINFORCED_DEEPSLATE, 1.02f);

        return map;
    }

    // you may also sacrifice certain blocks in order to give a short production boost

    //-----------NBT-----------//

    private float orbValue = 0;

    public void incrementOrbValue(float increment){
        orbValue += increment;
        setChanged();
    }

    public void setOrbValue(float value){
        orbValue = value;
        setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);
        this.orbValue = tag.getFloat("OrbValue");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);
        tag.putFloat("OrbValue", orbValue);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);
        this.orbValue = tag.getFloat("OrbValue");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);
        tag.putFloat("OrbValue", orbValue);

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
        this.orbValue = tag.getFloat("OrbValue");
        super.onDataPacket(net, pkt, provider);
    }

}
