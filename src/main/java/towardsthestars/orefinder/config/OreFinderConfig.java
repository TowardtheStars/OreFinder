package towardsthestars.orefinder.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class OreFinderConfig
{
    public static ForgeConfigSpec COMMON_CONFIG;


    public static ForgeConfigSpec.IntValue SEA_LEVEL;
    public static ForgeConfigSpec.IntValue MAX_HEIGHT;

    public static void register()
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("General settings").push("general");
        SEA_LEVEL = builder
                .comment("Above such height, ore finder will tell all veins in range")
                .defineInRange("SeaLevel", 63, 0, 255);
        MAX_HEIGHT = builder
                .comment("Ore finder will only search below and at this height when in chunk mode.")
                .defineInRange("MaxHeight", 255, 0, 255);
        builder.pop();
        COMMON_CONFIG = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }
}
