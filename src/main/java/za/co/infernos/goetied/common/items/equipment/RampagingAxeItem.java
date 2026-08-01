package za.co.infernos.goetied.common.items.equipment;

import za.co.infernos.goetied.common.effects.GoetiedEffects;
import za.co.infernos.goetied.common.items.ModTiers;
import za.co.infernos.goetied.utils.EffectsUtil;
import za.co.infernos.goetied.utils.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RampagingAxeItem extends AxeItem {
    public RampagingAxeItem() {
        super(ModTiers.SPECIAL, (new Properties()).rarity(Rarity.UNCOMMON));
    }

    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (pState.is(BlockTags.MINEABLE_WITH_AXE)){
            MobEffectInstance effectinstance1 = pEntityLiving.getEffect(GoetiedEffects.RAMPAGE);
            if (!pEntityLiving.hasEffect(GoetiedEffects.RAMPAGE)){
                pEntityLiving.addEffect(new MobEffectInstance(GoetiedEffects.RAMPAGE, MathHelper.secondsToTicks(10)));
            } else if (effectinstance1 != null){
                if (effectinstance1.getAmplifier() < 4 && pLevel.random.nextFloat() <= 0.25F) {
                    EffectsUtil.amplifyEffect(pEntityLiving, GoetiedEffects.RAMPAGE.get(), MathHelper.secondsToTicks(10));
                } else {
                    EffectsUtil.resetDuration(pEntityLiving, GoetiedEffects.RAMPAGE.get(), MathHelper.secondsToTicks(10));
                }
            }
        }
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }
}