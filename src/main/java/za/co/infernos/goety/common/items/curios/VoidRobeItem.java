package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.compat.iron.IronAttributes;
import za.co.infernos.goety.compat.iron.IronLoaded;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.CuriosFinder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import java.util.UUID;

public class VoidRobeItem extends SingleStackItem {

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isClientSide) {
            if (entityIn instanceof LivingEntity livingEntity) {
                if (CuriosFinder.hasVoidRobe(livingEntity)){
                    if (livingEntity.hasEffect(GoetyEffects.VOID_TOUCHED)){
                        livingEntity.removeEffect(GoetyEffects.VOID_TOUCHED);
                    }
                    if (za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.VoidRobeWaterSapped, 0) > 0) {
                        if (livingEntity.isInWaterRainOrBubble()) {
                            livingEntity.addEffect(new MobEffectInstance(GoetyEffects.SAPPED, 10, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.VoidRobeWaterSapped, 0) - 1, false, false));
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
                map.put(IronAttributes.ENDER_MAGIC_RESIST, new AttributeModifier(Goety.location("robes_iron_spell_resist"), 0.5F, AttributeModifier.Operation.ADD_VALUE));
            }
        }
        return map;
    }
}