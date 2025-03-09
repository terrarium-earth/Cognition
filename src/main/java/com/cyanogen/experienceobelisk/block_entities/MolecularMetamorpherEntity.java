package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.item.TransformingFocusItem;
import com.cyanogen.experienceobelisk.recipe.MolecularMetamorpherRecipe;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.utils.RecipeUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MolecularMetamorpherEntity extends ExperienceReceivingEntity implements GeoBlockEntity {

    public MolecularMetamorpherEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.MOLECULAR_METAMORPHER_BE.get(), pos, state);
    }

    boolean isProcessing = false;
    boolean busy = false;
    int processTime = 0;
    int processProgress = 0;
    int recipeCost = 0;
    ResourceLocation recipeId;

    //-----------ANIMATIONS-----------//

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    protected static final RawAnimation ACTIVE = RawAnimation.begin().thenPlay("active");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, this::controller));
    }

    protected <E extends MolecularMetamorpherEntity> PlayState controller(final AnimationState<E> state){

        MolecularMetamorpherEntity metamorpher = state.getAnimatable();
        AnimationController<E> controller = state.getController();
        RawAnimation animation = controller.getCurrentRawAnimation();

        if(animation == null){
            controller.setAnimation(IDLE);
        }
        else{
            if(metamorpher.busy && animation.equals(IDLE)){
                controller.setAnimation(ACTIVE);
            }
            else if(!metamorpher.busy && animation.equals(ACTIVE)){
                controller.setAnimation(IDLE);
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //-----------BEHAVIOR-----------//

    public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {

        if(blockEntity instanceof MolecularMetamorpherEntity metamorpher){

            boolean active = !metamorpher.redstoneEnabled || level.hasNeighborSignal(pos);
            metamorpher.sendObeliskInfoToScreen();

            if(metamorpher.isProcessing){

                metamorpher.busy = true;

                if(metamorpher.processProgress >= metamorpher.processTime){
                    metamorpher.dispenseResult();
                }
                else{
                    if(metamorpher.validateRecipe()){
                        metamorpher.incrementProcessProgress();
                    }
                }
            }
            else if(active && metamorpher.hasContents()){

                if(metamorpher.handleJsonRecipes()){
                    metamorpher.busy = true;
                }
                else metamorpher.busy = metamorpher.handleNameFormattingRecipes();

            }
            else{
                metamorpher.busy = false;
            }

            if(metamorpher.busy){
                metamorpher.handleAudio(level, pos);
            }

        }

    }

    public boolean hasContents(){

        boolean hasContents = false;

        for(int i = 0; i < 3; i++){
            ItemStack stack = inputHandler.getStackInSlot(i);
            if(!stack.isEmpty() && !stack.getItem().equals(Items.AIR)){
                hasContents = true;
                break;
            }
        }
        return hasContents;
    }

    public void handleAudio(Level level, BlockPos pos){

        int period = 15;

        long time = level.getGameTime();
        if(time % period == 0){
            level.playSound(null, pos, RegisterSounds.METAMORPHER_BUSY1.get(), SoundSource.BLOCKS, 0.8f,1f);
        }
        else if(time % period == period / 2){
            level.playSound(null, pos, RegisterSounds.METAMORPHER_BUSY2.get(), SoundSource.BLOCKS, 0.7f,1f);
        }
    }

    //-----------ITEM HANDLER-----------//

    protected ItemStackHandler inputHandler = inputHandler();
    protected ItemStackHandler outputHandler = outputHandler();
    private final LazyOptional<IItemHandler> inputHandlerOptional = LazyOptional.of(() -> inputHandler);
    private final LazyOptional<IItemHandler> outputHandlerOptional = LazyOptional.of(() -> outputHandler);

    public ItemStackHandler inputHandler() {
        return new ItemStackHandler(3);
    }

    public ItemStackHandler outputHandler(){
        return new ItemStackHandler(1){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return false;
            }
        };
    }

    public ItemStackHandler getInputHandler(){
        return inputHandler;
    }

    public ItemStackHandler getOutputHandler(){
        return outputHandler;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if(capability == ForgeCapabilities.ITEM_HANDLER){

            if(facing == Direction.DOWN){
                return outputHandlerOptional.cast();
            }
            else if(facing != Direction.UP){
                return inputHandlerOptional.cast();
            }
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        inputHandlerOptional.invalidate();
        outputHandlerOptional.invalidate();
        super.invalidateCaps();
    }

    //-----------RECIPE HANDLER-----------//

    public boolean handleJsonRecipes(){

        if(getRecipe().isPresent()){
            MolecularMetamorpherRecipe recipe = getRecipe().get();
            ItemStack output = recipe.getResultItem(null);
            int cost = recipe.getCost();

            if(canPerformRecipe(output, cost)){
                initiateRecipe(recipe);
                return true;
            }
        }
        return false;
    }

    public boolean canPerformRecipe(ItemStack output, int cost){

        //here we check for criteria that are independent of recipe ingredients

        ItemStack stackInResults = outputHandler.getStackInSlot(0);

        return getBoundObelisk() != null //has been bound to a valid obelisk
                && getBoundObelisk().getFluidAmount() >= cost * 20 //obelisk has enough Cognitium
                && (ItemStack.isSameItemSameTags(stackInResults, output)
                || stackInResults.isEmpty() || stackInResults.is(Items.AIR)) //results slot empty or same as output
                && stackInResults.getCount() <= output.getMaxStackSize() - output.getCount(); //results slot can accommodate output
    }

    public void initiateRecipe(MolecularMetamorpherRecipe recipe){

        this.setProcessing(true);
        this.setRecipeId(recipe);
        this.setProcessProgress(0);
        this.setProcessTime(recipe.getProcessTime());
        this.setRecipeCost(recipe.getCost());
        this.getBoundObelisk().drain(recipe.getCost() * 20);
    }

    public boolean validateRecipe(){

        //returns true if the recipe has not been changed
        //or if the inputs have been changed to equivalent ones, such as: swapping out an ingredient for another valid ingredient

        //returns false if the recipe has been changed to a different one or if the recipe is now invalid
        //in which case, the XP is refunded and the metamorpher is reset

        boolean hasValidJsonRecipe = getRecipe().isPresent() && getRecipe().get().getId().equals(recipeId);

        if(hasValidJsonRecipe || hasNameFormattingRecipe()){
            return true;
        }
        else{
            if(getBoundObelisk() != null){
                getBoundObelisk().fill(20 * recipeCost);
            }
            setProcessing(false);
            resetAll();
            return false;
        }
    }

    public SimpleContainer deplete(MolecularMetamorpherRecipe recipe){

        SimpleContainer container = getSimpleContainer();

        Map<Ingredient, Tuple<Integer, Integer>> ingredientMap = recipe.getIngredientMapNoFiller();

        for(Map.Entry<Ingredient, Tuple<Integer, Integer>> entry : ingredientMap.entrySet()){

            Ingredient ingredient = entry.getKey();
            int count = entry.getValue().getB();

            for(int i = 0; i < 3; i++){
                ItemStack stack = container.getItem(i);

                if(ingredient.test(stack)){

                    if(stack.is(RegisterItems.TRANSFORMING_FOCUS.get())){
                        if(stack.getDamageValue() >= TransformingFocusItem.durability - 1){
                            stack.shrink(1);
                        }
                        else{
                            stack.hurt(1, RandomSource.create(), null);
                        }
                    }
                    else if(stack.hasCraftingRemainingItem()){
                        container.setItem(i, stack.getCraftingRemainingItem());
                    }
                    else{
                        stack.shrink(count);
                    }
                    break;
                }
            }
        }

        return container;
    }

    public void dispenseResult(){

        setProcessing(false);

        Optional<? extends Recipe<?>> optional = level.getRecipeManager().byKey(recipeId);
        MolecularMetamorpherRecipe recipe = null;

        if(optional.isPresent() && optional.get() instanceof MolecularMetamorpherRecipe){
            recipe = (MolecularMetamorpherRecipe) optional.get();
        }
        else if(hasNameFormattingRecipe()){
            recipe = getNameFormattingRecipe();
        }

        if(recipe != null){
            ItemStack result = recipe.getResultItem(null);
            ItemStack stackInResults = outputHandler.getStackInSlot(0).copy();
            SimpleContainer remainders = deplete(recipe);
            int count = result.getCount();

            if(ItemStack.isSameItemSameTags(result, stackInResults)){
                stackInResults.grow(count);
                outputHandler.setStackInSlot(0, stackInResults);
            }
            else{
                outputHandler.setStackInSlot(0, result);
            }

            for(int i = 0; i < 3; i++){
                inputHandler.setStackInSlot(i, remainders.getItem(i));
            }
        }

        resetAll();
    }

    //-----------NON-JSON RECIPES-----------//

    public boolean handleNameFormattingRecipes(){

        if(Config.COMMON.formatting.get() && hasNameFormattingRecipe()){

            MolecularMetamorpherRecipe recipe = getNameFormattingRecipe();
            ItemStack output = recipe.getResultItem(null);
            int cost = recipe.getCost();

            if(canPerformRecipe(output, cost)){
                initiateRecipe(recipe);
                return true;
            }
        }
        return false;
    }

    public boolean hasNameFormattingRecipe(){
        Item formatItem = inputHandler.getStackInSlot(2).getItem();

        return !inputHandler.getStackInSlot(0).isEmpty() //any item
                && inputHandler.getStackInSlot(1).is(Items.NAME_TAG) //a name tag
                && (formatItem instanceof DyeItem || RecipeUtils.getValidFormattingItems().contains(formatItem)); //a valid formatting item
    }

    public MolecularMetamorpherRecipe getNameFormattingRecipe(){

        ItemStack inputItem = inputHandler.getStackInSlot(0);
        MutableComponent name = inputItem.getHoverName().copy();
        Item formatItem = inputHandler.getStackInSlot(2).getItem();

        if(RecipeUtils.getValidDyes().contains(formatItem)){

            DyeItem dye = (DyeItem) formatItem;
            int dyeColor = dye.getDyeColor().getId();
            ChatFormatting format = ChatFormatting.getById(RecipeUtils.dyeColorToTextColor(dyeColor));

            if (format != null) {
                name = name.withStyle(format);
            }
        }
        else if(RecipeUtils.getValidFormattingItems().contains(formatItem)){
            int index = RecipeUtils.getValidFormattingItems().indexOf(formatItem);
            char code = RecipeUtils.itemToFormat(index);
            ChatFormatting format = ChatFormatting.getByCode(code);

            if (format != null) {
                name = name.withStyle(format);
            }
        }

        Map<Ingredient, Tuple<Integer, Integer>> ingredientMap = new HashMap<>();
        ingredientMap.put(Ingredient.of(inputItem.copy()), new Tuple<>(1, inputItem.getCount()));
        ingredientMap.put(Ingredient.of(inputHandler.getStackInSlot(1).copy()), new Tuple<>(2, inputHandler.getStackInSlot(1).getCount()));
        ingredientMap.put(Ingredient.of(inputHandler.getStackInSlot(2).copy()), new Tuple<>(3, inputHandler.getStackInSlot(2).getCount()));

        ItemStack output = inputItem.copy().setHoverName(name);
        int cost = 315;
        int processTime = 60;

        return new MolecularMetamorpherRecipe(ImmutableMap.copyOf(ingredientMap), output, cost, processTime,
                new ResourceLocation(ExperienceObelisk.MOD_ID, "item_name_formatting"));
    }

    //-----------UTILITY METHODS-----------//

    public Optional<MolecularMetamorpherRecipe> getRecipe(){
        return this.level.getRecipeManager().getRecipeFor(MolecularMetamorpherRecipe.Type.INSTANCE, getSimpleContainer(), level);
    }

    public SimpleContainer getSimpleContainer(){
        SimpleContainer container = new SimpleContainer(3);
        container.setItem(0, inputHandler.getStackInSlot(0).copy());
        container.setItem(1, inputHandler.getStackInSlot(1).copy());
        container.setItem(2, inputHandler.getStackInSlot(2).copy());

        return container;
    }

    public boolean isProcessing(){
        return isProcessing;
    }

    public void setProcessing(boolean isProcessing){
        this.isProcessing = isProcessing;
        setChanged();
    }

    public boolean isBusy(){
        //separate check from isProcessing that does not get interrupted between ending & initiating recipes
        //this is to prevent the animation from becoming choppy
        return this.busy;
    }

    public int getProcessTime(){
        return processTime;
    }

    public void setProcessTime(int time){
        this.processTime = time;
        setChanged();
    }

    public int getProcessProgress(){
        return processProgress;
    }

    public void setProcessProgress(int progress){
        this.processProgress = progress;
        setChanged();
    }

    public void incrementProcessProgress(){
        this.processProgress += 1;
        setChanged();
    }

    public void setRecipeId(MolecularMetamorpherRecipe recipe){
        this.recipeId = recipe.getId();
        setChanged();
    }

    public void setRecipeCost(int cost){
        this.recipeCost = cost;
        setChanged();
    }

    public void resetAll(){

        processProgress = 0;
        processTime = 0;
        recipeId = null;
        recipeCost = 0;
        setChanged();
    }

    //-----------NBT-----------//

    @Override
    public void setChanged() {
        if(this.level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
            //do this if live block entity data is needed in the GUI
        }
        super.setChanged();
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);

        inputHandler.deserializeNBT(tag.getCompound("Inputs"));
        outputHandler.deserializeNBT(tag.getCompound("Outputs"));

        this.isProcessing = tag.getBoolean("IsProcessing");
        this.processTime = tag.getInt("ProcessTime");
        this.processProgress = tag.getInt("ProcessProgress");
        this.recipeId = new ResourceLocation(tag.getString("RecipeID"));
        this.recipeCost = tag.getInt("RecipeCost");
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);

        tag.put("Inputs", inputHandler.serializeNBT());
        tag.put("Outputs", outputHandler.serializeNBT());

        tag.putBoolean("IsProcessing", isProcessing);
        tag.putInt("ProcessTime", processTime);
        tag.putInt("ProcessProgress", processProgress);
        tag.putInt("RecipeCost", recipeCost);

        if(recipeId != null){
            tag.putString("RecipeID", recipeId.toString());
        }
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();

        tag.put("Inputs", inputHandler.serializeNBT());
        tag.put("Outputs", outputHandler.serializeNBT());

        tag.putBoolean("IsProcessing", isProcessing);
        tag.putInt("ProcessTime", processTime);
        tag.putInt("ProcessProgress", processProgress);
        tag.putInt("RecipeCost", recipeCost);

        if(recipeId != null){
            tag.putString("RecipeID", recipeId.toString());
        }

        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
