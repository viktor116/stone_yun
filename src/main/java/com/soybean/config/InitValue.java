package com.soybean.config;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author soybean
 * @date 2024/10/8 15:46
 * @description
 */
public class InitValue {

    public static final Logger LOGGER = LoggerFactory.getLogger(InitValue.MOD_ID);
    public static final String MOD_ID = "stone";
    public static final String MINECRAFT = "minecraft";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
