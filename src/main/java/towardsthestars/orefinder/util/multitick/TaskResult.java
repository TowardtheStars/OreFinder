package towardsthestars.orefinder.util.multitick;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface TaskResult<NBT extends INBT> extends INBTSerializable<NBT>
{
    /**
     * Merge function
     * @param another Another task result
     * @return Always return this
     */
    <T extends TaskResult<?>> T merge(T another);

    boolean isValid();
}
