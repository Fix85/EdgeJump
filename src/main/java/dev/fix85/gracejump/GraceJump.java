package dev.fix85.gracejump;

import net.fabricmc.api.ClientModInitializer;

public class GraceJump implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Config.load();
    }
}
