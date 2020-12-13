package towardsthestars.orefinder.item.prospect.result;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSource;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

@AllArgsConstructor
@NoArgsConstructor
public class ExactPosProspectResult extends ProspectResult<CompoundNBT>
{
    private BlockPos relativeCoordinates = BlockPos.ZERO;
    private Block block = null;

    @Override
    public boolean isEmpty()
    {
        return block == null;
    }

    @Override
    protected void report(ICommandSource source)
    {
        source.sendMessage(
                new TranslationTextComponent(
                        "orefinder.report.find_exact",
                        block.asItem().getDefaultInstance().getTextComponent(),
                        String.format("[%d, %d, %d]", relativeCoordinates.getX(), relativeCoordinates.getY(), relativeCoordinates.getZ())
                )
        );
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("block", this.block.getRegistryName().toString());
        nbt.put("pos", NBTUtil.writeBlockPos(this.relativeCoordinates));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        if(nbt.contains("block", 8))
        {
            this.block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
        }
        if (nbt.contains("pos", 10))
        {
            this.relativeCoordinates = NBTUtil.readBlockPos(nbt.getCompound("pos"));
        }
    }

    @Override
    public String resultType()
    {
        return "orefinder:exact";
    }
}
