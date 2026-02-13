package me.mubioh.bedrockcoordinates.client.gui;

import me.mubioh.bedrockcoordinates.config.CoordinateConfig;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Locale;

public class CoordinatesDisplay implements HudElement {

    private static final int TEXT_PADDING_X = 5;
    private static final int TEXT_PADDING_Y = 3;
    private static final int BACKGROUND_COLOR = 0xCC000000;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private static final int XYZ_DECIMALS = 3;

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        final MinecraftClient client = MinecraftClient.getInstance();
        final PlayerEntity player = client.player;

        if (player == null) return;
        if (client.options.hudHidden) return;
        if (client.getDebugHud().shouldShowDebugHud()) return;

        final CoordinateConfig config = CoordinateConfig.instance();
        if (!config.showCoordinates()) return;

        final String xStr;
        final String yStr;
        final String zStr;

        if (config.displayMode() == CoordinateConfig.DisplayMode.BLOCK) {
            BlockPos pos = player.getBlockPos();
            xStr = Integer.toString(pos.getX());
            yStr = Integer.toString(pos.getY());
            zStr = Integer.toString(pos.getZ());
        } else {
            String fmt = "%." + XYZ_DECIMALS + "f";
            xStr = String.format(Locale.ROOT, fmt, player.getX());
            yStr = String.format(Locale.ROOT, fmt, player.getY());
            zStr = String.format(Locale.ROOT, fmt, player.getZ());
        }

        final Text coordinatesText = Text.translatable(
                "hud.coordinates.position",
                xStr, yStr, zStr
        );

        final int textWidth = client.textRenderer.getWidth(coordinatesText);
        final int textHeight = client.textRenderer.fontHeight;

        final int screenHeight = client.getWindow().getScaledHeight();
        final int xPos = 0;
        final int yPos = screenHeight / 10;

        final int bgLeft = xPos;
        final int bgTop = yPos - TEXT_PADDING_Y;
        final int bgRight = xPos + textWidth + (TEXT_PADDING_X * 2);
        final int bgBottom = yPos + textHeight + TEXT_PADDING_Y;

        context.fill(bgLeft, bgTop, bgRight, bgBottom, BACKGROUND_COLOR);
        context.drawTextWithShadow(client.textRenderer, coordinatesText, xPos + TEXT_PADDING_X, yPos, TEXT_COLOR);
    }
}