package com.github.abigailfails.helpfulhitboxes;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HelpfulHitboxes.MOD_ID)
public class HelpfulHitboxesEvents {
    @SubscribeEvent
    public static void onPlaceBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Item item = event.getItemStack().getItem();
        BlockState originalState = world.getBlockState(pos);
        if (originalState.getBlock() instanceof AbstractRailBlock && item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractRailBlock) {
            Direction direction = event.getPlayer().getHorizontalFacing();
            BlockPos.Mutable currentPos = event.getPos().toMutable().move(direction);
            for (int i = 0; i < 7; i++) {
                BlockPos nextPos = null;
                boolean isNextRail = false;
                BlockPos.Mutable yCheckingPos = currentPos.toMutable().move(Direction.DOWN);
                for (int j = 0; j < 3; j++) {
                    if (world.getBlockState(yCheckingPos).getBlock() instanceof AbstractRailBlock) {
                        nextPos = yCheckingPos.move(direction).toImmutable();
                        isNextRail = true;
                    } else if (!isNextRail) {
                        BlockItemUseContext context = new BlockItemUseContext(event.getPlayer(), event.getHand(), event.getItemStack(), event.getHitVec().withPosition(yCheckingPos));
                        if (world.getBlockState(yCheckingPos).isReplaceable(context)) {
                            BlockState stateForPlacement = originalState.getBlock().getStateForPlacement(context);
                            if (stateForPlacement != null && stateForPlacement.isValidPosition(world, yCheckingPos))
                            nextPos = yCheckingPos.toImmutable();
                        }
                    }
                    yCheckingPos.move(Direction.UP);
                }
                if (!isNextRail) {
                    if (nextPos != null) {
                        BlockItemUseContext context = new BlockItemUseContext(event.getPlayer(), event.getHand(), event.getItemStack(), event.getHitVec().withPosition(nextPos));
                        ((BlockItem) item).tryPlace(context);
                        event.setCancellationResult(ActionResultType.SUCCESS);
                        event.setCanceled(true);
                    }
                    break;
                } else {
                    currentPos.setPos(nextPos);
                }
            }
        }
    }
}

