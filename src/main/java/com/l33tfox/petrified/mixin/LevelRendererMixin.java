package com.l33tfox.petrified.mixin;

import com.l33tfox.petrified.block.entity.TerracottaSoldierBlockEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.SortedSet;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow
    @Final
    private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;

    // for also showing breaking animation on bottom block when upper block is being broken
    @ModifyExpressionValue(method = "extractVisibleBlockEntities", at = @At(value = "INVOKE",
                    target = "Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;get(J)Ljava/lang/Object;"))
    private Object petrified$checkUpperHalfBreaking(Object original, @Local(name = "blockPos")
            BlockPos blockPos, @Local(name = "blockEntity") BlockEntity blockEntity) {
        if (!(blockEntity instanceof TerracottaSoldierBlockEntity) || original != null) {
            return original;
        }

        Level level = blockEntity.getLevel();

        if (level == null || !level.isClientSide()) {
            return null;
        }

        return destructionProgress.get(blockPos.above().asLong());
    }
}
