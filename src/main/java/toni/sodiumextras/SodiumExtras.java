package toni.sodiumextras;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import toni.sodiumextras.foundation.fps.DebugOverlayEvent;
import toni.sodiumextras.foundation.fps.FpsHistory;

#if FABRIC
    import net.fabricmc.api.ClientModInitializer;
    import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
    import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
    import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

    #if mc >= 215
    import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
    import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
    import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
    #elif mc >= 211
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
    #endif

    #if AFTER_21_1

    import net.neoforged.neoforge.client.gui.ConfigurationScreen;
    import net.neoforged.fml.config.ModConfig;
    import net.neoforged.neoforge.common.ModConfigSpec;
    import net.neoforged.neoforge.common.ModConfigSpec.*;
    #endif
    #if CURRENT_20_1
    import net.minecraftforge.fml.config.ModConfig;
    import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
    import net.minecraftforge.common.ForgeConfigSpec.*;
    import net.minecraftforge.common.ForgeConfigSpec;
    import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
    #endif
#endif

#if NEO
import net.neoforged.fml.config.ModConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
#endif

#if FORGE
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
#endif

#if FORGELIKE
@Mod(SodiumExtras.ID)
#endif
public class SodiumExtras #if FABRIC implements ClientModInitializer #endif {
    public static final String ID = "sodiumextras";
    public static final Logger LOGGER = LogManager.getLogger("SodiumExtras");

    public static FpsHistory fpsHistory = new FpsHistory();

    public SodiumExtras(#if NEO IEventBus modEventBus, ModContainer modContainer #endif) {
        #if NEO
        modContainer.registerConfig(ModConfig.Type.CLIENT, EmbyConfig.SPECS);
        #endif

        #if FORGE
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EmbyConfig.SPECS);
        #endif
    }

    #if FABRIC
    @Override
    public void onInitializeClient() {
        #if mc >= 211
        ClientLifecycleEvents.CLIENT_STOPPING.register((mc) -> {
            EmbyConfig.SPECS.save();
        });
        #endif

        #if mc >= 215
            ModConfigEvents.loading(SodiumExtras.ID).register((ModConfig config) -> EmbyConfig.updateCache());
            ConfigRegistry.INSTANCE.register(SodiumExtras.ID, ModConfig.Type.CLIENT, EmbyConfig.SPECS);
        #elif AFTER_21_1
            NeoForgeModConfigEvents.loading(SodiumExtras.ID).register((ModConfig config) -> {
                EmbyConfig.updateCache();
            });

            NeoForgeConfigRegistry.INSTANCE.register(SodiumExtras.ID, ModConfig.Type.CLIENT, EmbyConfig.SPECS);
        #else
            ModConfigEvents.loading(SodiumExtras.ID).register((ModConfig config) -> EmbyConfig.updateCache());
            ForgeConfigRegistry.INSTANCE.register(SodiumExtras.ID, net.minecraftforge.fml.config.ModConfig.Type.CLIENT, EmbyConfig.SPECS);
        #endif

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            DebugOverlayEvent.renderFPSChar(Minecraft.getInstance(), drawContext);
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            int currentFps = client.getFps();
            fpsHistory.add(currentFps);
        });
    }
    #endif
}