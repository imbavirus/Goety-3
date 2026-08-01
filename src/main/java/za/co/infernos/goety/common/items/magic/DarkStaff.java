package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.api.magic.SpellType;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DarkStaff extends DarkWand {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public DarkStaff(Properties properties, double damage, double attackSpeed, SpellType spellType){
        super(properties, spellType);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE.value(), new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, damage - 1.0D, AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.ATTACK_SPEED.value(), new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE));
        this.defaultModifiers = builder.build();
    }

    public DarkStaff(Properties properties, double damage, SpellType spellType) {
        this(properties, damage, -2.4D, spellType);
    }

    public DarkStaff(double damage, double attackSpeed, SpellType spellType) {
        this(new Item.Properties().stacksTo(1), damage, attackSpeed, spellType);
    }

    public DarkStaff(double damage, SpellType spellType) {
        this(damage, -2.4D, spellType);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot pEquipmentSlot, ItemStack stack) {
        if (pEquipmentSlot == EquipmentSlot.MAINHAND){
            return this.defaultModifiers;
        }
        return this.defaultModifiers;
    }
}