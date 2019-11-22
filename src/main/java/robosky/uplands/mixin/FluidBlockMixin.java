package robosky.uplands.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.state.StateFactory;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import robosky.uplands.block.UplandsWaterBlock;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block {

    private FluidBlockMixin() {
        super(null);
    }

    @Inject(method = "appendProperties", at = @At("RETURN"))
    private void onAppendProperties(StateFactory.Builder<Block, BlockState> builder, CallbackInfo info) {
        // You see...since this is called in the superclass constructor,
        // none of this object's fields has been initialized (including this.fluid),
        // so we can't use the fluid to determine if this object is water or lava.
        // Nor can we compare this object with Blocks.WATER because the Blocks
        // class hasn't been initialized yet. However, Blocks.WATER is initialized
        // first, so it is null when the water block is being created and is
        // nonnull on every subsequent execution of this method.
        if(Blocks.WATER == null) {
            builder.add(UplandsWaterBlock.FALL());
        }
    }

    // so we can see the block state property values
    @Inject(method = "getOutlineShape", at = @At("RETURN"), cancellable = true)
    private void replaceOutlineShape(CallbackInfoReturnable<VoxelShape> info) {
        info.setReturnValue(VoxelShapes.fullCube());
    }
}
