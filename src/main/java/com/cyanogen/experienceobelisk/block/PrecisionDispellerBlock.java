package com.cyanogen.experienceobelisk.block;

import com.cyanogen.experienceobelisk.gui.PrecisionDispellerMenu;
import com.cyanogen.experienceobelisk.registries.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class PrecisionDispellerBlock extends ExperienceReceivingBlock implements EntityBlock {

    public PrecisionDispellerBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(9f)
                .destroyTime(1.2f)
                .requiresCorrectToolForDrops()
                .explosionResistance(9f)
                .noOcclusion()
                .lightLevel(pLightEmission -> 7)
                .sound(SoundType.METAL)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        if(super.use(state, level, pos, player, hand, result) != InteractionResult.PASS){
            return InteractionResult.CONSUME;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            NetworkHooks.openScreen((ServerPlayer) player, state.getMenuProvider(level,pos), pos);
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {

        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Precision Dispeller");
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                return new PrecisionDispellerMenu(containerId, inventory, player, pos);
            }
        };

    }

    //-----EW alignment-----//
    //spinny bit
    VoxelShape shape1 = Shapes.create(new AABB(2 / 16D,4 / 16D,4 / 16D,14 / 16D,16 / 16D,12 / 16D));
    //leg 1
    VoxelShape shape2a = Shapes.create(new AABB(5 / 16D,0 / 16D,11.5 / 16D,11 / 16D,7 / 16D,14.5 / 16D));
    VoxelShape shape2b = Shapes.create(new AABB(6 / 16D,7 / 16D,11.5 / 16D,6 / 16D,13 / 16D,14 / 16D));
    VoxelShape shape2 = Shapes.join(shape2a, shape2b, BooleanOp.OR);
    //leg 2
    VoxelShape shape3a = Shapes.create(new AABB(5 / 16D,0 / 16D,1.5 / 16D,11 / 16D,7 / 16D,4.5 / 16D));
    VoxelShape shape3b = Shapes.create(new AABB(6 / 16D,7 / 16D,2 / 16D,6 / 16D,13 / 16D,4.5 / 16D));
    VoxelShape shape3 = Shapes.join(shape3a, shape3b, BooleanOp.OR);

    VoxelShape shapeEW = Shapes.join(Shapes.join(shape1, shape2, BooleanOp.OR), shape3, BooleanOp.OR).optimize();

    //-----NS alignment-----//
    //spinny bit
    VoxelShape shape4 = Shapes.create(new AABB(4 / 16D,4 / 16D,2 / 16D,12 / 16D,16 / 16D,14 / 16D));
    //leg 1
    VoxelShape shape5a = Shapes.create(new AABB(11.5 / 16D,0 / 16D,5 / 16D,14.5 / 16D,7 / 16D,11 / 16D));
    VoxelShape shape5b = Shapes.create(new AABB(11.5 / 16D,7 / 16D,6 / 16D,14 / 16D,13 / 16D,6 / 16D));
    VoxelShape shape5 = Shapes.join(shape5a, shape5b, BooleanOp.OR);
    //leg 2
    VoxelShape shape6a = Shapes.create(new AABB(1.5 / 16D,0 / 16D,5 / 16D,4.5 / 16D,7 / 16D,11 / 16D));
    VoxelShape shape6b = Shapes.create(new AABB(2 / 16D,7 / 16D,6 / 16D,4.5 / 16D,13 / 16D,6 / 16D));
    VoxelShape shape6 = Shapes.join(shape6a, shape6b, BooleanOp.OR);

    VoxelShape shapeNS = Shapes.join(Shapes.join(shape5, shape6, BooleanOp.OR), shape4, BooleanOp.OR).optimize();

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {

        Direction direction = (Direction) state.getValues().get(FACING);
        if(direction == Direction.NORTH || direction == Direction.SOUTH){
            return shapeNS;
        }
        else{
            return shapeEW;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return RegisterBlockEntities.PRECISIONDISPELLER_BE.get().create(pos, state);
    }
}
