package milkwater.cobbleultraspace;

import milkwater.cobbleultraspace.features.BeastiteSpikeFeature;
import milkwater.cobbleultraspace.features.FlexTreeFeature;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static milkwater.cobbleultraspace.TeleportHandler.teleportToUltraSpace;

public class CobbleUltraSpace implements ModInitializer {
	public static final String MOD_ID = "cobbleultraspace";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // register the dimension
    public static final RegistryKey<World> ULTRA_SPACE_KEY =
            RegistryKey.of(RegistryKeys.WORLD, Identifier.of(MOD_ID, "ultra_space"));

    // register my features
    public static final Feature<DefaultFeatureConfig> FLEX_TREE_FEATURE =
            Registry.register(Registries.FEATURE,
                    Identifier.of(MOD_ID, "flex_tree"),
                    new FlexTreeFeature(DefaultFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> BEASTITE_SPIKE_BIG_FEATURE =
            Registry.register(Registries.FEATURE,
                    Identifier.of(MOD_ID, "beastite_spike_big"),
                    new BeastiteSpikeFeature(DefaultFeatureConfig.CODEC));

    @Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

        // simple teleport command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("goto_ultraspace")
                    .executes(context -> teleportToUltraSpace(context)));
        });
	}
}