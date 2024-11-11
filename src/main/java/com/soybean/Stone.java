package com.soybean;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.entity.EntityRegister;
import com.soybean.items.ItemsRegister;
import com.soybean.world.portals.PortalsRegister;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stone implements ModInitializer {

	@Override
	public void onInitialize() {
		ModBlock.initialize();
		ItemsRegister.initialize();
		EntityRegister.initialize();
		PortalsRegister.Initialize();
		InitValue.LOGGER.info("make in soybean =v=!");
	}
}