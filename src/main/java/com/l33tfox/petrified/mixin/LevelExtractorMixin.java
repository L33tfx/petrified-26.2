package com.l33tfox.petrified.mixin;

import com.l33tfox.petrified.block.PBlocks;
import com.l33tfox.petrified.block.TerracottaSoldierBlock;
import com.l33tfox.petrified.block.entity.TerracottaSoldierBlockEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.SortedSet;

@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin {

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

        ClientLevel clientLevel = (ClientLevel) level;
        return clientLevel.destructionProgress().get(blockPos.above().asLong());
    }
}
