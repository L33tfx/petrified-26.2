package com.l33tfox.petrified.structure;

import com.l33tfox.petrified.Petrified;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class PStructureProcessorTypes {
    public static final MapCodec<TombSoldierProcessor> TOMB_SOLDIER_PROCESSOR =
            register("tomb_soldier", TombSoldierProcessor.CODEC);

    private static <P extends StructureProcessor> MapCodec<P> register(
            String id,
            MapCodec<P> codec
    ) {
        return Registry.register(
                BuiltInRegistries.STRUCTURE_PROCESSOR,
                Identifier.fromNamespaceAndPath(Petrified.MOD_ID, id),
                codec
        );
    }

    public static void init() {
    }
}
