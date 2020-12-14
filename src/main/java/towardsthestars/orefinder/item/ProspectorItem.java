package towardsthestars.orefinder.item;

import lombok.Getter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import towardsthestars.orefinder.config.OreFinderConfig;
import towardsthestars.orefinder.item.prospect.*;
import towardsthestars.orefinder.item.prospect.result.ProspectResult;
import towardsthestars.orefinder.util.IRandomStartIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ProspectorItem extends TieredItem
{
    @Getter
    private int findingRange;
    @Getter
    private AbstractProspector<? extends ProspectResult> prospector;
    @Getter
    private IProspectPosRangeProvider underSeaLevelStreamProvider;
    @Getter
    private IProspectPosRangeProvider aboveSeaLevelStreamProvider;

    public ProspectorItem(IItemTier tier, Properties properties)
    {
        super(tier, properties
                .defaultMaxDamage(
                        tier.getMaxUses()
                )
                .group(ItemGroup.TOOLS)
        );

        this
                .setAboveSeaLevelStreamProvider(PosRangeProviders.EXPAND_DOWN_CHEBYSHEV)
                .setUnderSeaLevelStreamProvider(PosRangeProviders.EXPAND_CUBE_CHEBYSHEV);
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
                        this.prospector.getTooltip()
                )
        );
    }

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

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity livingEntity, int count)
    {
        World worldIn = livingEntity.getEntityWorld();
        if (livingEntity instanceof PlayerEntity && !worldIn.isRemote())
        {
            CompoundNBT itemStackTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
            ProspectResult previousResult = ProspectResult.fromNBT(itemStackTag.getCompound("prospect_result"));

            PlayerEntity playerIn = (PlayerEntity) livingEntity;
            BlockPos origin = playerIn.getPosition();
            IRandomStartIterator<BlockPos> rangeIter;
            if (origin.getY() >= OreFinderConfig.SEA_LEVEL.get() && !playerIn.isSneaking())
            {
                rangeIter = this.aboveSeaLevelStreamProvider.provide(origin, this.getFindingRange(), playerIn.getPitchYaw());
            } else
            {
                rangeIter = this.underSeaLevelStreamProvider.provide(origin, this.getFindingRange(), playerIn.getPitchYaw());
            }
            int unit_size = rangeIter.size() / this.getMaxUseDuration();
            rangeIter.startAt((this.getMaxUseDuration() - count) * unit_size);
            Stream<BlockPos> range =
                    StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize(
                                    rangeIter, Spliterator.SIZED
                            ),
                            false
                    ).limit(unit_size);
            ProspectResult result = this.prospector.prospect(worldIn, range, origin);
            result.merge(previousResult);
            if (!result.isEmpty())
            {
                itemStackTag.put("prospect_result", ProspectResult.toNBT(result));
                stack.setTag(itemStackTag);
            }

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
                CompoundNBT resultNBT = itemStackTag.getCompound("prospect_result");
                ProspectResult result = ProspectResult.fromNBT(resultNBT);
                result.sendReport(entityLiving);
                removeResult(stack);
            } else
            {
                ProspectResult.getNull().sendReport(entityLiving);
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

    public ProspectorItem setProspector(AbstractProspector<? extends ProspectResult> prospector)
    {
        this.prospector = prospector;
        return this;
    }

    public ProspectorItem setUnderSeaLevelStreamProvider(IProspectPosRangeProvider underSeaLevelStreamProvider)
    {
        this.underSeaLevelStreamProvider = underSeaLevelStreamProvider;
        return this;
    }

    public ProspectorItem setAboveSeaLevelStreamProvider(IProspectPosRangeProvider aboveSeaLevelStreamProvider)
    {
        this.aboveSeaLevelStreamProvider = aboveSeaLevelStreamProvider;
        return this;
    }

    protected boolean hasResult(ItemStack stack)
    {
        CompoundNBT itemStackTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        return itemStackTag.contains("prospect_result", 10);
    }

    protected void removeResult(ItemStack stack)
    {
        CompoundNBT itemStackTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        if (hasResult(stack))
        {
            itemStackTag.remove("prospect_result");
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
