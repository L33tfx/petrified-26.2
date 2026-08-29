package com.l33tfox.petrified.sounds;

import com.l33tfox.petrified.Petrified;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class PSounds {
    private PSounds() {
        // private empty constructor to avoid accidental instantiation - from fabric docs
    }

    public static final SoundEvent MINOTAUR_GRUNT = registerSound("minotaur_grunt");
    public static final SoundEvent MINOTAUR_ROAR = registerSound("minotaur_roar");
    public static final SoundEvent MINOTAUR_STEP = registerSound("minotaur_step");
    public static final SoundEvent MINOTAUR_IDLE = registerSound("minotaur_idle");
    public static final SoundEvent MINOTAUR_SWING = registerSound("minotaur_swing");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(Petrified.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void init() {
    }
}
