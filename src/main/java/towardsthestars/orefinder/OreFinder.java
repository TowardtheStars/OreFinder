package towardsthestars.orefinder;

import net.minecraft.block.Block;
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
import towardsthestars.orefinder.item.prospect.Prospectors;
import towardsthestars.orefinder.item.prospect.result.CoarseProspectResult;
import towardsthestars.orefinder.item.prospect.result.CountingProspectResult;
import towardsthestars.orefinder.item.prospect.result.ExactPosProspectResult;
import towardsthestars.orefinder.item.prospect.result.ProspectResult;

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
                            .setProspector(Prospectors.COUNT)
    );

    public static final RegistryObject<Item> STONE_FINDER = ITEM.register("stone_propick",
            () ->
                    new ProspectorItem(
                            ItemTier.STONE,
                            new Item.Properties()
                                    .rarity(Rarity.COMMON)
                    )
                            .setFindingRange(OreFinderConfig.Radius.STONE.get())
                            .setProspector(Prospectors.COARSE4)
    );

    public static final RegistryObject<Item> IRON_FINDER = ITEM.register("iron_propick",
            ()->
                    new ProspectorItem(
                            ItemTier.IRON,
                            new Item.Properties()
                                    .rarity(Rarity.UNCOMMON)
                    )
                            .setFindingRange(OreFinderConfig.Radius.IRON.get())
                            .setProspector(Prospectors.COARSE8)
    );

    public static final RegistryObject<Item> GOLD_FINDER = ITEM.register("golden_propick",
            () ->
                    new ProspectorItem(
                            ItemTier.GOLD,
                            new Item.Properties()
                                    .rarity(Rarity.RARE)
                                    .maxDamage(1)
                    )
                            .setFindingRange(OreFinderConfig.Radius.GOLDEN.get())
                            .setProspector(Prospectors.EXACT)

    );

    public static final RegistryObject<Item> DIAMOND_FINDER = ITEM.register("diamond_propick",
            () ->
                    new ProspectorItem(
                            ItemTier.DIAMOND,
                            new Item.Properties()
                                    .rarity(Rarity.RARE)
                    )
                            .setFindingRange(OreFinderConfig.Radius.DIAMOND.get())
                            .setProspector(Prospectors.COUNT)
    );


    public static final Tag<Block> PROSPECT_BLOCKS = new BlockTags.Wrapper(new ResourceLocation(
            MOD_ID, "prospect_blocks"
    ));



    public OreFinder()
    {
        OreFinderConfig.register();
        ProspectResult.register(CoarseProspectResult::new);
        ProspectResult.register(CountingProspectResult::new);
        ProspectResult.register(ExactPosProspectResult::new);
        ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static boolean shouldProspect(Block block)
    {
        return PROSPECT_BLOCKS.contains(block);
    }
}
