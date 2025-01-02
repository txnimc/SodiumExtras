package toni.sodiumextras.mixins.impl.sodium;

#if FORGE
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import org.embeddedt.embeddium.gui.EmbeddiumVideoOptionsScreen;
#elif AFTER_21_1
    import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
    import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
    #else
    import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
    import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
#endif

import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toni.sodiumextras.foundation.embeddium.pages.EntityCullingPage;
import toni.sodiumextras.foundation.embeddium.pages.OthersPage;
import toni.sodiumextras.foundation.embeddium.pages.TrueDarknessPage;

import java.util.List;

@Mixin(value = #if FORGE EmbeddiumVideoOptionsScreen.class #else SodiumOptionsGUI.class #endif, remap = false, priority = 100/* Prevents other forks of sodium extra stay above emb++*/)
public class EmbOptionsMixin {
    @Shadow @Final private List<OptionPage> pages;
//
//    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
//    private void redirect$qualityPage(Screen prevScreen, CallbackInfo ci) {
//        pages.add(new QualityPlusPage());
//    }
    #if FORGE #else
    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$dynLightsPage(Screen prevScreen, CallbackInfo ci) {
        //pages.add(new QualityPlusPage());
        #if mc < 214 pages.add(new TrueDarknessPage()); #endif
        pages.add(new EntityCullingPage());
        pages.add(new OthersPage());
    }
    #endif
}