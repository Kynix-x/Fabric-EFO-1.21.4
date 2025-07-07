package net.kynix.echoesfromorion;

import net.fabricmc.api.ModInitializer;

import net.kynix.echoesfromorion.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoesFromOrion implements ModInitializer {
	public static final String MOD_ID = "echoesfromorion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModItems.registerModItems();
	}
}