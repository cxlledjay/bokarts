package de.cxlledjay.bokarts.util;

import de.cxlledjay.bokarts.BoKarts;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> DRIVABLE_BLOCKS = createTag("driveable_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, BoKarts.id(name));
        }
    }


    public static class Items {
        public static final TagKey<Item> KARTS = createTag("karts");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, BoKarts.id(name));
        }
    }

}
