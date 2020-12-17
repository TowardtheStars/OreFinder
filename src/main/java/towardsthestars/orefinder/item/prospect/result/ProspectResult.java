package towardsthestars.orefinder.item.prospect.result;

import lombok.NoArgsConstructor;
import net.minecraft.command.ICommandSource;
import net.minecraft.nbt.EndNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import towardsthestars.orefinder.util.multitick.TaskResult;


@NoArgsConstructor
public abstract class ProspectResult<NBT extends INBT>
        implements TaskResult<NBT>
{

    public boolean isEmpty()
    {
        return true;
    }

    public void sendReport(ICommandSource source)
    {
        if (this.isEmpty())
        {
            source.sendMessage(new TranslationTextComponent("orefinder.report.empty")
                    .setStyle(new Style().setColor(TextFormatting.GREEN))
            );
        }
        else
        {
            report(source);
        }
    }

    // abstract
    protected void report(ICommandSource source){}


    @Override
    public NBT serializeNBT()
    {
        return null;
    }

    @Override
    public void deserializeNBT(NBT nbt)
    {

    }


    public static final ProspectResult NULL_RESULT = new ProspectResult<EndNBT>(){
        @Override
        public EndNBT serializeNBT()
        {
            return EndNBT.INSTANCE;
        }
    };


    /**
     * Merge function
     *
     * @param another Another task result
     * @return Always return this
     */
    @Override
    public <T extends TaskResult<?>> T merge(T another)
    {
        return another;
    }

    @Override
    public boolean isValid()
    {
        return !this.isEmpty();
    }
}
