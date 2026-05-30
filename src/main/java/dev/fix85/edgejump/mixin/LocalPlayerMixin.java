package dev.fix85.edgejump.mixin;

import dev.fix85.edgejump.Config;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class LocalPlayerMixin {
    @Unique
    private boolean edgejump$isAboutToFall(ClientPlayerEntity player) {
        double vx = player.getVelocity().x;
        double vz = player.getVelocity().z;
        if (vx == 0 && vz == 0) {
            return false;
        }

        double checkDistance = Config.get().graceTicks * 0.08;
        return player.doesNotCollide(Math.signum(vx) * checkDistance, -0.5, Math.signum(vz) * checkDistance);
    }


    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void edgejump$handleAutoEdgeJump(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        Config config = Config.get();
        if (!config.enabled) {
            return;
        }

        if (player.isOnGround()) {
            if (!player.isSneaking() 
                    && !player.isGliding() 
                    && !player.hasVehicle() 
                    && !player.getAbilities().flying 
                    && !player.isTouchingWater() 
                    && !player.isInLava() 
                    && !player.isClimbing()) {
                if (edgejump$isAboutToFall(player)) {
                    ((LivingEntity) player).jump();
                }
            }
        }
    }
}
