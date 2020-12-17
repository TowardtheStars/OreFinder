package towardsthestars.orefinder.item.prospect.multitick;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import towardsthestars.orefinder.item.prospect.result.CoarseProspectResult;
import towardsthestars.orefinder.util.Plain8Direction;
import towardsthestars.orefinder.util.multitick.TickedStreamProvider;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class MultiTickCoarseProspecting
        extends MultiTickProspector<CompoundNBT, CoarseProspectResult>
{
    private final Function<Vec2f, Plain8Direction> toCoarse;
    public MultiTickCoarseProspecting(
            World world, BlockPos origin,
            TickedStreamProvider<BlockPos> stream,
            Function<Vec2f, Plain8Direction> toCoarse
            )
    {
        super(world, origin, stream, CoarseProspectResult::new);
        this.toCoarse = toCoarse;
    }

    @Override
    public CoarseProspectResult generateResult()
    {
        // Use filter
        // Find first filtered element in posRange
        Stream<BlockPos> posRange = this.getTargetStream();
        Optional<BlockPos> optional = posRange
                .min(
                        Comparator.comparingDouble(pos -> pos.distanceSq(origin))
                );

        // If found, assemble result
        if (optional.isPresent())
        {
            BlockPos resultPos = optional.get();
            Vec2f delta = new Vec2f(
                    resultPos.getX() - origin.getX(),
                    resultPos.getZ() - origin.getZ()
            );
            return new CoarseProspectResult(
                    origin,
                    resultPos,
                    toCoarse.apply(delta),
                    world.getBlockState(resultPos).getBlock()
            );
        }
        return new CoarseProspectResult();
    }
}
