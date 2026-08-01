package za.co.infernos.goety.common.entities.ally.spider;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.Holder;

public class CaveSpiderServant extends SpiderServant{
    public CaveSpiderServant(EntityType<? extends SpiderServant> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return SpiderServant.setCustomAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CaveSpiderServantHealth, 20.0D))
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CaveSpiderServantDamage, 20.0D));
    }

    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CaveSpiderServantHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.CaveSpiderServantDamage, 20.0D));
    }

    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity livingEntity) {
               if (livingEntity instanceof SpiderServant) {
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 7;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 15;
                }

                if (i > 0) {
                    Holder<MobEffect> effect = MobEffects.POISON;
                    if (i == 15){
                        effect = GoetyEffects.ACID_VENOM;
                    }
                    livingEntity.addEffect(new MobEffectInstance(effect, i * 20, 0), this);
                }
            }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean spawnWithEffects() {
        return false;
    }

    protected float getStandingEyeHeight(Pose p_32265_, EntityDimensions p_32266_) {
        return 0.45F;
    }
}
