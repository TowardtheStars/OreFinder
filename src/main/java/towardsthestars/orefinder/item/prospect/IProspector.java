package towardsthestars.orefinder.item.prospect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import towardsthestars.orefinder.item.prospect.result.ProspectResult;

import java.util.stream.Stream;

@FunctionalInterface
public interface IProspector<T extends ProspectResult>
{
    T prospect(World world, Stream<BlockPos> posRange, Vec3i prospectOrigin);

}
