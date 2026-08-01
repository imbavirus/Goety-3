package za.co.infernos.goety.common.effects.brew.block;

import za.co.infernos.goety.common.effects.brew.BrewEffect;
import za.co.infernos.goety.config.BrewConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;

public class CorrosionBlockEffect extends BrewEffect {
    public CorrosionBlockEffect() {
        super("corrosion", za.co.infernos.goety.utils.ConfigHelper.getInt(BrewConfig.CorrosionCost, 0), MobEffectCategory.NEUTRAL, 0xbae633);
    }

    @Override
    public void applyBlockEffect(Level pLevel, BlockPos pPos, LivingEntity pSource, int pAmplifier, int pAreaOfEffect) {
        if (pLevel instanceof ServerLevel serverLevel) {
            for (BlockPos blockPos : this.getSpherePos(pPos, pAreaOfEffect + 3)) {
                BlockState state = serverLevel.getBlockState(blockPos);
                if (!state.is(BlockTags.WITHER_IMMUNE)
                        && !state.hasBlockEntity()
                        && state.canEntityDestroy(pLevel, blockPos, pSource)
                        && state.getDestroySpeed(serverLevel, blockPos) != -1.0F){
                    serverLevel.destroyBlock(blockPos, state.is(BlockTags.create(net.minecraft.resources.ResourceLocation.parse("c:obsidians"))));
                }
                for (LivingEntity livingEntity : pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(blockPos))){
                    this.applyEntityEffect(livingEntity, pSource, pSource, pAmplifier);
                }
            }
            serverLevel.playSound(null, pPos, ModSounds.BREW_GAS_ALT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        }
    }

    @Override
    public void applyEntityEffect(LivingEntity pTarget, @Nullable Entity source, @Nullable Entity pIndirectSource, int pAmplifier){
        pAmplifier += 1;
        if(pTarget.level().random.nextInt(Mth.floor(5.0F / Mth.clamp(pAmplifier, 1, 5))) == 0) {
            pTarget.hurt(pTarget.damageSources().thrown(pTarget, pIndirectSource), 8.0F * pAmplifier);
        }

        for(EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack stack = pTarget.getItemBySlot(equipmentSlot);
            if(stack.isDamageableItem()) {
                ItemHelper.hurtAndBreak(stack, 50 + (pTarget.level().random.nextInt(25) * pAmplifier), pIndirectSource instanceof LivingEntity entity ? entity : null);
            }
        }
    }
}
