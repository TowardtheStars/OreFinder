package towardsthestars.orefinder.util;

import java.util.Iterator;

public interface IRandomStartIterator<E> extends Iterator<E>
{
    IRandomStartIterator<E> startAt(int start);
    int size();
}
