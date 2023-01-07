package com.electrolyte.unstable.mixin;

import com.electrolyte.unstable.blocks.CursedEarth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseSpawner.class)
public abstract class MixinBaseSpawner {

    @Inject(method = "isNearPlayer", at = @At(value = "HEAD"), cancellable = true)
    private void isNearPlayer(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(level.getBlockEntity(pos) instanceof SpawnerBlockEntity) {
            if(level.getBlockState(pos.below()).getBlock() instanceof CursedEarth) {
                cir.setReturnValue(true);
            }
        }
    }
}
