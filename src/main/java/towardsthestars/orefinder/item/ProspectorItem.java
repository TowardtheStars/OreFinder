package towardsthestars.orefinder.item;

import lombok.Getter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import towardsthestars.orefinder.config.OreFinderConfig;
import towardsthestars.orefinder.item.prospect.*;
import towardsthestars.orefinder.item.prospect.multitick.MultiTickProspector;
import towardsthestars.orefinder.item.prospect.result.ProspectResult;
import towardsthestars.orefinder.util.blockpos.BlockPosStreamProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ProspectorItem extends TieredItem
{
    @Getter
    private int findingRange;
    @Getter
    private IProspectorTaskProvider<?> prospectorProvider;

    public ProspectorItem(IItemTier tier, Properties properties)
    {
        super(tier, properties
                .defaultMaxDamage(
                        tier.getMaxUses()
                )
                .group(ItemGroup.TOOLS)
        );
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(
                new TranslationTextComponent(
                        "orefinder.tooltip.range",
                        this.findingRange
                ).applyTextStyle(TextFormatting.AQUA)
        );
        tooltip.add(
                new TranslationTextComponent(
                        "orefinder.tooltip.method",
                        this.prospectorProvider.getTooltip()
                )
        );
    }

    @Nonnull
    @Override
    public UseAction getUseAction(ItemStack stack)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return OreFinderConfig.MAX_USE_DURATION.get();
    }

    public int getMaxUseDuration()
    {
        return OreFinderConfig.MAX_USE_DURATION.get();
    }

    @Override @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    {
        playerIn.setActiveHand(handIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public static final String PROSPECT_SCENE_TAG = "ProspectScene";

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity livingEntity, int count)
    {
        World worldIn = livingEntity.getEntityWorld();
        if (livingEntity instanceof PlayerEntity && !worldIn.isRemote())
        {
            CompoundNBT itemStackTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
            MultiTickProspector<?, ?> task = this.getProspectorProvider().provide(
                    worldIn, livingEntity.getPosition(),
                    BlockPosStreamProvider.fromOrigin(
                            livingEntity.getPosition(), this.getFindingRange(),
                            BlockPosStreamProvider.DivideAxis.X, this.getMaxUseDuration()
                    )
            );
            task.setTick(this.getMaxUseDuration() - count);

            if (itemStackTag.contains(PROSPECT_SCENE_TAG, 10))
            {
                task.restoreScene(itemStackTag.getCompound(PROSPECT_SCENE_TAG));
            }
            task.executeSingleTick();
            itemStackTag.put(PROSPECT_SCENE_TAG, task.saveScene());
            stack.setTag(itemStackTag);
        }
    }

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, @Nonnull LivingEntity entityLiving)
    {
        CompoundNBT itemStackTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        if (!worldIn.isRemote())
        {
            if (hasResult(stack))
            {
                LogManager.getLogger().info("Prospect finished!");
                MultiTickProspector<?, ?> task = this.getProspectorProvider().provide(
                        worldIn, entityLiving.getPosition(),
                        BlockPosStreamProvider.fromOrigin(
                                entityLiving.getPosition(), this.getFindingRange(),
                                BlockPosStreamProvider.DivideAxis.X, this.getMaxUseDuration()
                        )
                );
                task.restoreScene(itemStackTag.getCompound(PROSPECT_SCENE_TAG));
                ProspectResult result = task.getPreviousResult();
                result.sendReport(entityLiving);
                removeResult(stack);
            } else
            {
                ProspectResult.NULL_RESULT.sendReport(entityLiving);
            }
        }
        stack.damageItem(1, entityLiving, livingEntity -> livingEntity.sendBreakAnimation(entityLiving.getActiveHand()));
        return stack;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (!worldIn.isRemote())
        {
            entityLiving.sendMessage(
                    new TranslationTextComponent("orefinder.hint.not_finish")
            );
        }
        removeResult(stack);
    }



    public ProspectorItem setFindingRange(int findingRange)
    {
        this.findingRange = findingRange;
        return this;
    }

    public ProspectorItem setProspectorProvider(IProspectorTaskProvider<?> prospector)
    {
        this.prospectorProvider = prospector;
        return this;
    }


    protected boolean hasResult(ItemStack stack)
    {
        CompoundNBT itemStackTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        return itemStackTag.contains(PROSPECT_SCENE_TAG, 10);
    }

    protected void removeResult(ItemStack stack)
    {
        CompoundNBT itemStackTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        if (hasResult(stack))
        {
            itemStackTag.remove(PROSPECT_SCENE_TAG);
            if (itemStackTag.isEmpty())
            {
                stack.setTag(null);
            }
            else
            {
                stack.setTag(itemStackTag);
            }
        }
    }
}
