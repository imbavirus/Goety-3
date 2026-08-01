package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.compat.iron.IronAttributes;
import za.co.infernos.goety.compat.iron.IronLoaded;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.CuriosFinder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class UnholyRobeItem extends SingleStackItem{

    public UnholyRobeItem() {
        super(new Properties().fireResistant().stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity livingEntity) {
            if (CuriosFinder.hasCurio(livingEntity, this)) {
                if (livingEntity.isOnFire()){
                    livingEntity.clearFire();
                }
                if (!worldIn.isClientSide) {
                    if (livingEntity.hasEffect(GoetyEffects.BURN_HEX)){
                        livingEntity.removeEffect(GoetyEffects.BURN_HEX);
                    }
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                        UUID uuid, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        if (IronLoaded.IRON_SPELLBOOKS.isLoaded()){
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.RobesIronResist, false)) {
                map.put(IronAttributes.FIRE_MAGIC_RESIST, new AttributeModifier(Goety.location("robes_iron_fire_resist"), 1.0F, AttributeModifier.Operation.ADD_VALUE));
                map.put(IronAttributes.NATURE_MAGIC_RESIST, new AttributeModifier(Goety.location("robes_iron_nature_resist"), 0.5F, AttributeModifier.Operation.ADD_VALUE));
                map.put(IronAttributes.BLOOD_MAGIC_RESIST, new AttributeModifier(Goety.location("robes_iron_blood_resist"), 0.75F, AttributeModifier.Operation.ADD_VALUE));
                map.put(IronAttributes.HOLY_MAGIC_RESIST, new AttributeModifier(Goety.location("robes_iron_holy_resist"), -0.25F, AttributeModifier.Operation.ADD_VALUE));
            }
        }
        return map;
    }
}