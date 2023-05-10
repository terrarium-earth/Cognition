package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.ModTags;
import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.fluid.ModFluidsInit;
import com.cyanogen.experienceobelisk.network.experienceobelisk.UpdateToServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
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

    //-----------PASSIVE BEHAVIOR-----------//

    protected boolean redstoneEnabled = false;
    protected double radius = 2.5;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        level.sendBlockUpdated(pos, state, state, 2);
        boolean isRedstonePowered = level.hasNeighborSignal(pos);

        XPObeliskEntity xpobelisk = (XPObeliskEntity) level.getBlockEntity(pos);

        boolean absorb = !xpobelisk.isRedstoneEnabled() || isRedstonePowered;
        double radius = xpobelisk.getRadius();

        if(level.getGameTime() % 3 == 0 && absorb){ //check every 3 ticks

            AABB area = new AABB(
                    pos.getX() - radius,
                    pos.getY() - radius,
                    pos.getZ() - radius,
                    pos.getX() + radius,
                    pos.getY() + radius,
                    pos.getZ() + radius);

            List<ExperienceOrb> list = level.getEntitiesOfClass(ExperienceOrb.class, area);

            for(ExperienceOrb orb : list){

                int value = orb.getValue() * 20;
                if(xpobelisk.getSpace() >= value && orb.isAlive()){

                    xpobelisk.fill(value);
                    orb.discard();
                }
            }
        }
    }

    public boolean isRedstoneEnabled(){
        return this.redstoneEnabled;
    }

    public void setRedstoneEnabled(boolean state){
        this.redstoneEnabled = state;
        this.setChanged();
    }

    public double getRadius(){
        return this.radius;
    }

    public void setRadius(double radius){
        this.radius = radius;
        this.setChanged();
    }


    //-----------FLUID HANDLER-----------//

    protected FluidTank tank = xpObeliskTank();

    private final LazyOptional<IFluidHandler> handler = LazyOptional.of(() -> tank);

    private static final Fluid rawExperience = ModFluidsInit.RAW_EXPERIENCE.get().getSource();
    private static final Fluid cognitium = ModFluidsInit.COGNITIUM.get().getSource();

    public static final int capacity = Config.COMMON.capacity.get(); //this is 10^8 by default

    private FluidTank xpObeliskTank() {
        return new FluidTank(capacity){

            @Override
            protected void onContentsChanged()
            {
                setChanged();
            }

            @Override
            public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
                return isFluidValid(stack);
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                String fluidName = String.valueOf(ForgeRegistries.FLUIDS.getKey(stack.getFluid()));

                if(stack.getFluid() == rawExperience || stack.getFluid() == cognitium){
                    return true;
                }
                else{
                    return stack.getFluid().is(ModTags.Fluids.EXPERIENCE) && Config.COMMON.allowedFluids.get().contains(fluidName);
                }
            }

            //Converts fluid when piped in, in case players have stored raw experience in external tanks
            @Override
            public int fill(FluidStack resource, FluidAction action) {

                if(isFluidValid(resource)){
                    setChanged();
                    if(resource.getFluid() == rawExperience){
                        return super.fill(new FluidStack(cognitium, resource.getAmount() * 20), action);
                    }
                    else{
                        return super.fill(new FluidStack(cognitium, resource.getAmount()), action);
                    }
                }
                else{
                    return 0;
                }
            }

            @NotNull
            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                setChanged();
                return super.drain(maxDrain, action);
            }

            @NotNull
            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {
                setChanged();
                return super.drain(resource, action);
            }

            @Override
            public void setFluid(FluidStack stack)
            {
                this.fluid = stack;
                setChanged();
            }

            @Override
            public int getTanks() {
                return 1;
            }
        };
    }

    public int fill(int amount){
        return tank.fill(new FluidStack(cognitium, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    public void drain(int amount)
    {
        tank.drain(new FluidStack(cognitium, amount), IFluidHandler.FluidAction.EXECUTE);
    }

    public void setFluid(int amount)
    {
        tank.setFluid(new FluidStack(cognitium, amount));
    }

    public int getFluidAmount(){
        return tank.getFluidAmount();
    }

    public int getSpace(){ return tank.getSpace(); }


    //-----------NBT-----------//

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        tank.readFromNBT(tag);

        this.radius = tag.getDouble("Radius");
        this.redstoneEnabled = tag.getBoolean("isRedstoneControllable");

        //converts legacy fluid to new fluid on loading of the block entity
        if(tank.getFluid().getFluid() == rawExperience){
            int amount = tank.getFluidAmount() * 20;
            tank.setFluid(new FluidStack(cognitium, amount));
        }
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

    //gets packet to send to client
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
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


    //-----------LOGIC-----------//


    public static int levelsToXP(int levels){
        if (levels <= 16) {
            return (int) (Math.pow(levels, 2) + 6 * levels);
        } else if (levels <= 31) {
            return (int) (2.5 * Math.pow(levels, 2) - 40.5 * levels + 360);
        } else {
            return (int) (4.5 * Math.pow(levels, 2) - 162.5 * levels + 2220);
        }
    }

    public void handleRequest(UpdateToServer.Request request, int XP, ServerPlayer sender){

        long playerXP = levelsToXP(sender.experienceLevel) + Math.round(sender.experienceProgress * sender.getXpNeededForNextLevel());
        long finalXP;

        if(request == UpdateToServer.Request.FILL && this.getSpace() != 0){

            //-----FILLING-----//

            //final amount of experience points the player will have after storing n levels
            finalXP = levelsToXP(sender.experienceLevel - XP) + Math.round(sender.experienceProgress *
                    (levelsToXP(sender.experienceLevel - XP + 1) - levelsToXP(sender.experienceLevel - XP)));

            long addAmount = (playerXP - finalXP) * 20;

            //if amount to add exceeds remaining capacity
            if(addAmount * 20 >= this.getSpace()){
                sender.giveExperiencePoints(-this.fill(this.getSpace()) / 20); //fill up however much is left and deduct that amount frm player
            }

            //normal operation
            else if(sender.experienceLevel >= XP){

                this.fill((int) (addAmount));
                sender.giveExperienceLevels(-XP);

            }
            //if player has less than the required XP
            else if (playerXP >= 1){

                this.fill((int) (playerXP * 20));
                sender.setExperiencePoints(0);
                sender.setExperienceLevels(0);


            }
        }

        //-----DRAINING-----//

        else if(request == UpdateToServer.Request.DRAIN){

            int amount = this.getFluidAmount();

            finalXP = levelsToXP(sender.experienceLevel + XP) + Math.round(sender.experienceProgress *
                    (levelsToXP(sender.experienceLevel + XP + 1) - levelsToXP(sender.experienceLevel + XP)));

            long drainAmount = (finalXP - playerXP) * 20;

            //normal operation
            if(amount >= drainAmount){

                this.drain((int) drainAmount);
                sender.giveExperienceLevels(XP);

            }
            else if(amount >= 1){

                sender.giveExperiencePoints(amount / 20);
                this.setFluid(0);
            }
        }

        //-----FILL OR DRAIN ALL-----//

        else if(request == UpdateToServer.Request.FILL_ALL){

            if(playerXP * 20 <= this.getSpace()){
                this.fill((int) (playerXP * 20));
                sender.setExperiencePoints(0);
                sender.setExperienceLevels(0);
            }
            else{
                sender.giveExperiencePoints(-this.getSpace() / 20);
                this.setFluid(capacity);
            }


        }
        else if(request == UpdateToServer.Request.DRAIN_ALL){

            sender.giveExperiencePoints(this.getFluidAmount() / 20);
            this.setFluid(0);
        }
    }

}


