package za.co.infernos.goetied.common.items.curios;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.effects.GoetiedEffects;
import za.co.infernos.goetied.compat.iron.IronAttributes;
import za.co.infernos.goetied.compat.iron.IronLoaded;
import za.co.infernos.goetied.config.MainConfig;
import za.co.infernos.goetied.utils.CuriosFinder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import java.util.UUID;

public class WildRobeItem extends SingleStackItem {

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isClientSide) {
            if (entityIn instanceof LivingEntity livingEntity) {
                if (CuriosFinder.hasWildRobe(livingEntity)){
                    if (livingEntity.hasEffect(MobEffects.POISON)){
                        livingEntity.removeEffect(MobEffects.POISON);
                    }
                    if (livingEntity.hasEffect(GoetiedEffects.ACID_VENOM)){
                        livingEntity.removeEffect(GoetiedEffects.ACID_VENOM);
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
                map.put(IronAttributes.NATURE_MAGIC_RESIST, new AttributeModifier(Goetied.location("robes_iron_spell_resist"), 0.5F, AttributeModifier.Operation.ADD_VALUE));
            }
        }
        return map;
    }
}