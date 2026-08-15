//package com.l33tfox.petrified.structure;
//
//import com.mojang.serialization.MapCodec;
//import net.minecraft.core.Registry;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.world.level.levelgen.structure.Structure;
//import net.minecraft.world.level.levelgen.structure.StructureType;
//
//public interface PStructureType<S extends Structure> {
//
//    StructureType<MinotaurTombStructure> MINOTAUR_TOMB = register("minotaur_tomb", MinotaurTombStructure.CODEC);
//
//    MapCodec<S> codec();
//
//    private static <S extends Structure> StructureType<S> register(final String id, final MapCodec<S> codec) {
//        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, id, () -> codec);
//    }
//}
