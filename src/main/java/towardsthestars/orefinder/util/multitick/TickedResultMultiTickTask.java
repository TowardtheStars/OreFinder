package towardsthestars.orefinder.util.multitick;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Supplier;

public abstract class TickedResultMultiTickTask<T extends INBT, R extends TaskResult<T>> implements IMultiTickTask
{
    private Logger LOGGER = LogManager.getLogger();

    private long tick;

    private final Supplier<R> defaultResultSupplier;

    private R previousResult;


    private R result = null;

    public TickedResultMultiTickTask(Supplier<R> defaultResultSupplier)
    {
        this.defaultResultSupplier = defaultResultSupplier;
    }

    // 生成结果
    public abstract R generateResult();

    @Override
    public void executeSingleTick()
    {
        R previousResult = getPreviousResult();
        R newResult = generateResult();
        LOGGER.debug(MarkerManager.getMarker("executeSingleTick"), "tick: " + this.tick);

        if (previousResult == null || !previousResult.isValid())
        {
            result = newResult;
        }
        else
        {
            LOGGER.debug(MarkerManager.getMarker("ExecuteSingleTick"), "Previous Result is valid");
            if (newResult.isValid())
            {
                result = getPreviousResult().merge(newResult);
            }
            else
            {
                result = getPreviousResult();
            }
        }
    }



    public long getTick()
    {
        return this.tick;
    }

    public R getPreviousResult()
    {
        return this.previousResult;
    }

    public R getResult()
    {
        return this.result;
    }

    public void setTick(long tick)
    {
        this.tick = tick;
    }

    public void setPreviousResult(R previousResult)
    {
        this.previousResult = previousResult;
    }

    @Override
    public CompoundNBT saveScene()
    {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong("LastTick", this.getTick());
        nbt.put("Result", this.getResult().serializeNBT());
        return nbt;
    }

    @Override
    public void restoreScene(CompoundNBT savedScene)
    {
        this.setTick(savedScene.getLong("LastTick") + 1);
        R previous = defaultResultSupplier.get();
        previous.deserializeNBT((T)savedScene.get("Result"));
        this.setPreviousResult(previous);
    }
}
