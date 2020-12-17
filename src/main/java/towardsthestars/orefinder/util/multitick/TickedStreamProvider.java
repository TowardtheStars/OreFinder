package towardsthestars.orefinder.util.multitick;

import java.util.stream.Stream;

@FunctionalInterface
public interface TickedStreamProvider<T>
{
    Stream<T> provide(long tick);
}
