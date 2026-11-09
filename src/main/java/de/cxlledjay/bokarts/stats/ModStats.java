package de.cxlledjay.bokarts.stats;

import de.cxlledjay.bokarts.BoKarts;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class ModStats {

    public static final Identifier KART_ONE_CM = registerStat("kart_one_cm", StatFormatter.DISTANCE);
    public static final Identifier KART_TIME_DRIVEN = registerStat("kart_time_driven", StatFormatter.TIME);
    public static final Identifier KART_HORN_USED = registerStat("kart_horn_used", StatFormatter.DEFAULT);
    public static final Identifier KART_RACES_DRIVEN = registerStat("kart_races_driven", StatFormatter.DEFAULT);
    public static final Identifier KART_RACES_WON = registerStat("kart_races_won", StatFormatter.DEFAULT);

    private static Identifier registerStat(String name, StatFormatter statFormatter) {
        Identifier statId = BoKarts.id(name);
        Registry.register(Registries.CUSTOM_STAT, name, statId);
        Stats.CUSTOM.getOrCreateStat(statId, statFormatter);
        return statId;
    }


    public static void register() {
        BoKarts.LOGGER.info("Registering Statistics for " + BoKarts.MOD_ID);
    }

}
