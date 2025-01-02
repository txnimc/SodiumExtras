package toni.sodiumextras.mixins.impl.togglefog;

import com.mojang.blaze3d.shaders.FogShape;
#if mc>=214 import net.minecraft.client.renderer.FogParameters;#endif
import net.minecraft.core.Holder;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toni.sodiumextras.EmbyConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = FogRenderer.class, priority = 910)
public abstract class FogMixin {
    @Unique private static final float FOG_START = -8.0F;
    @Unique private static final float FOG_END = 1_000_000.0F;

    #if mc < 214
    @Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void inject$fogDistance(Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance, boolean shouldCreateFog, float partialTick, CallbackInfo ci, FogType fogType, Entity entity, FogRenderer.FogData fogData) {
        if (EmbyConfig.fogCache) return;

        fogData.start = FOG_START;
        fogData.end = FOG_END;
        fogData.shape = FogShape.SPHERE;
    }
    #endif
}