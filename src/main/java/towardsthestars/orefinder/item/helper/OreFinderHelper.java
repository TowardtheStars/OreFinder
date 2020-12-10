package towardsthestars.orefinder.item.helper;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.ICommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Map;
import java.util.stream.Stream;

public class OreFinderHelper
{
    public static Map<Block, Integer> find(World worldIn, BlockPos start, BlockPos end)
    {
        Stream<BlockPos> range = BlockPos.getAllInBox(start, end);
        Map<Block, Integer> counter =
                Maps.newHashMap();
        range
                .map(pos -> worldIn.getBlockState(pos).getBlock())
                .filter(block -> block.getTags().stream().anyMatch(resourceLocation ->
                {
                    return resourceLocation.toString().split("/")[0].equals("forge:ores");
                }
                ))
                .forEach(block -> counter.compute(block, (block1, v) -> v == null? 1 : v + 1));

        return counter;
    }

    public static void report(ICommandSource source, Map<Block, Integer> result)
    {
        source.sendMessage(
                new StringTextComponent("Scan Result:")
                        .setStyle(new Style().setColor(TextFormatting.GREEN))
        );

        for (Map.Entry<Block, Integer> entry : result.entrySet())
        {
            source.sendMessage(new ItemStack(entry.getKey().asItem(), 1).getTextComponent()
                    .appendText(String.format(" x %d", entry.getValue())));
        }
    }
}
