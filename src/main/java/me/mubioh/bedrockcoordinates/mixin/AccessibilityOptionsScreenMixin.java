package me.mubioh.bedrockcoordinates.mixin;

import me.mubioh.bedrockcoordinates.config.CoordinateConfig;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {

    @Inject(method = "getOptions", at = @At("RETURN"), cancellable = true)
    private static void injectCoordinatesToggle(GameOptions gameOptions, CallbackInfoReturnable<SimpleOption<?>[]> cir) {
        final SimpleOption<?>[] original = cir.getReturnValue();

        final SimpleOption<Boolean> toggle = new SimpleOption<>(
                "options.bedrockcoordinates.show_coordinates",
                SimpleOption.emptyTooltip(),
                (optionText, value) -> Text.translatable(value ? "options.on" : "options.off"),
                SimpleOption.BOOLEAN,
                CoordinateConfig.instance().showCoordinates(),
                value -> {
                    CoordinateConfig.instance().setShowCoordinates(value);
                    CoordinateConfig.save();
                }
        );

        final SimpleOption<?>[] withToggle = Arrays.copyOf(original, original.length + 1);
        withToggle[original.length] = toggle;
        cir.setReturnValue(withToggle);
    }
}
