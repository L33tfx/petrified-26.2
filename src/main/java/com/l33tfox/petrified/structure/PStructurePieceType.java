//package com.l33tfox.petrified.structure;
//
//import net.minecraft.core.Registry;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.levelgen.structure.StructurePiece;
//import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
//import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
//import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
//import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
//
//import java.util.Locale;
//
//public interface PStructurePieceType {
//    StructurePieceType MINOTAUR_TOMB_PIECE = setTemplatePieceId(MinotaurTombPiece::new, "minotaur_tomb_piece");
//
//    private static StructurePieceType setFullContextPieceId(final StructurePieceType.StructureTemplateType type, final String id) {
//        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, id.toLowerCase(Locale.ROOT), type);
//    }
//
//    private static StructurePieceType setTemplatePieceId(final StructurePieceType.StructureTemplateType type, final String id) {
//        return setFullContextPieceId(type, id);
//    }
//
//}
