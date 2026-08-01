package za.co.infernos.goety.mixin;

import za.co.infernos.goety.common.entities.ally.illager.AbstractIllagerServant;
import za.co.infernos.goety.config.MobsConfig;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
    public AbstractArrowMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Inject(
            method = {"canHitEntity(Lnet/minecraft/world/entity/Entity;)Z"},
            at = @At(value = "HEAD"),
            cancellable = true
    )
    protected void canHitEntity(Entity pEntity, CallbackInfoReturnable<Boolean> callback) {
        if (this.getOwner() instanceof AbstractIllagerServant servant
                && za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.IllagerServantGhostArrows, false)) {
            AbstractArrow arrow = (AbstractArrow) (Object) this;
            if (!MobUtil.canHitEntity(arrow, pEntity)) {
                callback.setReturnValue(false);
            }
        }
    }
}