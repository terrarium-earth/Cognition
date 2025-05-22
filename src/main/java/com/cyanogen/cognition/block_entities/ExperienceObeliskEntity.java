package com.cyanogen.cognition.block_entities;

import com.cyanogen.cognition.config.Config;
import com.cyanogen.cognition.network.experience_obelisk.UpdateContents;
import com.cyanogen.cognition.registries.RegisterBlockEntities;
import com.cyanogen.cognition.registries.RegisterFluids;
import com.cyanogen.cognition.saved_data.MemoryTabletData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

import static com.cyanogen.cognition.utils.ExperienceUtils.*;

public class ExperienceObeliskEntity extends BlockEntity implements GeoBlockEntity {

    public ExperienceObeliskEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.EXPERIENCE_OBELISK.get(), pos, state);
    }

    //-----------ANIMATIONS-----------//

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    protected static final RawAnimation IDLE_INACTIVE = RawAnimation.begin().thenPlay("idle.inactive");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::controller));
    }

    protected <E extends ExperienceObeliskEntity> PlayState controller(final AnimationState<E> state){

        ExperienceObeliskEntity obelisk = state.getAnimatable();
        AnimationController<E> controller = state.getController();
        RawAnimation animation = controller.getCurrentRawAnimation();

        if(animation == null || level == null){
            controller.setAnimation(IDLE);
        }
        else if(controller.getAnimationState() == AnimationController.State.STOPPED
                || controller.getAnimationState() == AnimationController.State.PAUSED){

            controller.forceAnimationReset();
            controller.setAnimation(IDLE);
        }
        else{
            boolean isInactive = obelisk.redstoneEnabled && !level.hasNeighborSignal(obelisk.getBlockPos());

            if(isInactive && animation.equals(IDLE)){
                controller.setAnimation(IDLE_INACTIVE);
            }
            else if(!isInactive && animation.equals(IDLE_INACTIVE)){
                controller.setAnimation(IDLE);
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //-----------PASSIVE BEHAVIOR-----------//

    protected boolean redstoneEnabled = false;
    protected double radius = 2.5;

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        boolean isRedstonePowered = level.hasNeighborSignal(pos);

        if(blockEntity instanceof ExperienceObeliskEntity obelisk){

            boolean absorb = !obelisk.isRedstoneEnabled() || isRedstonePowered;
            double radius = obelisk.getRadius();
            int space = obelisk.getSpace();

            if(absorb && level.getGameTime() % 10 == 0){
                List<ExperienceOrb> list = level.getEntitiesOfClass(ExperienceOrb.class, getAreaOfEffect(pos, radius));

                if(!list.isEmpty()) for(int i = 0; i < Math.min(30,list.size()); i++){

                    ExperienceOrb orb = list.get(i);
                    CompoundTag tag = new CompoundTag();
                    orb.addAdditionalSaveData(tag);

                    int value = orb.value;
                    int count = tag.getInt("Count");

                    int amount = value * 20 * count;
                    if(space >= amount){
                        obelisk.fill(amount);
                        space = space - amount;
                        orb.discard();
                    }
                }
            }

            if(!level.isClientSide){
                obelisk.checkAroundForMemorized();
            }
        }
    }

    public static AABB getAreaOfEffect(BlockPos pos, double radius){
        return new AABB(
                pos.getX() - radius,
                pos.getY() - radius,
                pos.getZ() - radius,
                pos.getX() + radius,
                pos.getY() + radius,
                pos.getZ() + radius);
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

    @Override
    public void setChanged() {
        if(this.level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
        }
        super.setChanged();
    }

    //-----------MEMORY TABLET-----------//

    protected String savedPlayer = "";

    public String getSavedPlayer(){
        return savedPlayer;
    }

    public void syncFromStorage(){
        //clears any saved player who has linked elsewhere, making room for someone else
        if(level != null){
            MemoryTabletData data = MemoryTabletData.getFromStorage(level, this.savedPlayer);
            if(data != null && data.hasLinkedObelisk() && data.getLinkedObelisk() != getBlockPos()){
                this.savedPlayer = "";
                setChanged();
            }
        }
    }

    public void remember(Player player, MemoryTabletData data){
        this.savedPlayer = player.getStringUUID();
        if(data == null){
            MemoryTabletData newData = new MemoryTabletData();
            newData.setLinkedObelisk(getBlockPos(), player.level().dimension().location().toString(), true);
            MemoryTabletData.createAndSaveToStorage(player, newData);
        }
        else{
            data.setLinkedObelisk(getBlockPos(), player.level().dimension().location().toString(), true);
        }
        setChanged();
    }

    public void forget(Player player, MemoryTabletData data){
        this.savedPlayer = "";
        if(data == null){
            MemoryTabletData newData = new MemoryTabletData();
            newData.setLinkedObelisk(new BlockPos(0,0,0), "minecraft:overworld",false);
            MemoryTabletData.createAndSaveToStorage(player, newData);
        }
        else{
            data.setLinkedObelisk(new BlockPos(0,0,0), "minecraft:overworld",false);
        }
        setChanged();
    }

    public void checkAroundForMemorized(){
        if(level != null && level.getGameTime() % 20 == 0){
            BlockPos pos = getBlockPos();

            List<Player> list = level.getEntitiesOfClass(Player.class, getAreaOfEffect(pos, getRadius()));
            for(Player player : list){
                MemoryTabletData data = MemoryTabletData.getFromStorage(player);
                if(data != null && !player.isDeadOrDying() && hasMemorized(data) && hasXpToRecover(data)){
                    handleExperienceRecovery(player, data);
                    break;
                }
            }
        }
    }

    public boolean hasMemorized(MemoryTabletData data){
        return data != null && data.getLinkedObelisk().equals(getBlockPos());
    }

    public boolean hasXpToRecover(MemoryTabletData data){
        return data != null && data.getExperienceToRecover() > 0;
    }

    public void handleExperienceRecovery(Player player, MemoryTabletData data){
        assert data != null;
        long xp = data.getExperienceToRecover();
        int pointsRecovered = (int) Math.min(5000000 - getExperiencePoints(), xp);

        player.giveExperiencePoints(pointsRecovered); assert level != null;
        level.playSound(null, getBlockPos(), SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0f, 0.25f);

        ServerLevel server = (ServerLevel) level;
        BlockPos pos = getBlockPos();
        server.sendParticles((ServerPlayer) player,
                ParticleTypes.TOTEM_OF_UNDYING, false,
                pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 64, 1, 1, 1, 0.1);

        data.setExperienceToRecover(0);
        player.displayClientMessage(Component.translatable("message.cognition.experience_obelisk.experience_recovered",
                Component.literal(String.valueOf(xpToLevels(pointsRecovered))).withStyle(ChatFormatting.GREEN)), true);
    }

    //-----------FLUID HANDLER-----------//

    public static final BlockCapability<IFluidHandler, Direction> FLUID_HANDLER = Capabilities.FluidHandler.BLOCK;
    protected ExperienceObeliskTank tank = new ExperienceObeliskTank();
    private static final Fluid cognitium = RegisterFluids.COGNITIUM_SOURCE.get();
    public static final int capacity = (int) Math.min((Math.round((double) Config.COMMON.capacity.get() / 20) * 20), 2147483640);

    public class ExperienceObeliskTank extends FluidTank{

        public ExperienceObeliskTank() {
            super(ExperienceObeliskEntity.capacity);
        }

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
            String fluidName = BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString();

            if(stack.getFluid() == cognitium){
                return true;
            }
            else{
                return Config.COMMON.allowedFluids.get().contains(fluidName);
            }
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {

            if(isFluidValid(resource)){
                return super.fill(new FluidStack(cognitium, resource.getAmount()), action);
            }
            else{
                return 0;
            }
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

    }

    public static @Nullable IFluidHandler getCapability(ExperienceObeliskEntity obelisk, Direction direction) {
        return direction == null || !direction.equals(Direction.UP) ? obelisk.tank : null;
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

    public int getExperiencePoints(){
        return getFluidAmount() / 20;
    }

    public int getLevels(){
        return xpToLevels(getExperiencePoints());
    }

    //-----------NBT-----------//

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);

        tank.readFromNBT(provider, tag);
        this.savedPlayer = tag.getString("SavedPlayer");
        this.radius = tag.getDouble("Radius");
        this.redstoneEnabled = tag.getBoolean("isRedstoneControllable");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);

        tank.writeToNBT(provider, tag);
        tag.putString("SavedPlayer", savedPlayer);
        tag.putDouble("Radius", radius);
        tag.putBoolean("isRedstoneControllable", redstoneEnabled);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);

        tank.readFromNBT(provider, tag);
        this.savedPlayer = tag.getString("SavedPlayer");
        this.radius = tag.getDouble("Radius");
        this.redstoneEnabled = tag.getBoolean("isRedstoneControllable");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);

        tank.writeToNBT(provider, tag);
        tag.putString("SavedPlayer", savedPlayer);
        tag.putDouble("Radius", radius);
        tag.putBoolean("isRedstoneControllable", redstoneEnabled);

        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    //-----------LOGIC-----------//

    public void handleRequest(String request, int levels, ServerPlayer sender){

        long playerXP = getTotalXP(sender);
        long finalXP;

        if(Objects.equals(request, UpdateContents.FILL) && this.getSpace() != 0){

            //-----FILLING-----//

            //final amount of experience points the player will have after storing n levels
            finalXP = levelsToXP(sender.experienceLevel - levels) + Math.round(sender.experienceProgress *
                    (levelsToXP(sender.experienceLevel - levels + 1) - levelsToXP(sender.experienceLevel - levels)));

            long addAmount = (playerXP - finalXP) * 20;

            //if amount to add exceeds remaining capacity
            if(sender.experienceLevel >= levels && addAmount >= this.getSpace()){
                sender.giveExperiencePoints(-this.fill(this.getSpace()) / 20); //fill up however much is left and deduct that amount frm player
            }
            //normal operation
            else if(sender.experienceLevel >= levels){
                this.fill((int) (addAmount));
                sender.giveExperienceLevels(-levels);

            }
            //if player has less than the required XP
            else if (playerXP >= 1){
                this.fill((int) (playerXP * 20));
                sender.setExperiencePoints(0);
                sender.setExperienceLevels(0);

            }
        }

        //-----DRAINING-----//

        else if(Objects.equals(request, UpdateContents.DRAIN) && this.getFluidAmount() != 0){

            int amount = this.getFluidAmount();

            finalXP = levelsToXP(sender.experienceLevel + levels) + Math.round(sender.experienceProgress *
                    (levelsToXP(sender.experienceLevel + levels + 1) - levelsToXP(sender.experienceLevel + levels)));

            long drainAmount = (finalXP - playerXP) * 20;

            if(amount >= drainAmount){
                this.drain((int) drainAmount);
                sender.giveExperienceLevels(levels);

            }
            else if(amount >= 1){
                sender.giveExperiencePoints(amount / 20);
                this.setFluid(0);
            }
        }

        //-----FILL OR DRAIN ALL-----//

        else if(Objects.equals(request, UpdateContents.FILL_ALL) && this.getSpace() != 0){

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
        else if(Objects.equals(request, UpdateContents.DRAIN_ALL) && this.getFluidAmount() != 0){

            sender.giveExperiencePoints(this.getFluidAmount() / 20);
            this.setFluid(0);
        }
    }

}


