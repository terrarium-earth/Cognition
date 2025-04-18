package com.cyanogen.experienceobelisk.block_entities;

import com.cyanogen.experienceobelisk.ExperienceObelisk;
import com.cyanogen.experienceobelisk.config.Config;
import com.cyanogen.experienceobelisk.item.TransformingFocusItem;
import com.cyanogen.experienceobelisk.recipe.MolecularMetamorpherRecipe;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import com.cyanogen.experienceobelisk.registries.RegisterItems;
import com.cyanogen.experienceobelisk.registries.RegisterRecipes;
import com.cyanogen.experienceobelisk.registries.RegisterSounds;
import com.cyanogen.experienceobelisk.utils.RecipeUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MolecularMetamorpherEntity extends ExperienceReceivingEntity implements GeoBlockEntity {

    public MolecularMetamorpherEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.MOLECULAR_METAMORPHER.get(), pos, state);
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

    public static final BlockCapability<IItemHandler, Direction> ITEM_HANDLER = Capabilities.ItemHandler.BLOCK;

    public static @Nullable IItemHandler getCapability(MolecularMetamorpherEntity metamorpher, Direction direction){
        if(direction == null || direction.equals(Direction.UP)){
            return null;
        }
        else{
            return direction.equals(Direction.DOWN) ? metamorpher.outputHandler : metamorpher.inputHandler;
        }
    }

    protected ItemStackHandler inputHandler = inputHandler();
    protected ItemStackHandler outputHandler = outputHandler();

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

    public boolean isEmpty(){

        return inputHandler.getStackInSlot(0).isEmpty() &&
                inputHandler.getStackInSlot(1).isEmpty() &&
                inputHandler.getStackInSlot(2).isEmpty() &&
                outputHandler.getStackInSlot(0).isEmpty();
    }

    //-----------RECIPE HANDLER-----------//

    public boolean handleJsonRecipes(){

        if(getRecipe() != null){
            MolecularMetamorpherRecipe recipe = getRecipe();
            ItemStack output = recipe.assemble(getRecipeInput(), level == null ? null : level.registryAccess());
            int cost = recipe.getCost();

            if(canPerformRecipe(output, cost)){
                initiateRecipe(recipe);
                return true;
            }
        }
        return false;
    }

    public boolean canPerformRecipe(ItemStack output, int cost){

        //here we check for criteria that are independent of recipe inputs

        ItemStack stackInResults = outputHandler.getStackInSlot(0);

        return getBoundObelisk() != null //has been bound to a valid obelisk
                && getBoundObelisk().getFluidAmount() >= cost * 20 //obelisk has enough Cognitium
                && (ItemStack.isSameItemSameComponents(stackInResults, output) || stackInResults.isEmpty() || stackInResults.is(Items.AIR)) //results slot empty or same as output
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

    /**Returns true if the recipe has not been changed
     * or if the inputs have been changed to equivalent ones, such as: swapping out an ingredient for another valid ingredient.
    *Returns false if the recipe has been changed to a different one or if the recipe is now invalid --
     * in which case, the XP is refunded and the metamorpher is reset
     */

    public boolean validateRecipe(){

        boolean hasValidJsonRecipe = (getRecipe() != null) && getRecipe().getId().equals(recipeId);

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

        SimpleContainer container = new SimpleContainer(3);
        for(int k = 0; k < 3; k++){
            container.setItem(k, inputHandler.getStackInSlot(k).copy());
        }

        for(int j = 1; j <= 3; j++){
            Ingredient ingredient = recipe.getIngredients(true).get(j).getA();
            int count = recipe.getIngredients(true).get(j).getB();

            for(int i = 0; i < 3; i++){
                ItemStack stack = container.getItem(i);

                if(ingredient.test(stack)){

                    if(stack.is(RegisterItems.TRANSFORMING_FOCUS.get())){
                        if(stack.getDamageValue() >= TransformingFocusItem.durability - 1){
                            stack.shrink(1);
                        }
                        else{
                            int damage = stack.getDamageValue();
                            stack.setDamageValue(damage + 1);
                        }
                        break;
                    }
                    else if(stack.hasCraftingRemainingItem()){
                        container.setItem(i, stack.getCraftingRemainingItem());
                        break;
                    }
                    else if(stack.getCount() >= count){
                        stack.shrink(count);
                        break;
                    }
                    else{
                        count = count - stack.getCount();
                        stack.shrink(stack.getCount());
                    }
                }
            }
        }
        return container;
    }

    public void dispenseResult(){

        MolecularMetamorpherRecipe recipe = null;

        List<RecipeHolder<MolecularMetamorpherRecipe>> list = level.getRecipeManager().getAllRecipesFor(RegisterRecipes.MOLECULAR_METAMORPHER_TYPE.get());
        for(RecipeHolder<MolecularMetamorpherRecipe> holder : list){
            if(holder.value().getId().equals(recipeId)){
                recipe = holder.value();
            }
        }

        if(recipe != null){
            setProcessing(false);
            SimpleContainer resultState = deplete(recipe);
            ItemStack resultItem = recipe.getResultItem(null);
            ItemStack stackInResults = outputHandler.getStackInSlot(0).copy();

            int count = resultItem.getCount();

            if(ItemStack.isSameItemSameComponents(resultItem, stackInResults)){
                stackInResults.grow(count);
                outputHandler.setStackInSlot(0, stackInResults);
            }
            else{
                outputHandler.setStackInSlot(0, resultItem);
            }

            for(int i = 0; i < 3; i++){
                inputHandler.setStackInSlot(i, resultState.getItem(i));
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
        ItemStack nameTag = inputHandler.getStackInSlot(1);
        ItemStack formatStack = inputHandler.getStackInSlot(2);
        Item formatItem = formatStack.getItem();

        MutableComponent name = inputItem.getHoverName().copy();

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

        Ingredient ingredient1 = Ingredient.of(inputItem.copy());
        int count1 = inputItem.getCount();
        Ingredient ingredient2 = Ingredient.of(nameTag.copy());
        int count2 = nameTag.getCount();
        Ingredient ingredient3 = Ingredient.of(formatStack.copy());
        int count3 = formatStack.getCount();

        ItemStack output = inputItem.copy();
        output.set(DataComponents.ITEM_NAME, name);
        int cost = 315;
        int processTime = 60;

        return new MolecularMetamorpherRecipe(ingredient1, count1, ingredient2, count2, ingredient3, count3, output, cost, processTime,
                ResourceLocation.fromNamespaceAndPath(ExperienceObelisk.MOD_ID, "item_name_formatting"));
    }

    //-----------UTILITY METHODS-----------//

    public @Nullable MolecularMetamorpherRecipe getRecipe(){
        Optional<RecipeHolder<MolecularMetamorpherRecipe>> holder =
                this.level.getRecipeManager().getRecipeFor(MolecularMetamorpherRecipe.Type.INSTANCE, getRecipeInput(), level);

        return holder.map(RecipeHolder::value).orElse(null);
    }

    public RecipeInput getRecipeInput(){

        return new RecipeInput() {
            @Override
            public ItemStack getItem(int slot) {
                return switch (slot) {
                    case 0 -> inputHandler.getStackInSlot(0).copy();
                    case 1 -> inputHandler.getStackInSlot(1).copy();
                    case 2 -> inputHandler.getStackInSlot(2).copy();
                    default -> ItemStack.EMPTY;
                };
            }

            @Override
            public int size() {
                return 3;
            }
        };
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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.loadAdditional(tag, provider);

        inputHandler.deserializeNBT(provider, tag.getCompound("Inputs"));
        outputHandler.deserializeNBT(provider, tag.getCompound("Outputs"));

        this.isProcessing = tag.getBoolean("IsProcessing");
        this.processTime = tag.getInt("ProcessTime");
        this.processProgress = tag.getInt("ProcessProgress");
        this.recipeId = ResourceLocation.bySeparator(tag.getString("RecipeID"),':');
        this.recipeCost = tag.getInt("RecipeCost");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

        super.saveAdditional(tag, provider);

        tag.put("Inputs", inputHandler.serializeNBT(provider));
        tag.put("Outputs", outputHandler.serializeNBT(provider));

        tag.putBoolean("IsProcessing", isProcessing);
        tag.putInt("ProcessTime", processTime);
        tag.putInt("ProcessProgress", processProgress);
        tag.putInt("RecipeCost", recipeCost);

        if(recipeId != null){
            tag.putString("RecipeID", recipeId.toString());
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {

        super.handleUpdateTag(tag, provider);

        inputHandler.deserializeNBT(provider, tag.getCompound("Inputs"));
        outputHandler.deserializeNBT(provider, tag.getCompound("Outputs"));

        this.isProcessing = tag.getBoolean("IsProcessing");
        this.processTime = tag.getInt("ProcessTime");
        this.processProgress = tag.getInt("ProcessProgress");
        this.recipeId = ResourceLocation.bySeparator(tag.getString("RecipeID"),':');
        this.recipeCost = tag.getInt("RecipeCost");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        CompoundTag tag = super.getUpdateTag(provider);

        tag.put("Inputs", inputHandler.serializeNBT(provider));
        tag.put("Outputs", outputHandler.serializeNBT(provider));

        tag.putBoolean("IsProcessing", isProcessing);
        tag.putInt("ProcessTime", processTime);
        tag.putInt("ProcessProgress", processProgress);
        tag.putInt("RecipeCost", recipeCost);

        if(recipeId != null){
            tag.putString("RecipeID", recipeId.toString());
        }

        return tag;
    }


}
