package za.co.infernos.goety.client.audio;

import za.co.infernos.goety.common.magic.spells.abyss.PrismaBeamSpell;
import za.co.infernos.goety.config.SpellConfig;
import za.co.infernos.goety.utils.MiscCapHelper;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class GuardianLaserSound extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0F;
    private static final float VOLUME_SCALE = 1.0F;
    private static final float PITCH_MIN = 0.7F;
    private static final float PITCH_SCALE = 0.5F;
    private final LivingEntity livingEntity;

    public GuardianLaserSound(LivingEntity livingEntity) {
        super(SoundEvents.GUARDIAN_ATTACK, livingEntity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.livingEntity = livingEntity;
        this.x = (float) livingEntity.getX();
        this.y = (float) livingEntity.getY();
        this.z = (float) livingEntity.getZ();
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
    }

    public boolean canPlaySound() {
        return !this.livingEntity.isSilent();
    }

    public void tick() {
        Entity entity = MiscCapHelper.getClientTarget(this.livingEntity);
        if (!this.livingEntity.isRemoved() && this.livingEntity.isAlive() && this.livingEntity.isUsingItem()
                && WandUtil.getSpell(this.livingEntity) instanceof PrismaBeamSpell && entity != null) {
            this.x = (float) this.livingEntity.getX();
            this.y = (float) this.livingEntity.getY();
            this.z = (float) this.livingEntity.getZ();
            int count = livingEntity.getUseItemRemainingTicks();
            ItemStack useItem = livingEntity.getUseItem();
            int useDuration = useItem.getItem().getUseDuration(useItem, this.livingEntity);
            int CastTime = useDuration - count;
            float f = CastTime / (float) za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.PrismaBeamDuration, 0);
            this.volume = 0.0F + 1.0F * f * f;
            this.pitch = 0.7F + 0.5F * f;
        } else {
            this.stop();
        }
    }
}