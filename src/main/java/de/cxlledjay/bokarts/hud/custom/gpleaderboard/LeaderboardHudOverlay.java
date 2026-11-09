package de.cxlledjay.bokarts.hud.custom.gpleaderboard;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.config.BoKartsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LeaderboardHudOverlay {


    public static final LeaderboardData LEADERBOARD_DATA = new LeaderboardData();

    private static final int TOTAL_WIDTH = 140;
    private static final int TOTAL_HEIGHT_TOP = 84;

    private static final int LINE_SPACING = 10;
    private static final int PADDING = 3;






    // callback registered in the overlay rendering
    public static void render(DrawContext context, RenderTickCounter tickCounter) {

        // do nothing if no leaderboard is active
        if(!BoKartsConfig.enableLeaderboard) return;

        if(!LEADERBOARD_DATA.isActive()) return;
        if(LEADERBOARD_DATA.myStats == null) return;

        // safety
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || client.currentScreen != null) return;
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        // data from config
        float scale =  BoKartsConfig.leaderboardScale;

        // screen data
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        int currentY = PADDING;

        int playerStatsSize = Math.min(
                screenHeight / 2 ,
                (LEADERBOARD_DATA.playerStats == null) ? (0) : LEADERBOARD_DATA.playerStats.size() * LINE_SPACING + LINE_SPACING
        );
        int leaderboardTotalHeight = TOTAL_HEIGHT_TOP + PADDING + playerStatsSize;

        float actualRenderedWidth = TOTAL_WIDTH * scale;
        float actualRenderedHeight = leaderboardTotalHeight * scale;

        // colors
        int underscoreColor = 0x5FFFFFFF;






        // push to new layer for scaling
        context.getMatrices().push();
        context.getMatrices().translate(
                (BoKartsConfig.displayOnLeft) ? (PADDING) : (screenWidth - (actualRenderedWidth + PADDING)),
                (((float) screenHeight / 2) - (actualRenderedHeight / 2)),
                0);
        context.getMatrices().scale(scale, scale, 1);


        // -------------------- personal stats --------------------

        // background
        context.fill(0, 0, TOTAL_WIDTH, TOTAL_HEIGHT_TOP, 0x60000000);

        // name
        int centeredNameX = (TOTAL_WIDTH / 2) - (client.textRenderer.getWidth(LEADERBOARD_DATA.myStats.name()) / 2);
        context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.myStats.name(), centeredNameX, currentY, 0xFFFFFF);
        currentY += 11;

        // underscore
        context.fill(0, currentY, TOTAL_WIDTH, currentY + 1, underscoreColor);
        currentY += 5;

        // fastest lap
        if(LEADERBOARD_DATA.fastestLap != null) {
            int rightAlignedFastestX = TOTAL_WIDTH - PADDING - (client.textRenderer.getWidth(LEADERBOARD_DATA.fastestLap.time()));
            context.drawTextWithShadow(client.textRenderer, Text.literal("Fastest lap: ").formatted(Formatting.WHITE), PADDING, currentY, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.fastestLap.time(), rightAlignedFastestX, currentY, 0xFFFFFF);
            currentY += LINE_SPACING;
            int rightAlignedFastestPlayerX = TOTAL_WIDTH - PADDING - (client.textRenderer.getWidth(LEADERBOARD_DATA.fastestLap.name()));
            context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.fastestLap.name(), rightAlignedFastestPlayerX, currentY, 0xFFFFFF);
        } else {
            context.drawTextWithShadow(client.textRenderer, Text.literal("Fastest lap: ").formatted(Formatting.WHITE), PADDING, currentY, 0xFFFFFF);
            currentY += LINE_SPACING;
        }
        currentY += 11;

        // underscore
        context.fill(0, currentY, TOTAL_WIDTH, currentY + 1, underscoreColor);
        currentY += 5;


        // lap times
        int rightAlignedBestX = TOTAL_WIDTH - PADDING - (client.textRenderer.getWidth(LEADERBOARD_DATA.myStats.best()));
        context.drawTextWithShadow(client.textRenderer, Text.literal("Personal best: ").formatted(Formatting.WHITE), PADDING, currentY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.myStats.best(), rightAlignedBestX, currentY, 0xFFFFFF);
        currentY += LINE_SPACING;
        int rightAlignedLastX = TOTAL_WIDTH - PADDING - (client.textRenderer.getWidth(LEADERBOARD_DATA.myStats.last()));
        context.drawTextWithShadow(client.textRenderer, Text.literal("Last: ").formatted(Formatting.WHITE), PADDING, currentY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.myStats.last(), rightAlignedLastX, currentY, 0xFFFFFF);
        currentY += 11;

        // underscore
        context.fill(0, currentY, TOTAL_WIDTH, currentY + 1, underscoreColor);
        currentY += 5;

        // laps
        int centeredLapsX = (TOTAL_WIDTH / 2) - (client.textRenderer.getWidth(LEADERBOARD_DATA.myStats.lapInfo()) / 2);
        context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.myStats.lapInfo(), centeredLapsX, currentY, 0xFFFFFF);
        currentY = (int) (currentY + LINE_SPACING * 1.5f);


        // -------------------- player stats rendering --------------------

        // safety when only rendering personal stats
        if(LEADERBOARD_DATA.playerStats == null) {
            context.getMatrices().pop();
            return;
        }

        // background
        context.fill(0, currentY, TOTAL_WIDTH - 19, currentY + playerStatsSize, 0x80000000);
        context.fill(TOTAL_WIDTH - 20, currentY, TOTAL_WIDTH, currentY + playerStatsSize, 0xA0000000);
        currentY += 6;

        // print player info
        for(int i = 0; i < LEADERBOARD_DATA.playerStats.size(); i++) {

            // current player
            LeaderboardData.PlayerStats currentStat = LEADERBOARD_DATA.playerStats.get(i);

            // current place
            Text place = Text.literal(String.valueOf(i + 1)).formatted(Formatting.WHITE);
            context.drawTextWithShadow(client.textRenderer, place, PADDING, currentY, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, currentStat.name(), 15, currentY, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, currentStat.delta(), 60, currentY, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, currentStat.tires(), 125, currentY, 0xFFFFFF);

            // space text
            currentY += LINE_SPACING;
        }





        // pop layer
        context.getMatrices().pop();
    }
}
