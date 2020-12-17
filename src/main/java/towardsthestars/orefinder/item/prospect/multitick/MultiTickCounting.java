package towardsthestars.orefinder.item.prospect.multitick;

import net.minecraft.block.Block;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import towardsthestars.orefinder.item.prospect.result.CountingProspectResult;
import towardsthestars.orefinder.util.multitick.TickedStreamProvider;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MultiTickCounting extends MultiTickProspector<ListNBT, CountingProspectResult>
{
    public MultiTickCounting(World world, BlockPos origin, TickedStreamProvider<BlockPos> streamProvider)
    {
        super(world, origin, streamProvider, CountingProspectResult::new);
    }

    @Override
    public CountingProspectResult generateResult()
    {
        Stream<BlockPos> posRange = this.getTargetStream();
        Map<Block, Long> counter =
                posRange
                        .map(pos -> world.getBlockState(pos).getBlock())
                        .collect(Collectors.groupingBy(block -> block, Collectors.counting()));
        return new CountingProspectResult(counter);
    }
}
