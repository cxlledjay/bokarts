package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.block.entity.custom.TrophyBlockEntity;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class TrophyBase extends BlockWithEntity {

    // facing
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    // hitbox
    protected static final VoxelShape SHAPE_NORTH_SOUTH = Block.createCuboidShape(2, 0, 2, 14, 19, 14);
    protected static final VoxelShape SHAPE_EAST_WEST = Block.createCuboidShape(2, 0, 2, 14, 19, 14);


    public TrophyBase(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    public static AbstractBlock.Settings getDefaultSettings() {
        return AbstractBlock.Settings.create()
                .mapColor(MapColor.GOLD)
                .sounds(BlockSoundGroup.METAL)
                .strength(1.5f)
                .requiresTool()
                .nonOpaque()
                .pistonBehavior(PistonBehavior.BLOCK);
    }

    abstract boolean isValidPlacement(int placement);


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST, WEST -> SHAPE_EAST_WEST;
            default -> SHAPE_NORTH_SOUTH;
        };
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
            ItemStack slotStack = blockEntity.getStack(0);

            int placement = stack.getOrDefault(ModDataComponentTypes.TRACK_REPORT_PLACEMENT, 0);

            // insert item
            if(stack.isOf(ModItems.TRACK_REPORT) && slotStack.isEmpty() && isValidPlacement(placement)) {
                if(!world.isClient) {
                    blockEntity.setStack(0, stack.split(1));
                    blockEntity.markDirty();
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    world.updateListeners(pos, state, state, net.minecraft.block.Block.NOTIFY_ALL);
                }
                return ItemActionResult.success(world.isClient);
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
            ItemStack slotStack = blockEntity.getStack(0);

            // get item
            if(player.getMainHandStack().isEmpty() && !slotStack.isEmpty()) {
                if(!world.isClient) {
                    player.getInventory().offerOrDrop(slotStack.copy());
                    blockEntity.setStack(0, ItemStack.EMPTY);
                    blockEntity.markDirty();
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    world.updateListeners(pos, state, state, net.minecraft.block.Block.NOTIFY_ALL);
                }
                return ActionResult.success(world.isClient);
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }


    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.getBlock() != newState.getBlock()) {
            if(world.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
                ItemScatterer.spawn(world, pos, blockEntity);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }


    // -------------------- block entity --------------------

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrophyBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
