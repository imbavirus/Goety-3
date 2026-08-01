package za.co.infernos.goetied.common.items.curios;

import za.co.infernos.goetied.common.effects.GoetiedEffects;
import za.co.infernos.goetied.compat.iron.IronAttributes;
import za.co.infernos.goetied.compat.iron.IronLoaded;
import za.co.infernos.goetied.config.MainConfig;
import za.co.infernos.goetied.utils.CuriosFinder;
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

import za.co.infernos.goetied.Goetied;
import java.util.UUID;

public class FrostRobeItem extends SingleStackItem{

    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return stack.getItem() instanceof FrostRobeItem;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity livingEntity) {
            if (CuriosFinder.hasCurio(livingEntity, this)){
                livingEntity.setTicksFrozen(0);
                livingEntity.setIsInPowderSnow(false);
                if (!worldIn.isClientSide) {
                    if (livingEntity.hasEffect(GoetiedEffects.FREEZING)){
                        livingEntity.removeEffect(GoetiedEffects.FREEZING);
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
            if (za.co.infernos.goetied.utils.ConfigHelper.getBoolean(MainConfig.RobesIronResist, false)) {
                map.put(IronAttributes.ICE_MAGIC_RESIST, new AttributeModifier(Goetied.location("robes_iron_spell_resist"), 0.5F, AttributeModifier.Operation.ADD_VALUE));
            }
        }
        return map;
    }
}