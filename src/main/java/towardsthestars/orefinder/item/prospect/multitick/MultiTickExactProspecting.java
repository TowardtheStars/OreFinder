package towardsthestars.orefinder.item.prospect.multitick;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import towardsthestars.orefinder.item.prospect.result.ExactPosProspectResult;
import towardsthestars.orefinder.util.multitick.TickedStreamProvider;

import java.util.Comparator;
import java.util.Optional;

public class MultiTickExactProspecting
        extends MultiTickProspector<CompoundNBT, ExactPosProspectResult>
{
    private Logger LOGGER = LogManager.getLogger();

    public MultiTickExactProspecting(
            World world,
            BlockPos origin,
            TickedStreamProvider<BlockPos> stream
    )
    {
        super(world, origin, stream, ExactPosProspectResult::new);
    }

    @Override
    public ExactPosProspectResult generateResult()
    {
        Optional<BlockPos> validPos = this.getTargetStream()
                .min(Comparator.comparingDouble(pos -> pos.distanceSq(origin)))
                ;
        if (validPos.isPresent())
        {
            BlockPos pos = validPos.get();
            Block block = world.getBlockState(pos).getBlock();
            LOGGER.debug(MarkerManager.getMarker("generateResult"),
                    String.format("Found ore %s at %s", block, pos)
            );
            LOGGER.debug(MarkerManager.getMarker("generateResult"),
                    String.format("Origin %s", origin)
            );
            return new ExactPosProspectResult(
                    pos.subtract(origin),
                    block
            );
        }
        LOGGER.debug(MarkerManager.getMarker("generateResult"), "Not Found in this iteration");
        return new ExactPosProspectResult();
    }
}
