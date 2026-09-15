package de.cxlledjay.bokarts.util;

import de.cxlledjay.bokarts.BoKarts;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> DRIVABLE_BLOCKS = createTag("driveable_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, BoKarts.id(name));
        }
    }

}
