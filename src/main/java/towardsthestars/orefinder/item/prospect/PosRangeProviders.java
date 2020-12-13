package towardsthestars.orefinder.item.prospect;

import towardsthestars.orefinder.util.Chebyshev2DIterator;

public class PosRangeProviders
{

    public static IProspectPosRangeProvider EXPAND_CHEBYSHEV = (origin, radius, facing) ->
            new Chebyshev2DIterator(origin, radius, 0, 255);

    public static IProspectPosRangeProvider EXPAND_DOWN_CHEBYSHEV = (origin, radius, facing) ->
            new Chebyshev2DIterator(origin, radius, 0, origin.getY());

    public static IProspectPosRangeProvider EXPAND_CUBE_CHEBYSHEV = (origin, radius, facing) ->
            new Chebyshev2DIterator(origin, radius, origin.getY() - radius, origin.getY() + radius);


}
