package towardsthestars.orefinder.item.prospect.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import towardsthestars.orefinder.util.IPlainDirection;
import towardsthestars.orefinder.util.Plain8Direction;

/**
 * Whether there are ores or not
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoarseProspectResult extends ProspectResult<CompoundNBT>
{

    private IPlainDirection direction = null;
    private Block oreBlock = null;
    @Override
    public boolean isEmpty()
    {
        return oreBlock == null;
    }

    @Override
    protected void report(ICommandSource source)
    {
        ITextComponent mainComponent = new TranslationTextComponent(
                "orefinder.report.coarse",
                new ItemStack(oreBlock.asItem()).getTextComponent(),
                direction.getTranslationTextComponent()
        );

        source.sendMessage(mainComponent);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        if (!this.isEmpty())
        {
            nbt.put("direction", this.direction.getNBT());
            nbt.putString("block", this.oreBlock.getRegistryName().toString());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        if (nbt.contains("direction"))
        {
            this.direction = Plain8Direction.DIRECTIONS8[nbt.getByte("direction")];
        }
        if (nbt.contains("block"))
        {
            this.oreBlock = ForgeRegistries.BLOCKS.getValue(
                    new ResourceLocation(
                        nbt.getString("block")
                    )
            );
        }
    }

    @Override
    public String resultType()
    {
        return "orefinder:coarse";
    }
}
