package com.soybean;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.items.ItemsRegister;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stone implements ModInitializer {

	@Override
	public void onInitialize() {
		ModBlock.initialize();
		ItemsRegister.initialize();
		InitValue.LOGGER.info("make in soybean =v=!");
	}
}