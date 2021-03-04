package com.github.abigailfails.helpfulhitboxes.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChainBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChainBlock.class)
public abstract class ChainBlockMixin {
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    protected void checkHeldBlock(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context.hasItem(state.getBlock().asItem())) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}
