package me.mubioh.bedrockcoordinates.client;

import me.mubioh.bedrockcoordinates.client.gui.CoordinatesDisplay;
import me.mubioh.bedrockcoordinates.config.CoordinateConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class BedrockCoordinatesClient implements ClientModInitializer {

    private static final String MOD_ID = "bedrockcoordinates";
    private static final Identifier HUD_ID = Identifier.of(MOD_ID, "display");

    private static KeyBinding toggleModeKey;

    @Override
    public void onInitializeClient() {
        CoordinateConfig.load();
        HudElementRegistry.addLast(HUD_ID, new CoordinatesDisplay());

        toggleModeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bedrockcoordinates.toggle_mode",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KeyBinding.Category.create(
                        Identifier.of(MOD_ID, "keybinds")
                )
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleModeKey.wasPressed()) {
                var mode = CoordinateConfig.instance().toggleDisplayMode();
                CoordinateConfig.save();

                if (client.getToastManager() != null) {

                    Text title = Text.translatable("bedrockcoordinates.toast.title");

                    Text description = Text.translatable(
                            "bedrockcoordinates.mode_switched",
                            Text.translatable(mode == CoordinateConfig.DisplayMode.BLOCK
                                    ? "bedrockcoordinates.mode.block"
                                    : "bedrockcoordinates.mode.xyz")
                    );

                    client.getToastManager().add(
                            new SystemToast(
                                    SystemToast.Type.PERIODIC_NOTIFICATION,
                                    title,
                                    description
                            )
                    );
                }
            }
        });
    }
}
