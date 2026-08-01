package za.co.infernos.goety.common.items.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForgeMod;
import top.theillusivec4.curios.api.SlotContext;
import java.util.UUID;

public class WayfarersBeltItem extends SingleStackItem {

    @Override
    @SuppressWarnings("removal")
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                        UUID uuid, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        map.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath("goety", "wayfarer_speed"), 0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        map.put(Attributes.STEP_HEIGHT, new AttributeModifier(ResourceLocation.fromNamespaceAndPath("goety", "wayfarer_step_height"), 1.0625F, AttributeModifier.Operation.ADD_VALUE));
        map.put(NeoForgeMod.SWIM_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath("goety", "wayfarer_swim_speed"), 0.0175F, AttributeModifier.Operation.ADD_VALUE));
        return map;
    }
}