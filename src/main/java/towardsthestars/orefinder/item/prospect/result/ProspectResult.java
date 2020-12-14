package towardsthestars.orefinder.item.prospect.result;

import com.google.common.collect.Maps;
import lombok.NoArgsConstructor;
import net.minecraft.command.ICommandSource;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.EndNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import towardsthestars.orefinder.util.ICanMerge;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Supplier;

@NoArgsConstructor
public abstract class ProspectResult<T extends INBT, E extends ProspectResult>
        implements INBTSerializable<T>, ICanMerge<E>
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

    protected abstract void report(ICommandSource source);

    public abstract String resultType();

    private static Map<String, Supplier<? extends ProspectResult>> RESULT_PROVIDERS = Maps.newHashMap();

    public static void register(Supplier<? extends ProspectResult> supplier)
    {
        RESULT_PROVIDERS.put(supplier.get().resultType(), supplier);
    }

    public static class NullResult extends ProspectResult<EndNBT, NullResult>
    {
        private NullResult(){}
        static NullResult NULL_RESULT = new NullResult();
        @Override
        protected void report(ICommandSource source)
        {
            source.sendMessage(new TranslationTextComponent("orefinder.report.empty")
                    .setStyle(new Style().setColor(TextFormatting.GREEN))
            );
        }

        @Override
        public String resultType()
        {
            return null;
        }

        @Override
        public EndNBT serializeNBT()
        {
            return null;
        }

        @Override
        public void deserializeNBT(EndNBT nbt)
        {

        }

        @Override
        public NullResult merge(NullResult another)
        {
            return this;
        }
    }

    public static ProspectResult getNull()
    {
        return NullResult.NULL_RESULT;
    }

    public static Supplier<? extends ProspectResult> getValue(String type)
    {
        return RESULT_PROVIDERS.get(type);
    }

    @Nonnull
    public static ProspectResult fromNBT(CompoundNBT nbt)
    {
        Supplier<? extends ProspectResult> supplier = ProspectResult.getValue(nbt.getString("type"));
        if (supplier != null)
        {
            ProspectResult prospectResult = supplier.get();
            prospectResult.deserializeNBT(nbt.get("result"));
            return prospectResult;
        }
        return NullResult.getNull();
    }

    @Nonnull
    public static CompoundNBT toNBT(ProspectResult result)
    {
        if (!result.isEmpty())
        {
            CompoundNBT resultNbt = new CompoundNBT();
            resultNbt.putString("type", result.resultType());
            resultNbt.put("result", result.serializeNBT());
            return resultNbt;
        }
        return new CompoundNBT();
    }
}
