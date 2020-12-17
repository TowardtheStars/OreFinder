package towardsthestars.orefinder.util.blockpos;

import lombok.RequiredArgsConstructor;
import net.minecraft.util.math.BlockPos;
import towardsthestars.orefinder.util.multitick.TickedStreamProvider;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class BlockPosStreamProvider implements TickedStreamProvider<BlockPos>
{
    private final BlockPos start, end;
    private final long divide;
    private final DivideAxis divideAxis;

    public enum DivideAxis
    {
        X(){
            @Nonnull
            @Override
            public Stream<BlockPos> slice(BlockPos start, BlockPos end, long sliceNo, long divide)
            {
                BlockPos sliceStart = new BlockPos(
                        DivideAxis.lerpInt(sliceNo, divide, start.getX(), end.getX()),
                        start.getY(),
                        start.getZ()
                );
                BlockPos sliceEnd = new BlockPos(
                        DivideAxis.lerpInt(sliceNo + 1, divide, start.getX(), end.getX()),
                        end.getY(),
                        end.getZ()
                );
                return BlockPosHelper.getAllInBox(sliceStart, sliceEnd);
            }
        },
        Y(){
            @Nonnull
            @Override
            public Stream<BlockPos> slice(BlockPos start, BlockPos end, long sliceNo, long divide)
            {
                BlockPos sliceStart = new BlockPos(
                        start.getX(),
                        DivideAxis.lerpInt(sliceNo, divide, start.getY(), end.getY()),
                        start.getZ()
                );
                BlockPos sliceEnd = new BlockPos(
                        end.getX(),
                        DivideAxis.lerpInt(sliceNo + 1, divide, start.getY(), end.getY()),
                        end.getZ()
                );
                return BlockPosHelper.getAllInBox(sliceStart, sliceEnd);
            }
        },
        Z(){
            @Nonnull
            @Override
            public Stream<BlockPos> slice(BlockPos start, BlockPos end, long sliceNo, long divide)
            {
                BlockPos sliceStart = new BlockPos(
                        start.getX(),
                        start.getY(),
                        DivideAxis.lerpInt(sliceNo, divide, start.getZ(), end.getZ())
                );
                BlockPos sliceEnd = new BlockPos(
                        end.getX(),
                        end.getY(),
                        DivideAxis.lerpInt(sliceNo + 1, divide, start.getZ(), end.getZ())
                );
                return BlockPosHelper.getAllInBox(sliceStart, sliceEnd);
            }
        };

        @Nonnull
        public Stream<BlockPos> slice(BlockPos start, BlockPos end, long sliceNo, long divide)
        {
            return BlockPosHelper.getAllInBox(start, end);
        }

        private static int lerpInt(long sliceNo, long slice, int start, int end)
        {
            return (int)((end - start) * sliceNo / slice) + start;
        }
    }

    @Override
    public Stream<BlockPos> provide(long tick)
    {
        return this.divideAxis.slice(start, end, tick, divide);
    }

    public static BlockPosStreamProvider fromOrigin(BlockPos origin, int radius, DivideAxis divideAxis, long divide)
    {
        BlockPos start = origin.add(-radius, -radius, -radius);
        if (start.getY() < 0)
        {
            start = new BlockPos(start.getX(), 0, start.getZ());
        }
        BlockPos end = origin.add(radius, radius, radius);

        return new BlockPosStreamProvider(start, end, divide, divideAxis);
    }

    public static BlockPosStreamProvider fromOriginAllHeight(BlockPos origin, int radius, DivideAxis divideAxis, long divide)
    {
        BlockPos start = origin.add(-radius, 0, -radius);
        BlockPos end = origin.add(radius, 255, radius);
        return new BlockPosStreamProvider(start, end, divide, divideAxis);
    }
}
