package towardsthestars.orefinder.item.prospect.multitick;

import net.minecraft.nbt.INBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import towardsthestars.orefinder.OreFinder;
import towardsthestars.orefinder.item.prospect.result.ProspectResult;
import towardsthestars.orefinder.util.multitick.MultiTickTraverseStream;
import towardsthestars.orefinder.util.multitick.TickedStreamProvider;

import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class MultiTickProspector<TAG extends INBT, RET extends ProspectResult<TAG>>
        extends MultiTickTraverseStream<BlockPos, TAG, RET>
{
    protected final World world;
    protected final BlockPos origin;
    public MultiTickProspector(World world, BlockPos origin, TickedStreamProvider<BlockPos> stream, Supplier<RET> defaultResultSupplier)
    {
        super(stream, defaultResultSupplier);
        this.world = world;
        this.origin = origin;
    }

    @Override
    public Stream<BlockPos> getTargetStream()
    {
        return super.getTargetStream().filter(
                pos -> world.isBlockPresent(pos) && OreFinder.shouldProspect(world.getBlockState(pos).getBlock())
        );

    }
}
