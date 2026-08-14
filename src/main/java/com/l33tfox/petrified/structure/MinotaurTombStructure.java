package com.l33tfox.petrified.structure;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class MinotaurTombStructure extends Structure {
    public static final MapCodec<MinotaurTombStructure> CODEC = simpleCodec(MinotaurTombStructure::new);

    public MinotaurTombStructure(final Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(final Structure.GenerationContext context) {
        return Optional.of(new Structure.GenerationStub(context.chunkPos().getWorldPosition(), builder -> generatePieces(builder, context)));
    }

    private void generatePieces(final StructurePiecesBuilder builder, final Structure.GenerationContext context) {
    }

    @Override
    public @NonNull StructureType<?> type() {
        return PStructureType.MINOTAUR_TOMB;
    }
}
