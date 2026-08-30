package com.l33tfox.petrified.structure;

import com.l33tfox.petrified.Petrified;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class PStructureProcessorTypes {
    public static final StructureProcessorType<TombSoldierProcessor> TOMB_SOLDIER_PROCESSOR = register(
            "tomb_soldier", TombSoldierProcessor.CODEC
    );

    private static <P extends StructureProcessor> StructureProcessorType<P> register(final String id, final MapCodec<P> codec) {
        return (StructureProcessorType)Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, Identifier.fromNamespaceAndPath(Petrified.MOD_ID, id), (StructureProcessorType)() -> codec);
    }

    public static void init() {
    }
}
