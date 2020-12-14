package towardsthestars.orefinder.item.prospect;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.TranslationTextComponent;
import towardsthestars.orefinder.OreFinder;
import towardsthestars.orefinder.item.prospect.result.*;
import towardsthestars.orefinder.util.Plain8Direction;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Prospectors
{
    public static AbstractProspector<CoarseProspectResult> COARSE4 = AbstractProspector.of(
            (world, posRange, origin)->
            {
                Optional<BlockPos> optional = posRange.filter(
                        pos -> OreFinder.shouldProspect(world.getBlockState(pos).getBlock())
                ).findFirst();
                if (optional.isPresent())
                {
                    BlockPos resultPos = optional.get();
                    Vec2f delta = new Vec2f(resultPos.getX() - origin.getX(), resultPos.getZ() - origin.getZ());
                    return new CoarseProspectResult(
                            Plain8Direction.fromVec2f4(delta),
                            world.getBlockState(resultPos).getBlock()
                    );
                }
                return new CoarseProspectResult();
            },
            new TranslationTextComponent("orefinder.prospector.coarse4")
    );

    public static AbstractProspector<CoarseProspectResult> COARSE8 = AbstractProspector.of(
            (world, posRange, origin) ->
            {
                // Use filter
                // Find first filtered element in posRange

                Optional<BlockPos> optional = posRange.filter(
                        pos -> world.isBlockPresent(pos) && OreFinder.shouldProspect(world.getBlockState(pos).getBlock())
                ).findFirst();

                // If found, assemble result
                if (optional.isPresent())
                {
                    BlockPos resultPos = optional.get();
                    Vec2f delta = new Vec2f(
                            resultPos.getX() - origin.getX(),
                            resultPos.getZ() - origin.getZ()
                    );
                    return new CoarseProspectResult(
                            Plain8Direction.fromVec2f8(delta),
                            world.getBlockState(resultPos).getBlock()
                    );
                }
                return new CoarseProspectResult();
            },
            new TranslationTextComponent("orefinder.prospector.coarse8")
    );

    public static AbstractProspector<CountingProspectResult> COUNT = AbstractProspector.of(
            (world, posRange, prospectOrigin) ->
            {
                Map<Block, Long> counter =
                        posRange
                                .filter(world::isBlockPresent)
                                .map(pos -> world.getBlockState(pos).getBlock())
                                .filter(OreFinder::shouldProspect)
                                .collect(Collectors.groupingBy(block -> block, Collectors.counting()));
                return new CountingProspectResult(counter);
            },
            new TranslationTextComponent("orefinder.prospector.count")
    );

    public static AbstractProspector<ExactPosProspectResult> EXACT = AbstractProspector.of(
            (world, posRange, prospectOrigin) ->
            {
                Optional<BlockPos> optional = posRange
                        .filter(pos -> OreFinder.shouldProspect(world.getBlockState(pos).getBlock()))
                        .findFirst();

                if (optional.isPresent())
                {
                    BlockPos pos = optional.get();
                    return new ExactPosProspectResult(
                            pos.subtract(prospectOrigin),
                            world.getBlockState(pos).getBlock()
                    );
                }
                return new ExactPosProspectResult();
            },
            new TranslationTextComponent("orefinder.prospector.exact")
    );


}
