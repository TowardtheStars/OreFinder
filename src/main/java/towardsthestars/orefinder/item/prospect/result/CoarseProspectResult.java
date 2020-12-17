package towardsthestars.orefinder.item.prospect.result;

import lombok.*;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import towardsthestars.orefinder.util.IPlainDirection;
import towardsthestars.orefinder.util.Plain8Direction;
import towardsthestars.orefinder.util.multitick.TaskResult;

/**
 * Whether there are ores or not
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoarseProspectResult extends ProspectResult<CompoundNBT>
{
    @Getter
    @Setter
    private BlockPos origin;
    private BlockPos orePos = null;
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
            nbt.put("origin", NBTUtil.writeBlockPos(origin));
            nbt.put("orepos", NBTUtil.writeBlockPos(orePos));
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
        this.origin = NBTUtil.readBlockPos(nbt.getCompound("origin"));
        this.orePos = NBTUtil.readBlockPos(nbt.getCompound("orepos"));
    }

    public double distanceSq()
    {
        return this.orePos.distanceSq(this.getOrigin());
    }


    @Override
    public CoarseProspectResult merge(TaskResult another)
    {
        if (another instanceof CoarseProspectResult)
        {
            CoarseProspectResult another1 = (CoarseProspectResult) another;
            if (this.isEmpty() || (another1.isValid() && another1.distanceSq() < this.distanceSq()))
            {
                this.orePos = another1.orePos;
                this.oreBlock = another1.oreBlock;
                this.direction = another1.direction;
            }
        }
        return this;
    }
}
