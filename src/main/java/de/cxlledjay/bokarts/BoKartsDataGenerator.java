package de.cxlledjay.bokarts;

import de.cxlledjay.bokarts.datagen.ModBlockTagProvider;
import de.cxlledjay.bokarts.datagen.ModLootTableProvider;
import de.cxlledjay.bokarts.datagen.ModModelProvider;
import de.cxlledjay.bokarts.datagen.ModRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BoKartsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
	}
}
