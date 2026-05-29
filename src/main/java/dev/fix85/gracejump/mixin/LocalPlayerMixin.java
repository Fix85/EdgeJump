package dev.fix85.gracejump.mixin;

import dev.fix85.gracejump.Config;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class LocalPlayerMixin {
    @Shadow
    public abstract void jump();

    private int gracejump$coyoteTicks = 0;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void gracejump$handleCoyoteJump(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        Config config = Config.get();
        if (!config.enabled) {
            return;
        }

        if (player.isOnGround()) {
            gracejump$coyoteTicks = config.graceTicks;
        } else {
            if (gracejump$coyoteTicks > 0) {
                if (player.input.playerInput.jump() 
                        && !player.isGliding() 
                        && !player.hasVehicle() 
                        && !player.getAbilities().flying 
                        && !player.isTouchingWater() 
                        && !player.isInLava() 
                        && !player.isClimbing()) {
                    jump();
                    gracejump$coyoteTicks = 0;
                } else {
                    gracejump$coyoteTicks--;
                }
            }
        }
    }

    @Inject(method = "jump", at = @At("HEAD"))
    private void gracejump$onJump(CallbackInfo ci) {
        gracejump$coyoteTicks = 0;
    }
}
