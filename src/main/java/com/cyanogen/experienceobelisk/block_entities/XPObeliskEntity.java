package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class XPObeliskEntity extends BlockEntity implements IAnimatable{

    //-----------ANIMATIONS-----------//

    //events that control what animation is being played
    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        AnimationController controller = event.getController();
        controller.transitionLengthTicks = 0;
        controller.setAnimation(new AnimationBuilder().addAnimation("xpobelisk.idle", true));

        return PlayState.CONTINUE;
    }

    public XPObeliskEntity(BlockPos pPos, BlockState pState) {
        super(ModTileEntitiesInit.XPOBELISK_BE.get(), pPos, pState);
        this.pos = pPos;
        this.state = pState;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private final AnimationFactory manager = new AnimationFactory(this);
    @Override
    public AnimationFactory getFactory() {
        return manager;
    }

    //-----------PASSIVE XP-----------//

    protected boolean redstoneEnabled = false;
    protected double radius = 2.5;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        level.sendBlockUpdated(pos, state, state, 2);

        boolean isRedstonePowered = level.hasNeighborSignal(pos);

        BlockEntity entity = level.getBlockEntity(pos);

        if(entity instanceof XPObeliskEntity xpobelisk){

            boolean absorb = !xpobelisk.isRedstoneEnabled() || isRedstonePowered;
            double radius = xpobelisk.getRadius();

            AABB area = new AABB(
                    pos.getX() - radius,
                    pos.getY() - radius,
                    pos.getZ() - radius,
                    pos.getX() + radius,
                    pos.getY() + radius,
                    pos.getZ() + radius);

            List<Entity> list = level.getEntities(null, area);

            for(Entity e : list){
                if(e instanceof ExperienceOrb orb && xpobelisk.getSpace() > 0 && absorb && level.getGameTime() % 3 == 0){ //check every 3 ticks instead of every tick

                    int value = orb.getValue();
                    xpobelisk.fill(value * 20); //parity w mob grinding utils
                    e.discard();

                }
            }
        }
    }

    public boolean isRedstoneEnabled(){
        return this.redstoneEnabled;
    }

    public void setRedstoneEnabled(boolean state){
        this.redstoneEnabled = state;
    }

    public double getRadius(){
        return this.radius;
    }

    public void setRadius(double radius){
        this.radius = radius;
    }


    //-----------FLUID HANDLER-----------//

    protected FluidTank tank = xpObeliskTank();
    private final LazyOptional<IFluidHandler> handler = LazyOptional.of(() -> tank);
    private static final Fluid rawExperience = ModFluidsInit.RAW_EXPERIENCE.get().getSource();
    public static BlockPos pos;
    public static BlockState state;

    private FluidTank xpObeliskTank() {
        return new FluidTank(16000000){ //1903 levels
            @Override
            protected void onContentsChanged()
            {
                super.onContentsChanged();
                setChanged();
            }

            @Override
            public void setFluid(FluidStack stack)
            {
                this.fluid = stack;
                setChanged();
            }

            @Override
            public boolean isFluidValid(FluidStack stack)
            {
                return stack.getFluid() == rawExperience;
            }

            @Override
            public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
                return stack.getFluid() == rawExperience;
            }
        };
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        tank.readFromNBT(tag);

        this.radius = tag.getDouble("Radius");
        this.redstoneEnabled = tag.getBoolean("isRedstoneControllable");
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        tank.writeToNBT(tag);

        tag.putDouble("Radius", radius);
        tag.putBoolean("isRedstoneControllable", redstoneEnabled);
    }

    //sends CompoundTag out with nbt data
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();
        tank.writeToNBT(tag);

        tag.putDouble("Radius", radius);
        tag.putBoolean("isRedstoneControllable", redstoneEnabled);

        return tag;
    }

    //receives and loads nbt data whenever chunk is loaded
    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        load(tag);
    }

    //gets packet to send to client
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //updates client whenever level.sendBlockUpdated is called
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        CompoundTag compoundtag = pkt.getTag();
        if (compoundtag != null) {
            load(compoundtag);
        }
    }


    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return handler.cast();
        return super.getCapability(capability, facing);
        //controls which sides can give or receive fluids
    }

    public int fill(int amount)
    {
        level.sendBlockUpdated(pos, state, state, 2);
        return tank.fill(new FluidStack(rawExperience, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    public void drain(int amount)
    {
        tank.drain(new FluidStack(rawExperience, amount), IFluidHandler.FluidAction.EXECUTE);
        level.sendBlockUpdated(pos, state, state, 2);
    }

    public void setFluid(int amount)
    {
        tank.setFluid(new FluidStack(rawExperience, amount));
        level.sendBlockUpdated(pos, state, state, 2);
    }

    public int getFluidAmount(){
        return tank.getFluidAmount();
    }

    public int getSpace(){ return tank.getSpace(); }


}

