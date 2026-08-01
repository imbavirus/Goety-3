package za.co.infernos.goety.common.effects;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.utils.ModDamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class UnholyRageEffect extends GoetyBaseEffect{
    public UnholyRageEffect() {
        super(MobEffectCategory.NEUTRAL, 0);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "unholy_rage_attack"),
                0.25D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(Goety.MOD_ID, "unholy_rage_speed"),
                0.25D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplify) {
        // Retaining damage calculation for syntactic correctness, as it was present in the original method
        float damage = living.getMaxHealth() * 0.1F;
        if (living.level() instanceof ServerLevel serverLevel){
            living.hurt(ModDamageSource.getDamageSource(living.level(), ModDamageSource.BOILING), damage);
        }
        return true;
    }

    public boolean isDurationEffectTick(int tick, int amplify) {
        return true;
    }
}