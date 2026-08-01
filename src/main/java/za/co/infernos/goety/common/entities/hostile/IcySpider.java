package za.co.infernos.goety.common.entities.hostile;

import za.co.infernos.goety.common.entities.ally.spider.SpiderServant;
import za.co.infernos.goety.config.AttributesConfig;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;

public class IcySpider extends Spider {
    public IcySpider(EntityType<? extends Spider> p_33786_, Level p_33787_) {
        super(p_33786_, p_33787_);
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
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 7;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 15;
                }

                if (i > 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, i * 20, 0), this);
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
