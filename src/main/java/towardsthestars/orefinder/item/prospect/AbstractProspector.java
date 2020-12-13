package towardsthestars.orefinder.item.prospect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import towardsthestars.orefinder.item.prospect.result.ProspectResult;

import java.util.stream.Stream;

public abstract class AbstractProspector<T extends ProspectResult> implements IProspector<T>
{
    public abstract ITextComponent getTooltip();

    public static <T extends ProspectResult> AbstractProspector<T>
            of(IProspector<T> prospector, ITextComponent tooltip)
    {
        return new AbstractProspector<T>()
        {
            @Override
            public ITextComponent getTooltip()
            {
                return tooltip;
            }

            @Override
            public T prospect(World world, Stream<BlockPos> posRange, Vec3i prospectOrigin)
            {
                return prospector.prospect(world, posRange, prospectOrigin);
            }
        };
    }
}
