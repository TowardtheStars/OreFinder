package towardsthestars.orefinder.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import towardsthestars.orefinder.config.OreFinderConfig;
import towardsthestars.orefinder.item.helper.OreFinderHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ItemOreFinder extends TieredItem
{
    public ItemOreFinder(IItemTier tier, Properties properties)
    {
        super(tier, properties
                .defaultMaxDamage((int) Math.log(tier.getMaxUses()) * 5 - 10)
                .group(ItemGroup.TOOLS)
        );
    }

    public int findingRange()
    {
        return this.getTier().getEnchantability() / 6 + 4;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int r = 2 * this.findingRange() + 1;
        tooltip.add(new StringTextComponent(String.format("Range: %d x %d", r, r)));
    }

    @Override @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote)
        {
            playerIn.swingArm(handIn);
            return ActionResult.resultSuccess(stack);
        }
        BlockPos origin = playerIn.getPosition();
        int range = this.findingRange();
        if (origin.getY() >= OreFinderConfig.SEA_LEVEL.get() && !playerIn.isSneaking())
        {
            BlockPos start = new BlockPos(origin.getX() - range, 0, origin.getZ() - range);
            BlockPos end = new BlockPos(origin.getX() + range, OreFinderConfig.MAX_HEIGHT.get(), origin.getZ() + range);
            Map<Block, Integer> result = OreFinderHelper.find(worldIn, start, end);
            OreFinderHelper.report(playerIn, result);
        }
        else
        {
            Map<Block, Integer> result = OreFinderHelper.find(
                    worldIn,
                    origin.add(-range, -range, -range),
                    origin.add(range, range, range)
            );
            OreFinderHelper.report(playerIn, result);
        }
        stack.damageItem(1, playerIn, playerEntity -> playerEntity.sendBreakAnimation(handIn));
        return ActionResult.resultSuccess(stack);
    }



}
