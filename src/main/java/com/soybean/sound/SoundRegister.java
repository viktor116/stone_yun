package com.soybean.sound;

import com.soybean.config.InitValue;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/21 17:00
 * @description
 */
public class SoundRegister {
    public static final SoundEvent BLUE_ARROW = register("blue_arrow");
    public static final SoundEvent ALREADY_BOMB = register("already_bomb");
    public static final SoundEvent BOMB_TIME = register("bomb_time");
    public static final SoundEvent ROCKET_LAUNCHER = register("rocket_launcher");


    public static void initialize(){

    }

    public static SoundEvent register(String id){
        Identifier soundID = Identifier.of(InitValue.MOD_ID, id);
        SoundEvent soundEvent = SoundEvent.of(soundID);
        return Registry.register(Registries.SOUND_EVENT, soundID, soundEvent);
    }
}
