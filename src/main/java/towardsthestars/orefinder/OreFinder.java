package towardsthestars.orefinder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import towardsthestars.orefinder.config.OreFinderConfig;
import towardsthestars.orefinder.item.ItemOreFinder;

@Mod("orefinder")
public class OreFinder
{
    public static final DeferredRegister<Item> ITEM = new DeferredRegister<>(ForgeRegistries.ITEMS, "orefinder");

    public static final RegistryObject<Item> WOOD_FINDER = ITEM.register("wood_finder",
            ()-> new ItemOreFinder(
                    ItemTier.WOOD,
                    new Item.Properties().rarity(Rarity.COMMON)
            )
    );

    public static final RegistryObject<Item> STONE_FINDER = ITEM.register("stone_finder",
            () -> new ItemOreFinder(
                    ItemTier.STONE,
                    new Item.Properties().rarity(Rarity.COMMON)
            )
    );

    public static final RegistryObject<Item> IRON_FINDER = ITEM.register("iron_finder",
            ()-> new ItemOreFinder(
                    ItemTier.IRON,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
            )
    );

    public static final RegistryObject<Item> GOLD_FINDER = ITEM.register("gold_finder",
            () -> new ItemOreFinder(
                    ItemTier.GOLD,
                    new Item.Properties().rarity(Rarity.RARE)
            )
    );

    public static final RegistryObject<Item> DIAMOND_FINDER = ITEM.register("diamond_finder",
            () -> new ItemOreFinder(
                    ItemTier.DIAMOND,
                    new Item.Properties().rarity(Rarity.RARE)
            )
    );


    public OreFinder()
    {
        OreFinderConfig.register();
        ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());

    }
}
