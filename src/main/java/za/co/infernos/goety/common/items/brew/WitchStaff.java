package za.co.infernos.goety.common.items.brew;

import za.co.infernos.goety.common.entities.projectiles.ThrownBrew;
import za.co.infernos.goety.common.items.handler.WitchStaffItemHandler;
import za.co.infernos.goety.utils.BrewUtils;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WitchStaff extends Item {
    public WitchStaff(Properties p_41383_) {
        super(p_41383_);
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (getThrowBrew(itemstack) != null) {
            if (!worldIn.isClientSide) {
                ThrownBrew thrownBrew = new ThrownBrew(worldIn, playerIn);
                thrownBrew.setItem(itemstack);
                float velocity = 0.5F + BrewUtils.getVelocity(itemstack);
                thrownBrew.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -20.0F, velocity, 1.0F);
                if (worldIn.addFreshEntity(thrownBrew)) {
                    SEHelper.addCooldown(playerIn, this, MathHelper.secondsToTicks(1));
                }
            }

            playerIn.awardStat(Stats.ITEM_USED.get(this));
            if (!playerIn.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
        } else if (getDrinkBrew(itemstack) != null) {
            playerIn.startUsingItem(handIn);
        }

        return InteractionResultHolder.consume(itemstack);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        if (getDrinkBrew(stack) != null) {
            getDrinkBrew(stack).finishUsingItem(getBrew(stack), worldIn, entityLiving);
        }
        return stack;
    }

    public int getUseDuration(@NotNull ItemStack stack, LivingEntity livingEntity) {
        if (getDrinkBrew(stack) != null) {
            return getDrinkBrew(stack).getUseDuration(getBrew(stack), livingEntity);
        } else {
            return 0;
        }
    }

    public static ItemStack getBrew(ItemStack itemstack) {
        WitchStaffItemHandler handler = WitchStaffItemHandler.get(itemstack);
        return handler.getSlot();
    }

    public static BrewItem getDrinkBrew(ItemStack itemStack) {
        if (getBrew(itemStack) != null && !getBrew(itemStack).isEmpty()
                && getBrew(itemStack).getItem() instanceof BrewItem brewItem) {
            return brewItem;
        } else {
            return null;
        }
    }

    public static ThrowableBrewItem getThrowBrew(ItemStack itemStack) {
        if (getBrew(itemStack) != null && !getBrew(itemStack).isEmpty()
                && getBrew(itemStack).getItem() instanceof ThrowableBrewItem brewItem) {
            return brewItem;
        } else {
            return null;
        }
    }

    public static IItemHandler getItemHandler(ItemStack itemStack) {
        return new WitchStaffItemHandler(itemStack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.equals(newStack) && slotChanged;
    }
}