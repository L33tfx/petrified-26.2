package com.l33tfox.petrified;

import com.l33tfox.petrified.block.PBlocks;
import com.l33tfox.petrified.block.entity.PBlockEntityTypes;
import com.l33tfox.petrified.entity.PEntityTypes;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Petrified implements ModInitializer {
	public static final String MOD_ID = "petrified";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PBlocks.init();
		PBlockEntityTypes.init();
		PEntityTypes.registerAttributes();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
