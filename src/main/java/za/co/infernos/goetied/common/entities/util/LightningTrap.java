package za.co.infernos.goetied.common.entities.util;

import za.co.infernos.goetied.client.particles.AoEParticleOption;
import za.co.infernos.goetied.client.particles.ModParticleTypes;
import za.co.infernos.goetied.common.entities.ModEntityType;
import za.co.infernos.goetied.common.entities.boss.Apostle;
import za.co.infernos.goetied.common.entities.projectiles.SpellLightningBolt;
import za.co.infernos.goetied.config.AttributesConfig;
import za.co.infernos.goetied.utils.ColorUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class LightningTrap extends AbstractTrap {

    public LightningTrap(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.setParticle(ModParticleTypes.NONE.get());
    }

    public LightningTrap(Level worldIn, double x, double y, double z) {
        this(ModEntityType.LIGHTNING_TRAP.get(), worldIn);
        this.setPos(x, y, z);
    }

    @Override
    public float radius() {
        return 1.5F;
    }

    public void tick() {
        super.tick();
        if (this.tickCount == 1) {
            if (this.level() instanceof ServerLevel serverLevel) {
                ColorUtil colorUtil = ColorUtil.WHITE;
                serverLevel.sendParticles(new AoEParticleOption(3.0F, 3.0F / this.getDuration(), 0.0F, this.getDuration()), this.getX(), this.getY() + 0.1F, this.getZ(), 0, colorUtil.red(), colorUtil.green(), colorUtil.blue(), 1.0F);
            }
        }
        if (this.tickCount >= this.getDuration()) {
            SpellLightningBolt lightning = new SpellLightningBolt(ModEntityType.SPELL_LIGHTNING_BOLT.get(), this.level());
            lightning.setPos(this.getX(),this.getY(),this.getZ());
            lightning.setOwner(this.getOwner());
            if (this.getOwner() instanceof Apostle){
                lightning.setDamage((float)za.co.infernos.goetied.utils.ConfigHelper.getDouble(AttributesConfig.ApostleMagicDamage, 20.0D));
            }
            level().addFreshEntity(lightning);
            this.discard();
        }
    }
}