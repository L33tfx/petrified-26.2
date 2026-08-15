//package com.l33tfox.petrified.structure;
//
//import com.google.common.collect.Lists;
//import com.l33tfox.petrified.Petrified;
//import com.mojang.serialization.MapCodec;
//import net.minecraft.core.BlockPos;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.level.block.Rotation;
//import net.minecraft.world.level.levelgen.structure.Structure;
//import net.minecraft.world.level.levelgen.structure.StructurePiece;
//import net.minecraft.world.level.levelgen.structure.StructureType;
//import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
//import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
//import org.jspecify.annotations.NonNull;
//
//import java.util.List;
//import java.util.Optional;
//
//public class MinotaurTombStructure extends Structure {
//    public static final MapCodec<MinotaurTombStructure> CODEC = simpleCodec(MinotaurTombStructure::new);
//
//    public MinotaurTombStructure(final Structure.StructureSettings settings) {
//        super(settings);
//    }
//
//    @Override
//    public Optional<Structure.GenerationStub> findGenerationPoint(final Structure.GenerationContext context) {
//        Rotation rotation = Rotation.getRandom(context.random());
//        return Optional.of(new Structure.GenerationStub(context.chunkPos().getWorldPosition(), builder -> generatePieces(builder, context, rotation)));
//    }
//
//    private void generatePieces(final StructurePiecesBuilder builder, final Structure.GenerationContext context, final Rotation rotation) {
//        int QUADRANT_SIZE = 40; // Change this to your quadrant width/length
//
//        // 1. Unrotated local offsets for a 2x2 grid
//        BlockPos nwOffset = BlockPos.ZERO;
//        BlockPos neOffset = new BlockPos(QUADRANT_SIZE, 0, 0);
//        BlockPos swOffset = new BlockPos(0, 0, QUADRANT_SIZE);
//        BlockPos seOffset = new BlockPos(QUADRANT_SIZE, 0, QUADRANT_SIZE);
//
//        // 2. Rotate offsets around the origin based on selected structure rotation
//        BlockPos nwPos = origin.add(nwOffset.rotate(rotation));
//        BlockPos nePos = origin.add(neOffset.rotate(rotation));
//        BlockPos swPos = origin.add(swOffset.rotate(rotation));
//        BlockPos sePos = origin.add(seOffset.rotate(rotation));
//
//        Identifier nwId = Petrified.id("minotaur_tomb/tomb_nw");
//        Identifier neId = Petrified.id("minotaur_tomb/tomb_ne");
//        Identifier swId = Petrified.id("minotaur_tomb/tomb_sw");
//        Identifier seId = Petrified.id("minotaur_tomb/tomb_se");
//
//        // 4. Add pieces to the collector
//        builder.addPiece(new MinotaurTombPiece(builder, nwId, nwPos, rotation));
//        builder.addPiece(new MinotaurTombPiece(builder, neId, nePos, rotation));
//        builder.addPiece(new MinotaurTombPiece(builder, swId, swPos, rotation));
//        builder.addPiece(new MinotaurTombPiece(builder, seId, sePos, rotation));
//    }
//
//    @Override
//    public @NonNull StructureType<?> type() {
//        return PStructureType.MINOTAUR_TOMB;
//    }
//}
