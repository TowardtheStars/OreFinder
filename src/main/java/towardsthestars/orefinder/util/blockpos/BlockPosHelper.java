package towardsthestars.orefinder.util.blockpos;

import net.minecraft.util.math.BlockPos;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BlockPosHelper
{
    public static Stream<BlockPos> getAllInBox(BlockPos start, BlockPos end)
    {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new BlockPosBoxIterator(start, end), Spliterator.ORDERED
                ),
                false
        );
    }

    public static Stream<BlockPos> getAllInBox(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        return getAllInBox(new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
    }

}
