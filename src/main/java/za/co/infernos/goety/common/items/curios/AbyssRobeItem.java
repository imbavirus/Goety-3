package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.compat.iron.IronAttributes;
import za.co.infernos.goety.compat.iron.IronLoaded;
import za.co.infernos.goety.config.MainConfig;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import za.co.infernos.goety.Goety;
import java.util.UUID;

public class AbyssRobeItem extends SingleStackItem {

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                        UUID uuid, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        map.put(NeoForgeMod.SWIM_SPEED, new AttributeModifier(Goety.location("abyss_robe_buff"), 0.5F, AttributeModifier.Operation.ADD_VALUE));
        if (IronLoaded.IRON_SPELLBOOKS.isLoaded()){
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.RobesIronResist, false)) {
                map.put(IronAttributes.LIGHTNING_MAGIC_RESIST, new AttributeModifier(Goety.location("robes_iron_spell_resist"), -0.25F, AttributeModifier.Operation.ADD_VALUE));
            }
        }
        return map;
    }
}