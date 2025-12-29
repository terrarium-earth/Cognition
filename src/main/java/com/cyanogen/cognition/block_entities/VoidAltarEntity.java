package com.cyanogen.cognition.block_entities;

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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

@SuppressWarnings("FieldCanBeLocal")
public class VoidAltarEntity extends BlockEntity {

    private final int base = 3;
    private final int max = 10;

    public VoidAltarEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof VoidAltarEntity altar && !level.isClientSide){
            int yLevel = pos.getY();

            if(level.getGameTime() % 20 == 0){

                if(altar.orbValue >= 2048){

                    ServerLevel server = (ServerLevel) level;
                    ExperienceOrb orb = new ExperienceOrb(server, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2048);
                    server.addFreshEntity(orb);
                    altar.setOrbValue(0);
                }
                else{
                    //calculate boosts here
                    altar.incrementOrbValue(altar.getIncrement(yLevel, level, pos));
                }

            }
        }

    }

    //base rate at y = 0 or above: 3xp/s, equivalent to two and a half enchanted bookshelves
    //base rate at y = -63 or below: 8xp/s
    //orb value is incremented every second
    //orb is dispensed when value reaches 2048 or higher

    public int getIncrement(int yLevel, Level level, BlockPos pos){
        return Math.min(2048, (int) (getBaseRate(yLevel) * getBoost(level, pos)));
    }

    public float getBaseRate(int yLevel){
        yLevel = Math.clamp(yLevel, -63, 0);
        return base + (max - base) * (-yLevel / 63f);
    }

    public float getBoost(Level level, BlockPos pos){

        int radius = 4;
        int x1 = pos.getX() - radius;
        int x2 = pos.getX() + radius + 1;
        int y1 = pos.getY() - radius;
        int y2 = pos.getY() + radius + 1;
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

        //NON-CONSUMABLES
        map.put(Blocks.BEDROCK, 1.01f);
        map.put(Blocks.OBSIDIAN, 1.03f);
        map.put(Blocks.CRYING_OBSIDIAN, 1.035f);

        //CONSUMABLES
        map.put(Blocks.IRON_BLOCK, 1.12f);
        map.put(Blocks.GOLD_BLOCK, 1.175f);
        map.put(Blocks.DIAMOND_BLOCK, 1.21f);
        return map;
    }

    //-----------NBT-----------//

    private int orbValue = 0;

    public int getOrbValue(){
        return orbValue;
    }

    public void incrementOrbValue(int increment){
        orbValue += increment;
        setChanged();
    }

    public void setOrbValue(int value){
        orbValue = value;
        setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);
        this.orbValue = tag.getInt("OrbValue");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);

        tag.putInt("OrbValue", orbValue);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);

        this.orbValue = tag.getInt("OrbValue");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);

        tag.putInt("OrbValue", orbValue);

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
        this.orbValue = tag.getInt("OrbValue");
        super.onDataPacket(net, pkt, provider);
    }

}
