package towardsthestars.orefinder.util.multitick;

import net.minecraft.nbt.INBT;

import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class MultiTickTraverseStream<E, T extends INBT, R extends TaskResult<T>>
        extends TickedResultMultiTickTask<T, R>
{
    private TickedStreamProvider<E> streamProvider;

    public MultiTickTraverseStream(
            TickedStreamProvider<E> stream,
            Supplier<R> defaultResultSupplier
    )
    {
        super(defaultResultSupplier);
        this.streamProvider = stream;
    }

    public Stream<E> getTargetStream()
    {
        return streamProvider.provide(this.getTick());
    }


}
