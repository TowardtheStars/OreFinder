package towardsthestars.orefinder;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import towardsthestars.orefinder.config.OreFinderConfig;
import towardsthestars.orefinder.item.ProspectorItem;
import towardsthestars.orefinder.item.prospect.IProspectorTaskProvider;

@Mod("orefinder")
public class OreFinder
{
    public static final String MOD_ID = "orefinder";
    private static final DeferredRegister<Item> ITEM =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> WOOD_FINDER = ITEM.register("wooden_propick",
            () ->
                    new ProspectorItem(
                            ItemTier.WOOD,
                            new Item.Properties()
                                    .rarity(Rarity.COMMON)
                    )
                            .setFindingRange(OreFinderConfig.Radius.WOODEN.get())
                            .setProspectorProvider(IProspectorTaskProvider.COUNT)
    );

    public static final RegistryObject<Item> STONE_FINDER = ITEM.register("stone_propick",
            () ->
                    new ProspectorItem(
                            ItemTier.STONE,
                            new Item.Properties()
                                    .rarity(Rarity.COMMON)
                    )
                            .setFindingRange(OreFinderConfig.Radius.STONE.get())
                            .setProspectorProvider(IProspectorTaskProvider.COARSE4)
    );

    public static final RegistryObject<Item> IRON_FINDER = ITEM.register("iron_propick",
            ()->
                    new ProspectorItem(
                            ItemTier.IRON,
                            new Item.Properties()
                                    .rarity(Rarity.RARE)
                    )
                            .setFindingRange(OreFinderConfig.Radius.IRON.get())
                            .setProspectorProvider(IProspectorTaskProvider.COARSE8)
    );

    public static final RegistryObject<Item> GOLD_FINDER = ITEM.register("golden_propick",
            () ->
                    new ProspectorItem(
                            ItemTier.GOLD,
                            new Item.Properties()
                                    .rarity(Rarity.UNCOMMON)
                                    .maxDamage(1)
                    )
                            .setFindingRange(OreFinderConfig.Radius.GOLDEN.get())
                            .setProspectorProvider(IProspectorTaskProvider.EXACT)

    );

    public static final RegistryObject<Item> DIAMOND_FINDER = ITEM.register("diamond_propick",
            () ->
                    new ProspectorItem(
                            ItemTier.DIAMOND,
                            new Item.Properties()
                                    .rarity(Rarity.RARE)
                    )
                            .setFindingRange(OreFinderConfig.Radius.DIAMOND.get())
                            .setProspectorProvider(IProspectorTaskProvider.COUNT)
    );


    public static final Tag<Block> PROSPECT_BLOCKS = new BlockTags.Wrapper(new ResourceLocation(
            MOD_ID, "prospect_blocks"
    ));



    public OreFinder()
    {
        OreFinderConfig.register();
        ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static boolean shouldProspect(Block block)
    {
        return PROSPECT_BLOCKS.contains(block) && block != Blocks.AIR && block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR;
    }
}
