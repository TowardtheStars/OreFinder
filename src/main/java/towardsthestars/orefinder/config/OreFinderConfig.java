package towardsthestars.orefinder.config;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class OreFinderConfig
{


    public static ForgeConfigSpec.IntValue SEA_LEVEL;
    public static ForgeConfigSpec.IntValue MAX_HEIGHT;
    public static ForgeConfigSpec.IntValue MAX_USE_DURATION;


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
        MAX_USE_DURATION = builder
                .comment("How many tick should players use prospecting pickaxe before giving results.")
                .defineInRange("MaxUseDuration", 200, 10, 72000);
        builder.pop();

        Radius.register(builder);
        Amount.register(builder);

        ForgeConfigSpec COMMON_CONFIG = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }

    public static class Radius
    {
        public static ForgeConfigSpec.IntValue
                WOODEN, STONE, IRON, GOLDEN, DIAMOND;

        private static ForgeConfigSpec.IntValue buildCfg(ForgeConfigSpec.Builder builder, String name, int defaultValue)
        {
            return builder
                    .comment(String.format("Radius for %s ore finder", name))
                    .defineInRange(name, defaultValue, 0, Integer.MAX_VALUE);
        }

        static void register(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Ranges for different ore finders").push("radius");

            WOODEN = buildCfg(builder, "wooden", 8);    // Count
            STONE = buildCfg(builder, "stone", 100);    // Direction, 4
            IRON = buildCfg(builder, "iron", 60);       // Direction, 8
            GOLDEN = buildCfg(builder, "golden", 7);    // Exact point, 1 shot
            DIAMOND = buildCfg(builder, "diamond", 16); // Count

            builder.pop();
        }
    }

    public static class Amount
    {
        private static final int LEVEL_COUNT = 6;
        private static final long[] DEFAULT_LEVEL = {
                4, 16, 16, 32, 64, 128
        };
        private static final ForgeConfigSpec.LongValue[] LEVELS = new ForgeConfigSpec.LongValue[LEVEL_COUNT];

        static void register(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Leveling ore amount").push("amount");
            for (int i = 0; i < LEVEL_COUNT; i++)
            {
                LEVELS[i] = builder
                        .comment(
                                "How many ore blocks to reach this level.",
                                "Please make sure that this increases along with level.",
                                "If the amount of a level was less than or equal with that of its prior level,",
                                "    the prior level would be ignored."
                        )
                        .defineInRange(
                                String.format("level_%d", i + 1),
                                DEFAULT_LEVEL[i],
                                1, Long.MAX_VALUE
                        );
            }

            builder.pop();
        }

        public static ITextComponent getLevelTranslationTextComponent(long count)
        {
            for (int i = 0; i < LEVEL_COUNT; i++)
            {
                if (count < LEVELS[i].get())
                    return new TranslationTextComponent(String.format("orefinder.report.level_%d", i));
            }
            return new TranslationTextComponent(String.format("orefinder.report.level_%d", LEVEL_COUNT));
        }
    }
}
