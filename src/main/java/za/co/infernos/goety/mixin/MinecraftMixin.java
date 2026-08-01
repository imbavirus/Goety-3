package za.co.infernos.goety.mixin;

import za.co.infernos.goety.common.blocks.entities.OminousIdolBlockEntity;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.entities.ally.illager.RaiderServant;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.magic.CommandFocus;
import za.co.infernos.goety.common.items.magic.OrderFocus;
import za.co.infernos.goety.common.items.magic.TransferScroll;
import za.co.infernos.goety.utils.WandUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "shouldEntityAppearGlowing", at = @At(value = "HEAD"), cancellable = true)
    public void shouldEntityAppearGlowing(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player != null) {
            if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.TREMOR_SENSE.get()))
                    && pEntity instanceof LivingEntity livingEntity
                    && player.onGround()
                    && livingEntity.onGround()) {
                MobEffectInstance instance = player.getEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(GoetyEffects.TREMOR_SENSE.get()));
                if (instance != null) {
                    float amp = (instance.getAmplifier() + 1.0F) / 2.0F;
                    if (player.distanceTo(livingEntity) <= 32.0F * amp) {
                        cir.setReturnValue(true);
                    }
                }
            }
            if (pEntity instanceof LivingEntity livingEntity) {
                if (WandUtil.findFocus(player).getItem() instanceof CommandFocus) {
                    LivingEntity servant = CommandFocus.getServantClient(player.level(), WandUtil.findFocus(player));
                    if (servant != null && servant == livingEntity){
                        cir.setReturnValue(true);
                    }
                } else if (WandUtil.findFocus(player).getItem() instanceof OrderFocus) {
                    List<LivingEntity> list = OrderFocus.getServantsClient(player.level(), WandUtil.findFocus(player));
                    if (!list.isEmpty()) {
                        if (list.contains(livingEntity)) {
                            cir.setReturnValue(true);
                        }
                    }
                }
                if (player.isHolding(ModItems.TRANSFER_SCROLL.get())) {
                    ItemStack itemStack = player.getMainHandItem().is(ModItems.TRANSFER_SCROLL.get()) ? player.getMainHandItem() : player.getOffhandItem();
                    if (TransferScroll.hasSummon(itemStack)) {
                        LivingEntity livingEntity1 = TransferScroll.getSummon(itemStack);
                        if (livingEntity == livingEntity1) {
                            cir.setReturnValue(true);
                        }
                    }
                }
                if (player.isHolding(ModItems.COMMAND_HORN.get())) {
                    // Command horn selection list is disabled in compatibility mode.
                }
                if (pEntity instanceof RaiderServant servant) {
                    if (servant.canLinkToIdol()) {
                        if (player.isCrouching() || player.isShiftKeyDown()) {
                            HitResult hitResult = minecraft.hitResult;
                            if (minecraft.level != null) {
                                if (hitResult instanceof BlockHitResult blockRayTraceResult) {
                                    BlockEntity blockEntity = minecraft.level.getBlockEntity(blockRayTraceResult.getBlockPos());
                                    if (blockEntity instanceof OminousIdolBlockEntity idolBlock) {
                                        if (idolBlock.getIds().contains(servant.getId())) {
                                            cir.setReturnValue(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
