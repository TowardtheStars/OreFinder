package towardsthestars.orefinder.util.blockpos;

import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class BlockPosBoxIterator implements Iterator<BlockPos>
{
    // Iteration origin
    private final BlockPos start;
    // limit
    private final int limX, limY, limZ;
    // Core iterating variable
    private int x, y, z;

    public BlockPosBoxIterator(BlockPos start, BlockPos end)
    {
        this.start = new BlockPos(
                Math.min(start.getX(), end.getX()),
                Math.min(start.getY(), end.getY()),
                Math.min(start.getZ(), end.getZ())
        );
        this.limX = Math.abs(end.getX() - start.getX());
        this.limY = Math.abs(end.getY() - start.getY());
        this.limZ = Math.abs(end.getZ() - start.getZ());
        System.out.println(this.limX);
        System.out.println(this.limY);
        System.out.println(this.limZ);
        this.x = this.y = this.z = 0;
    }


    @Override
    public boolean hasNext()
    {
        return z <= limZ;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public BlockPos next()
    {
        BlockPos result = start.add(x, y, z);
        System.out.println(result);
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
        return result;
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

    public long size()
    {
        return (limX + 1) * (limY + 1) * (limZ + 1);
    }


}
