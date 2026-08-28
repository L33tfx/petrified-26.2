package com.l33tfox.petrified.structure;

import com.l33tfox.petrified.block.PBlocks;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class TombSoldierProcessor implements StructureProcessor {
    public static final MapCodec<TombSoldierProcessor> CODEC = MapCodec.unit(TombSoldierProcessor::new);

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }

    // Replace all soldier blocks with new blocks to refresh NBT data to randomize poses and weapons
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(
            final LevelReader level,
            final BlockPos targetPosition,
            final BlockPos referencePos,
            final BlockPos templateRelativePos,
            final StructureTemplate.StructureBlockInfo processedBlockInfo,
            final StructurePlaceSettings settings
    ) {
        if (processedBlockInfo.state().is(PBlocks.TERRACOTTA_SOLDIER)) {
            return new StructureTemplate.StructureBlockInfo(
                    processedBlockInfo.pos(),
                    processedBlockInfo.state(),
                    null // wipe nbt data
            );
        }

        return processedBlockInfo;
    }
}
