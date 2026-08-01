package za.co.infernos.goety.common.entities.ally.spider;

import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class IcySpiderServant extends SpiderServant{
    public IcySpiderServant(EntityType<? extends SpiderServant> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return SpiderServant.setCustomAttributes()
                .add(Attributes.MAX_HEALTH, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.IcySpiderServantHealth, 20.0D))
                .add(Attributes.ATTACK_DAMAGE, za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.IcySpiderServantDamage, 20.0D));
    }

    public void setConfigurableAttributes() {
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.MAX_HEALTH), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.IcySpiderServantHealth, 20.0D));
        MobUtil.setBaseAttributes(this.getAttribute(Attributes.ATTACK_DAMAGE), za.co.infernos.goety.utils.ConfigHelper.getDouble(AttributesConfig.IcySpiderServantDamage, 20.0D));
    }

    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            if (target instanceof LivingEntity livingEntity) {
                int i = this.getMasterOwner() instanceof Player ? 7 : 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 7;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 15;
                }

                if (i > 0) {
                    Holder<MobEffect> effect = MobEffects.MOVEMENT_SLOWDOWN;
                    if (CuriosFinder.hasFrostRobes(this.getMasterOwner())){
                        effect = GoetyEffects.FREEZING;
                    }
                    livingEntity.addEffect(new MobEffectInstance(effect, i * 20, 0), this);
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
