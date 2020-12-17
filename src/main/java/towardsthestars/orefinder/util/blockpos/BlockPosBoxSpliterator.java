package towardsthestars.orefinder.util.blockpos;

import net.minecraft.util.math.BlockPos;

import java.util.Spliterator;
import java.util.function.Consumer;

public class BlockPosBoxSpliterator implements Spliterator<BlockPos>
{
    // Iteration origin
    private BlockPos start;
    // limit
    private int limX, limY, limZ;
    // Core iterating variable
    private int x, y, z;

    public BlockPosBoxSpliterator(BlockPos start, BlockPos end)
    {
        this.start = new BlockPos(
                Math.min(start.getX(), end.getX()),
                Math.min(start.getY(), end.getY()),
                Math.min(start.getZ(), end.getZ())
        );
        this.limX = Math.abs(end.getX() - start.getX());
        this.limY = Math.abs(end.getY() - start.getY());
        this.limZ = Math.abs(end.getZ() - start.getZ());
        this.x = this.y = this.z = 0;
    }

    @Override
    public boolean tryAdvance(Consumer<? super BlockPos> action)
    {
        if(z <= limZ && y <= limY && x <= limX)
        {
            action.accept(start.add(x, y, z));
            ++y;
            if (y > limY)
            {
                y = 0;
                ++x;
                if (x > limX)
                {
                    x = 0;
                    ++z;
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public Spliterator<BlockPos> trySplit()
    {
        if (limZ == 0 && limX == 0) // Will not split Y
        {
            return null;
        }
        // Divide X
        int rightXStart = (limX + 1) / 2;
        if (x < rightXStart)
        {
            BlockPosBoxSpliterator ret = new BlockPosBoxSpliterator(
                    new BlockPos(start.getX() + rightXStart, start.getY(), start.getZ() + z),
                    new BlockPos(start.getX() + limX, start.getY() + limY, start.getZ() + limZ)
            );
            this.limX = rightXStart - 1;
            return ret;
        }
        else
        {
            BlockPosBoxSpliterator ret = new BlockPosBoxSpliterator(
                    new BlockPos(start.getX(), start.getY(), start.getZ() + z + 1),
                    new BlockPos(start.getX() + rightXStart - 1, start.getY() + limY, start.getZ() + limZ)
            );
            this.limX = this.limX - rightXStart;
            this.start = this.start.add(rightXStart, 0, 0);
            return ret;
        }
    }


    @Override
    public long estimateSize()
    {
        return (limX + 1) * (limY + 1) * (limZ + 1);
    }


    @Override
    public int characteristics()
    {
        return SIZED | SUBSIZED | NONNULL;
    }


    @Override
    public void forEachRemaining(Consumer<? super BlockPos> action)
    {
        for(; y <= limY; ++y)
            action.accept(start.add(x, y, z));
        for (; x <= limX; ++x)
            for(y = 0; y <= limY; ++y)
                action.accept(start.add(x, y, z));
        for (; z <= limZ; ++z)
            for (x = 0; x <= limX; ++x)
                for(y = 0; y <= limY; ++y)
                    action.accept(start.add(x, y, z));
    }


    @Override
    public long getExactSizeIfKnown()
    {
        return (limX + 1) * (limY + 1) * (limZ + 1 - z) - (limY + 1) * (limX + 1 - x);
    }
}
