package towardsthestars.orefinder.util;

import lombok.Getter;
import lombok.NonNull;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import towardsthestars.orefinder.config.OreFinderConfig;

import java.util.NoSuchElementException;

public class Chebyshev2DIterator implements IRandomStartIterator<BlockPos>
{
    private final BlockPos origin;
    private final int range;
    private final int minY;
    private final int maxY;

    private BlockPos delta;
    private int step = 0, maxSideStep = 1;
    private boolean shouldAddMaxStep = false;
    private Direction direction = Direction.NORTH;

    @Getter
    private int stepCount = 0;

    public Chebyshev2DIterator(@NonNull BlockPos origin, int range, int minY, int maxY)
    {
        this.origin = new BlockPos(origin.getX(), 0, origin.getZ());
        this.range = range;
        this.minY = MathHelper.clamp(minY, 0, OreFinderConfig.MAX_HEIGHT.get());
        this.maxY = Math.min(maxY, OreFinderConfig.MAX_HEIGHT.get());
        this.delta = BlockPos.ZERO;
    }


    @Override
    public boolean hasNext()
    {
        return stepCount < range * range;
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
        BlockPos result = origin.add(delta);
        if (delta.getY() < maxY)
        {
            delta = delta.up();       // pos can reach maxY
        }
        else // reached maxY, moving to next xz
        {
            // reset y
            delta = new BlockPos(delta.getX(), minY, delta.getZ());
            if (step < maxSideStep) // on side, just step
            {
                delta = delta.offset(direction, 1);
                step++;
                stepCount++;
            } else                        // Old state at corner, turn right then step
            {
                direction = direction.rotateY();
                step = 0;
                if (shouldAddMaxStep)
                {
                    maxSideStep++;
                }
                shouldAddMaxStep = !shouldAddMaxStep;
                delta = delta.offset(direction, 1);
            }
        }
        return result;
    }

    public int size()
    {
        return this.range * this.range;
    }

    public Chebyshev2DIterator startAt(int start)
    {
        if (start < size() && start != 0)
        {
            int distance = MathHelper.floor(Math.sqrt(start));
            int sideLength = 2 * distance + 1;
            int mod = start - (sideLength - 2) * (sideLength - 2); // step count in this loop

            this.stepCount = start;
            Direction side = Direction.byHorizontalIndex(mod / (sideLength - 1) + 2);   // N-E-S-W
            /*
               Sides:
             Next Loop
                ↑ 0 1 2 3
                | ·---→     Add Max Step       N
                W N N N N ·                 -z |
              ↑ W ↑     E | 0                  |      +x
              | W  ...  E | 1          W ------┼-----→ E
              | W       E ↓ 2           -x     |
              · S S S S E   3                  ↓ +z
 Add Max Step     ←---- ·                      S
                3 2 1 0
             */
            this.step = mod % (sideLength - 1);
            switch (side)
            {
                case NORTH:
                    this.maxSideStep = sideLength - 2;
                    this.delta = new BlockPos(1 - distance + step, minY, -distance);
                    this.shouldAddMaxStep = true;
                    break;
                case EAST:
                    this.maxSideStep = sideLength - 1;
                    this.delta = new BlockPos(distance, minY, 1 - distance + step);
                    this.shouldAddMaxStep = false;
                    break;
                case SOUTH:
                    this.maxSideStep = sideLength - 1;
                    this.delta = new BlockPos(distance - 1 - step, minY, distance);
                    this.shouldAddMaxStep = true;
                    break;
                case WEST:
                    this.maxSideStep = sideLength;
                    this.delta = new BlockPos(-distance, minY, distance - 1 - step);
                    this.shouldAddMaxStep = false;
                    break;
            }
        }
        else if (start != this.stepCount)
        {
            this.stepCount = start; // No more elements or start from 0
            this.delta = BlockPos.ZERO.up(minY);
            this.direction = Direction.NORTH;
            this.maxSideStep = 1;
            this.shouldAddMaxStep = false;
            this.step = 0;
        }
        return this;
    }
}
