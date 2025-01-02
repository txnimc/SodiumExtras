package toni.sodiumextras.foundation.embeddium;


import toni.sodiumextras.EmbyConfig;
import toni.sodiumextras.EmbyConfig.FPSDisplayGravity;
import toni.sodiumextras.EmbyConfig.FPSDisplayMode;
import toni.sodiumextras.EmbyConfig.FPSDisplaySystemMode;
import toni.sodiumextras.EmbyConfig.FullScreenMode;
import net.minecraft.network.chat.Component;

import java.util.List;

#if AFTER_21_1
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.caffeinemc.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
#else
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
#endif

public class EmbPlusOptions {
    public static Option<FullScreenMode> getFullscreenOption(MinecraftOptionsStorage options) {
        return OptionImpl.createBuilder(FullScreenMode.class, options)
                .setName(Component.translatable("sodium.extras.options.screen.title"))
                .setTooltip(Component.translatable("sodium.extras.options.screen.desc"))
                .setControl((opt) -> new CyclingControl<>(opt, FullScreenMode.class, new Component[] {
                        Component.translatable("sodium.extras.options.screen.windowed"),
                        Component.translatable("sodium.extras.options.screen.borderless"),
                        Component.translatable("options.fullscreen")
                }))
                .setBinding(EmbyConfig::setFullScreenMode, (opts) -> EmbyConfig.fullScreen.get()).build();
    }


    public static void setFPSOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {
        var builder = OptionGroup.createBuilder();

        builder.add(OptionImpl.createBuilder(FPSDisplayMode.class, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.displayfps.title"))
                .setTooltip(Component.translatable("sodium.extras.options.displayfps.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplayMode.class, new Component[]{
                        Component.translatable("sodium.extras.options.common.off"),
                        Component.translatable("sodium.extras.options.common.simple"),
                        Component.translatable("sodium.extras.options.common.advanced")
                }))
                .setBinding(
                        (opts, value) -> {
                            EmbyConfig.fpsDisplayMode.set(value);
                            EmbyConfig.SPECS.save();
                        },
                        (opts) -> EmbyConfig.fpsDisplayMode.get())
                .setImpact(OptionImpact.LOW)
                .build()
        );

        builder.add(OptionImpl.createBuilder(FPSDisplaySystemMode.class, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.displayfps.system.title"))
                .setTooltip(Component.translatable("sodium.extras.options.displayfps.system.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplaySystemMode.class, new Component[]{
                        Component.translatable("sodium.extras.options.common.off"),
                        Component.translatable("sodium.extras.options.common.on"),
                        Component.translatable("sodium.extras.options.displayfps.system.gpu"),
                        Component.translatable("sodium.extras.options.displayfps.system.ram")
                }))
                .setBinding((options, value) -> {
                            EmbyConfig.fpsDisplaySystemMode.set(value);
                            EmbyConfig.SPECS.save();
                        },
                        (options) -> EmbyConfig.fpsDisplaySystemMode.get())
                .build()
        );

        var components = new Component[FPSDisplayGravity.values().length];
        for (int i = 0; i < components.length; i++) {
            components[i] = Component.translatable("sodium.extras.options.displayfps.gravity." + FPSDisplayGravity.values()[i].name().toLowerCase());
        }

        builder.add(OptionImpl.createBuilder(FPSDisplayGravity.class, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.displayfps.gravity.title"))
                .setTooltip(Component.translatable("sodium.extras.options.displayfps.gravity.desc"))
                .setControl((option) -> new CyclingControl<>(option, FPSDisplayGravity.class, components))
                .setBinding(
                        (opts, value) -> {
                            EmbyConfig.fpsDisplayGravity.set(value);
                            EmbyConfig.SPECS.save();
                        },
                        (opts) -> EmbyConfig.fpsDisplayGravity.get())
                .build()
        );


        builder.add(OptionImpl.createBuilder(Integer.TYPE, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.displayfps.margin.title"))
                .setTooltip(Component.translatable("sodium.extras.options.displayfps.margin.desc"))
                .setControl((option) -> new SliderControl(option, 4, 64, 1, (v) -> Component.literal(v + "px")))
                .setImpact(OptionImpact.LOW)
                .setBinding(
                        (opts, value) -> {
                            EmbyConfig.fpsDisplayMargin.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.fpsDisplayMarginCache = value;
                        },
                        (opts) -> EmbyConfig.fpsDisplayMarginCache)
                .build()
        );

        builder.add(OptionImpl.createBuilder(boolean.class, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.displayfps.shadow.title"))
                .setTooltip(Component.translatable("sodium.extras.options.displayfps.shadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.fpsDisplayShadow.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.fpsDisplayShadowCache = value;
                        },
                        (options) -> EmbyConfig.fpsDisplayShadowCache)
                .build()
        );

        groups.add(builder.build());
    }

    public static void setQualityPlusOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {
        final var fog = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.fog.title"))
                .setTooltip(Component.translatable("sodium.extras.options.fog.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((options, value) -> {
                            EmbyConfig.fog.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.fogCache = value;
                        },
                        (options) -> EmbyConfig.fogCache)
                .setImpact(OptionImpact.LOW)
                .build();

        final var fadeInQuality = OptionImpl.createBuilder(EmbyConfig.ChunkFadeSpeed.class, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.fadein.title"))
                .setTooltip(Component.translatable("sodium.extras.options.fadein.desc"))
                .setControl((option) -> new CyclingControl<>(option, EmbyConfig.ChunkFadeSpeed.class, new Component[]{
                        Component.translatable("options.off"),
                        Component.translatable("options.graphics.fast"),
                        Component.translatable("options.graphics.fancy")
                }))
                .setBinding((opts, value) -> {
                            EmbyConfig.chunkFadeSpeed.set(value);
                            EmbyConfig.SPECS.save();
                        },
                        (opts) -> EmbyConfig.chunkFadeSpeed.get())
                .setImpact(OptionImpact.LOW)
                .setEnabled(#if AFTER_21_1 () -> false #else false #endif)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(fog)
                .add(fadeInQuality)
                .build()
        );

        final var cloudHeight = OptionImpl.createBuilder(int.class, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.clouds.height.title"))
                .setTooltip(Component.translatable("sodium.extras.options.clouds.height.desc"))
                .setControl((option) -> new SliderControl(option, 64, 364, 4, ControlValueFormatter.biomeBlend()))
                .setBinding((options, value) -> {
                            EmbyConfig.cloudsHeight.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.cloudsHeightCache = value;
                        },
                        (options) -> EmbyConfig.cloudsHeightCache)
                .build();

        groups.add(OptionGroup.createBuilder()
                .add(cloudHeight)
                .build()
        );
    }

    public static void setPerformanceOptions(List<OptionGroup> groups, SodiumOptionsStorage sodiumOpts) {
        #if mc < 214
        var builder = OptionGroup.createBuilder();
        var fontShadow = OptionImpl.createBuilder(boolean.class, sodiumOpts)
                .setName(Component.translatable("sodium.extras.options.fontshadow.title"))
                .setTooltip(Component.translatable("sodium.extras.options.fontshadow.desc"))
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> {
                            EmbyConfig.fontShadows.set(value);
                            EmbyConfig.SPECS.save();
                            EmbyConfig.fontShadowsCache = value;
                        },
                        (options) -> EmbyConfig.fontShadowsCache)
                .setImpact(OptionImpact.VARIES)
                .build();
 

        builder.add(fontShadow); 

        groups.add(builder.build());
        #endif
    }


}
