package toni.sodiumextras.mixins.impl.darkness;

import toni.sodiumextras.EmbyConfig;
import toni.sodiumextras.foundation.darkness.DarknessPlus;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.class)
public class DimensionEffectsMixin {

    @Mixin(DimensionSpecialEffects.NetherEffects.class)
    public static class NetherMixin {
        #if mc < 214
        @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
        private void inject$brightFogColor(CallbackInfoReturnable<Vec3> cir) {
            if (EmbyConfig.darknessMode.get() == EmbyConfig.DarknessMode.OFF) return;
            if (!EmbyConfig.darknessOnNetherCache) return;

            cir.setReturnValue(DarknessPlus.getDarkFogColor(
                    cir.getReturnValue(),
                    EmbyConfig.darknessNetherFogBrightCache)
            );
        }
        #endif
    }

    @Mixin(DimensionSpecialEffects.EndEffects.class)
    public static class EndMixin {
        #if mc < 214
        @Inject(method = "getBrightnessDependentFogColor", at = @At(value = "RETURN"), cancellable = true)
        private void inject$brightFogColor(CallbackInfoReturnable<Vec3> cir) {
            if (EmbyConfig.darknessMode.get() == EmbyConfig.DarknessMode.OFF) return;
            if (!EmbyConfig.darknessOnEndCache) return;

            cir.setReturnValue(DarknessPlus.getDarkFogColor(
                    cir.getReturnValue(),
                    EmbyConfig.darknessEndFogBrightCache)
            );
        }
        #endif
    }
}
