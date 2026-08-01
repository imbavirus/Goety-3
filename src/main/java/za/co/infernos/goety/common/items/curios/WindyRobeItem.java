package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.network.ModNetwork;
import za.co.infernos.goety.common.network.server.SLightningPacket;
import za.co.infernos.goety.compat.iron.IronAttributes;
import za.co.infernos.goety.compat.iron.IronLoaded;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.SEHelper;
import za.co.infernos.goety.utils.ServerParticleUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;
import java.util.UUID;

public class WindyRobeItem extends SingleStackItem{

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof Player player) {
            if (CuriosFinder.hasWindyRobes(player) && !player.isSpectator()){
                if (SEHelper.getSoulsAmount(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.WindRobeSouls, 0))
                        || player.isCreative()) {
                    Vec3 vector3d = player.getDeltaMovement();
                    if (player.hasEffect(MobEffects.SLOW_FALLING)){
                        player.removeEffect(MobEffects.SLOW_FALLING);
                    }
                    if (!player.onGround() && vector3d.y < 0.0D
                            && !player.isNoGravity()
                            && !player.getAbilities().flying
                            && !player.onClimbable()
                            && !player.isInFluidType()
                            && !player.isInWater()
                            && !player.isInLava()
                            && player.fallDistance >= 2.0F) {
                        if (player.tickCount % 20 == 0 && !player.isCreative() && player.fallDistance > 3.0F) {
                            SEHelper.decreaseSouls(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.WindRobeSouls, 0));
                        }
                        if (worldIn instanceof ServerLevel serverLevel){
                            ColorUtil color = new ColorUtil(0xffffff);
                            ServerParticleUtil.windParticle(serverLevel, color, 1.0F + serverLevel.random.nextFloat() * 0.5F, 0.0F, player.getId(), player.position());
                            ServerParticleUtil.circularParticles(serverLevel, ParticleTypes.CLOUD, player, 1.0F);
                            if (CuriosFinder.hasCurio(player, ModItems.STORM_ROBE.get())) {
                                if (serverLevel.random.nextInt(20) == 0) {
                                    Vec3 vec3 = Vec3.atCenterOf(player.blockPosition());
                                    Vec3 vec31 = vec3.add(player.getRandom().nextDouble(), 1.0D, player.getRandom().nextDouble());
                                    ModNetwork.sendToALL(new SLightningPacket(vec3, vec31, 2));
                                }
                            }
                        }
                        if (!player.isCrouching() && !player.isShiftKeyDown()) {
                            player.setDeltaMovement(vector3d.multiply(1.0D, 0.875D, 1.0D));
                        } else {
                            player.setDeltaMovement(vector3d.multiply(1.0D, 0.99D, 1.0D));
                        }
                    }
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                        UUID uuid, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        if (IronLoaded.IRON_SPELLBOOKS.isLoaded()){
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.RobesIronResist, false)) {
                if (stack.is(ModItems.STORM_ROBE.get())){
                    map.put(IronAttributes.LIGHTNING_MAGIC_RESIST, new AttributeModifier(Goety.location("robes_iron_spell_resist"), 0.5F, AttributeModifier.Operation.ADD_VALUE));
                }
            }
        }
        return map;
    }
}