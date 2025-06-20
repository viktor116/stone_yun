package com.soybean;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.enchant.EnchantmentRegister;
import com.soybean.entity.EntityRegister;
import com.soybean.event.EventRegister;
import com.soybean.init.ScreenHandlerTypeInit;
import com.soybean.items.ItemsRegister;
import com.soybean.items.armor.ModArmorMaterials;
import com.soybean.network.NetworkRegister;
import com.soybean.particles.ParticlesRegister;
import com.soybean.screen.ScreenRegister;
import com.soybean.sound.SoundRegister;
import com.soybean.utils.ServerEachTickTaskManager;
import com.soybean.world.portals.PortalsRegister;
import net.fabricmc.api.ModInitializer;

public class Stone implements ModInitializer {

	@Override
	public void onInitialize() {

		ItemsRegister.initialize();
		EntityRegister.initialize();
		ModBlock.initialize();
		EnchantmentRegister.Initialize();
		ModArmorMaterials.initialize();
		EventRegister.Initialize();
		PortalsRegister.Initialize();
		ScreenHandlerTypeInit.initialize();
		NetworkRegister.init();
		ScreenRegister.init();
		ServerEachTickTaskManager.init();
		ParticlesRegister.initialize();
		SoundRegister.initialize();

		InitValue.LOGGER.info("make in soybeani =v=!");
	}
}