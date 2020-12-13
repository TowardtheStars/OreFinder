package towardsthestars.orefinder.item.prospect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import towardsthestars.orefinder.util.IRandomStartIterator;


@FunctionalInterface
public interface IProspectPosRangeProvider
{
    IRandomStartIterator<BlockPos> provide(BlockPos origin, int radius, Vec2f facing);
}
