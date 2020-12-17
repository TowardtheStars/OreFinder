package towardsthestars.orefinder.util.multitick;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public interface IMultiTickTask
{
    // 执行任务
    void executeSingleTick();

    // 保存现场
    CompoundNBT saveScene();
    void restoreScene(CompoundNBT savedScene);

}
