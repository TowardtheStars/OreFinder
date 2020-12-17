package towardsthestars.orefinder.item.prospect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import towardsthestars.orefinder.item.prospect.multitick.MultiTickCoarseProspecting;
import towardsthestars.orefinder.item.prospect.multitick.MultiTickCounting;
import towardsthestars.orefinder.item.prospect.multitick.MultiTickExactProspecting;
import towardsthestars.orefinder.item.prospect.multitick.MultiTickProspector;
import towardsthestars.orefinder.util.Plain8Direction;
import towardsthestars.orefinder.util.multitick.TickedStreamProvider;

public interface IProspectorTaskProvider<T extends MultiTickProspector<?, ?>>
{
    T provide(World world, BlockPos origin, TickedStreamProvider<BlockPos> provider);
    ITextComponent getTooltip();

    IProspectorTaskProvider<MultiTickCoarseProspecting> COARSE4
            = new IProspectorTaskProvider<MultiTickCoarseProspecting>()
    {
        @Override
        public MultiTickCoarseProspecting provide(World world, BlockPos origin, TickedStreamProvider<BlockPos> provider)
        {
            return new MultiTickCoarseProspecting(world, origin, provider, Plain8Direction::fromVec2f4);
        }

        @Override
        public ITextComponent getTooltip()
        {
            return new TranslationTextComponent("orefinder.prospector.coarse4");
        }
    };

    IProspectorTaskProvider<MultiTickCoarseProspecting> COARSE8
            = new IProspectorTaskProvider<MultiTickCoarseProspecting>()
    {
        @Override
        public MultiTickCoarseProspecting provide(World world, BlockPos origin, TickedStreamProvider<BlockPos> provider)
        {
            return new MultiTickCoarseProspecting(world, origin, provider, Plain8Direction::fromVec2f8);
        }

        @Override
        public ITextComponent getTooltip()
        {
            return new TranslationTextComponent("orefinder.prospector.coarse8");
        }
    };

    IProspectorTaskProvider<MultiTickCounting> COUNT
            = new IProspectorTaskProvider<MultiTickCounting>()
    {
        @Override
        public MultiTickCounting provide(World world, BlockPos origin, TickedStreamProvider<BlockPos> provider)
        {
            return new MultiTickCounting(world, origin, provider);
        }

        @Override
        public ITextComponent getTooltip()
        {
            return new TranslationTextComponent("orefinder.prospector.count")
                    .applyTextStyle(TextFormatting.AQUA);
        }
    };
    IProspectorTaskProvider<MultiTickExactProspecting> EXACT
            = new IProspectorTaskProvider<MultiTickExactProspecting>()
    {
        @Override
        public MultiTickExactProspecting provide(World world, BlockPos origin, TickedStreamProvider<BlockPos> provider)
        {
            return new MultiTickExactProspecting(world, origin, provider);
        }

        @Override
        public ITextComponent getTooltip()
        {
            return new TranslationTextComponent("orefinder.prospector.exact")
                    .applyTextStyle(TextFormatting.YELLOW);
        }
    };
}
