package de.cxlledjay.bokarts.hud.custom.gpleaderboard;

import de.cxlledjay.bokarts.block.entity.custom.TrackTimerBlockEntity;
import de.cxlledjay.bokarts.block.entity.custom.race.LeaderboardEntry;
import de.cxlledjay.bokarts.networking.packet.LeaderboardUpdateEntriesS2C;
import de.cxlledjay.bokarts.networking.packet.LeaderboardUpdatePersonalS2C;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardData {

    public PersonalStats myStats = null;
    public List<PlayerStats> playerStats = null;
    private boolean active = false;

    public boolean isActive() {
        return active;
    }

    public void reset() {
        this.myStats = null;
        this.playerStats = null;
        this.active = false;
    }

    public void update(LeaderboardUpdatePersonalS2C packet) {

        // formatting
        Formatting bestFormat = (packet.bestIsRecord() ? Formatting.LIGHT_PURPLE : Formatting.GREEN);
        Formatting lastFormat = (packet.last() == packet.best()) ?  bestFormat : Formatting.WHITE;

        // build texts
        Text name = Text.literal(packet.name()).formatted(Formatting.YELLOW).formatted(Formatting.BOLD);
        Text best = Text.literal(TrackTimerBlockEntity.getFormattedLapTime(packet.best())).formatted(bestFormat);
        Text last = Text.literal(TrackTimerBlockEntity.getFormattedLapTime(packet.last())).formatted(lastFormat);
        Text lapInfo = Text.literal("LAP " + packet.currentLap() + "/" + packet.totalLaps()).formatted(Formatting.WHITE).formatted(Formatting.BOLD);

        // save
        this.myStats = new PersonalStats(name, best, last, lapInfo);
        this.active = true;
    }

    public void update(LeaderboardUpdateEntriesS2C packet) {

        List<PlayerStats> updatedPlayerStats = new ArrayList<>();

        // loop trough each entry
        for(int i = 0; i < packet.entries().size(); ++i) {
            LeaderboardEntry e = packet.entries().get(i);

            Text name = Text.literal(e.displayName()).formatted(Formatting.WHITE);
            Text delta = Text.literal("+" + TrackTimerBlockEntity.getFormattedLapTime(e.delta())).formatted(Formatting.RED);

            // different deltas
            if (e.finished()) delta = Text.literal("Fin").formatted(Formatting.GRAY);
            else if (i == 0) delta = Text.literal("Leader").formatted(Formatting.WHITE);
            else if (e.lapped() && e.delta() == 1)  delta = Text.literal("+" + e.delta() + " LAP").formatted(Formatting.DARK_RED);
            else if (e.lapped() && e.delta() > 1)  delta = Text.literal("+" + e.delta() + " LAPS").formatted(Formatting.DARK_RED);

            // different tires
            Formatting tiresFormat = switch (e.tires()) {
                case "SS" -> Formatting.RED;
                case "S" -> Formatting.YELLOW;
                case "M" -> Formatting.WHITE;
                case "H" -> Formatting.BLUE;
                case "SH" -> Formatting.GOLD;
                default -> Formatting.GRAY;
            };
            Text tires = Text.literal(e.tires()).formatted(tiresFormat);

            // create new player stats
            updatedPlayerStats.add(new PlayerStats(name, delta, tires));
        }

        // save
        this.playerStats = updatedPlayerStats;
        this.active = true;
    }





    public record PersonalStats(
            Text name,
            Text best,
            Text last,
            Text lapInfo
    ) {}

    public record PlayerStats(
            Text name,
            Text delta,
            Text tires
    ) {}

}
