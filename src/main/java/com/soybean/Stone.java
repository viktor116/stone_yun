package com.soybean;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.enchant.EnchantmentRegister;
import com.soybean.entity.EntityRegister;
import com.soybean.event.EventRegister;
import com.soybean.items.ItemsRegister;
import com.soybean.items.armor.ModArmorMaterials;
import com.soybean.world.portals.PortalsRegister;
import net.fabricmc.api.ModInitializer;

public class Stone implements ModInitializer {

	@Override
	public void onInitialize() {
		ModBlock.initialize();
		EnchantmentRegister.Initialize();
		ModArmorMaterials.initialize();
		EventRegister.Initialize();
		ItemsRegister.initialize();
		EntityRegister.initialize();
		PortalsRegister.Initialize();
		InitValue.LOGGER.info("make in soybean =v=!");
	}
}