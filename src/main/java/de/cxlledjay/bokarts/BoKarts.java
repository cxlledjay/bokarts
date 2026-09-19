package de.cxlledjay.bokarts;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.item.ModItemGroups;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.networking.ModPackets;
import de.cxlledjay.bokarts.recipes.ModRecipes;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import de.cxlledjay.bokarts.sound.ModSounds;
import de.cxlledjay.bokarts.stats.ModStats;
import de.cxlledjay.bokarts.util.KartFuelItems;
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
		ModItemGroups.registerItemGroups();
		ModEntities.registerModEntities();
		ModSounds.registerSounds();
		ModPackets.registerC2SPackets();
		ModScreenHandlers.register();
		ModStats.register();
		ModDataComponentTypes.register();
		ModRecipes.register();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
