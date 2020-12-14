package towardsthestars.orefinder.item.prospect.result;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import towardsthestars.orefinder.config.OreFinderConfig;

import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
public class CountingProspectResult extends ProspectResult<ListNBT, CountingProspectResult>
{
    private Map<Block, Long> result = Maps.newHashMap();

    @Override
    public CountingProspectResult merge(CountingProspectResult another)
    {
        another.result.forEach(
                (block, count) ->
                this.result.merge(block, count, Long::sum)
        );
        return this;
    }

    @Override
    public boolean isEmpty()
    {
        return result.isEmpty();
    }

    @Override
    protected void report(ICommandSource source)
    {
        source.sendMessage(
                new TranslationTextComponent("orefinder.report.header")
                        .setStyle(new Style().setColor(TextFormatting.GREEN))
        );

        for (Map.Entry<Block, Long> entry : result.entrySet())
        {
            if (entry.getValue() > 0)
            {
                source.sendMessage(
                        new ItemStack(entry.getKey().asItem(), 1).getTextComponent()
                                .appendText(" : ")
                                .appendSibling(
                                        OreFinderConfig.Amount.getLevelTranslationTextComponent(entry.getValue())
                                )
                );
            }
        }
    }

    @Override
    public ListNBT serializeNBT()
    {
        ListNBT nbt = new ListNBT();
        nbt.addAll(this.result.entrySet().stream().map
                (
                        entry ->
                        {
                            CompoundNBT entryNBT = new CompoundNBT();
                            entryNBT.putString("block", entry.getKey().getRegistryName().toString());
                            entryNBT.putLong("count", entry.getValue());
                            return entryNBT;
                        }
                )
                .collect(Collectors.toSet())
        );
        return nbt;
    }

    @Override
    public void deserializeNBT(ListNBT nbt)
    {
        this.result = nbt.stream().map(inbt -> (CompoundNBT)inbt)
                .filter(
                        compoundNBT ->
                                compoundNBT.contains("block", 8)
                )
                .collect(Collectors.toMap(
                        compoundNBT -> ForgeRegistries.BLOCKS.getValue(
                                new ResourceLocation(compoundNBT.getString("block"))
                        ),
                        compoundNBT -> compoundNBT.contains("count", 4) ?
                                compoundNBT.getLong("count") : 0,
                        Long::sum
                ));
    }

    @Override
    public String resultType()
    {
        return "orefinder:counting";
    }
}
