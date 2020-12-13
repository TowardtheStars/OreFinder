package towardsthestars.orefinder.util;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.util.text.ITextComponent;

public interface IPlainDirection
{
    ITextComponent getTranslationTextComponent();
    ByteNBT getNBT();
}
