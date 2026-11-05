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

    private static final int TOTAL_WIDTH = 120;
    private static final int TOTAL_HEIGHT_TOP = 58;

    private static final int LINE_SPACING = 10;
    private static final int PADDING = 3;






    // callback registered in the overlay rendering
    public static void render(DrawContext context, RenderTickCounter tickCounter) {

        // do nothing if no leaderboard is active
        if(!BoKartsConfig.enableKartHud) return;

        if(!LEADERBOARD_DATA.isActive()) return;
        if(LEADERBOARD_DATA.myStats == null || LEADERBOARD_DATA.playerStats == null) return;

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

        // colors
        int underscoreColor = 0x5FFFFFFF;



        // push to new layer for scaling
        context.getMatrices().push();
        context.getMatrices().translate(PADDING, (float) screenHeight / 3, 0);
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

        // lap times
        context.drawTextWithShadow(client.textRenderer, Text.literal("best: ").formatted(Formatting.WHITE), LINE_SPACING, currentY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.myStats.best(), TOTAL_WIDTH / 2, currentY, 0xFFFFFF);
        currentY += LINE_SPACING;
        context.drawTextWithShadow(client.textRenderer, Text.literal("last: ").formatted(Formatting.WHITE), LINE_SPACING, currentY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.myStats.last(), TOTAL_WIDTH / 2, currentY, 0xFFFFFF);
        currentY += 11;

        // underscore
        context.fill(0, currentY, TOTAL_WIDTH, currentY + 1, underscoreColor);
        currentY += 5;

        // laps
        int centeredLapsX = (TOTAL_WIDTH / 2) - (client.textRenderer.getWidth(LEADERBOARD_DATA.myStats.lapInfo()) / 2);
        context.drawTextWithShadow(client.textRenderer, LEADERBOARD_DATA.myStats.lapInfo(), centeredLapsX, currentY, 0xFFFFFF);
        currentY = (int) (currentY + LINE_SPACING * 1.5f);


        // -------------------- player stats rendering --------------------

        int playerStatsSize = Math.min(screenHeight / 2 , LEADERBOARD_DATA.playerStats.size() * LINE_SPACING + LINE_SPACING);

        // background
        context.fill(0, currentY, TOTAL_WIDTH, currentY + playerStatsSize, 0x80000000);
        currentY += 6;

        // print player info
        for(int i = 0; i < LEADERBOARD_DATA.playerStats.size(); i++) {

            // current player
            LeaderboardData.PlayerStats currentStat = LEADERBOARD_DATA.playerStats.get(i);

            // current place
            Text place = Text.literal(String.valueOf(i + 1)).formatted(Formatting.WHITE);
            context.drawTextWithShadow(client.textRenderer, place, PADDING, currentY, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, currentStat.name(), TOTAL_WIDTH / 8, currentY, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, currentStat.delta(), TOTAL_WIDTH / 2, currentY, 0xFFFFFF);
            context.drawTextWithShadow(client.textRenderer, currentStat.tires(), TOTAL_WIDTH * 8 / 9, currentY, 0xFFFFFF);

            // space text
            currentY += LINE_SPACING;
        }





        // pop layer
        context.getMatrices().pop();
    }
}
