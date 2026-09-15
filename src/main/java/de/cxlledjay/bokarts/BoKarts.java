package de.cxlledjay.bokarts;

import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoKarts implements ModInitializer {
	public static final String MOD_ID = "bokarts";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModEntities.registerModEntities();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
