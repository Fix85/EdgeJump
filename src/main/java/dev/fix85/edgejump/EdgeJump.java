package dev.fix85.edgejump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class EdgeJump implements ClientModInitializer {
    private static KeyBinding toggleKey;
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of("edgejump", "general"));

    @Override
    public void onInitializeClient() {
        Config.load();

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.edgejump.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                Config config = Config.get();
                config.enabled = !config.enabled;
                Config.save();
                
                if (client.player != null) {
                    if (config.enabled) {
                        client.player.sendMessage(Text.translatable("message.edgejump.enabled"), true);
                    } else {
                        client.player.sendMessage(Text.translatable("message.edgejump.disabled"), true);
                    }
                }
            }
        });
    }
}
