//package com.l33tfox.petrified.structure;
//
//import com.l33tfox.petrified.Petrified;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.resources.Identifier;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.level.ServerLevelAccessor;
//import net.minecraft.world.level.block.Rotation;
//import net.minecraft.world.level.levelgen.structure.BoundingBox;
//import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
//import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
//import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
//import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
//import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
//import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
//import org.jspecify.annotations.NonNull;
//
//public class MinotaurTombPiece extends TemplateStructurePiece {
//    public MinotaurTombPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation) {
//        super(PStructurePieceType.MINOTAUR_TOMB_PIECE, 0, structureTemplateManager, Petrified.id(templateName), templateName, makeSettings(rotation), position);
//    }
//
//    public MinotaurTombPiece(final StructureTemplateManager structureTemplateManager, final CompoundTag tag) {
//        super(
//                PStructurePieceType.MINOTAUR_TOMB_PIECE,
//                tag,
//                structureTemplateManager,
//                location -> makeSettings(tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow())
//        );
//    }
//
//    private static StructurePlaceSettings makeSettings(final Rotation rotation) {
//        BlockIgnoreProcessor processor = BlockIgnoreProcessor.STRUCTURE_BLOCK;
//        return new StructurePlaceSettings().setIgnoreEntities(true).addProcessor(processor).setRotation(rotation);
//    }
//
//    @Override
//    protected @NonNull Identifier makeTemplateLocation() {
//        return Petrified.id(this.templateName);
//    }
//
//    @Override
//    protected void addAdditionalSaveData(final StructurePieceSerializationContext context, final CompoundTag tag) {
//        super.addAdditionalSaveData(context, tag);
//        tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
//    }
//
//    @Override
//    protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
//
//    }
//}
