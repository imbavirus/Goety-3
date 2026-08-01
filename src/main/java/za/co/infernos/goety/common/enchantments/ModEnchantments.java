package za.co.infernos.goety.common.enchantments;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.config.SpellConfig;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, Goety.MOD_ID);

    private static Enchantment simple(ResourceLocation id,
                                      HolderSet<Item> supportedItems,
                                      int weight,
                                      int maxLevel,
                                      int minCostBase,
                                      int minCostPerLevel,
                                      int maxCostBase,
                                      int maxCostPerLevel,
                                      int anvilCost,
                                      EquipmentSlotGroup... slots) {
        Enchantment.EnchantmentDefinition def = Enchantment.definition(
                supportedItems,
                weight,
                maxLevel,
                Enchantment.dynamicCost(minCostBase, minCostPerLevel),
                Enchantment.dynamicCost(maxCostBase, maxCostPerLevel),
                anvilCost,
                slots
        );
        return Enchantment.enchantment(def).build(id);
    }

    private static HolderSet<Item> tag(net.minecraft.tags.TagKey<Item> tag) {
        return BuiltInRegistries.ITEM.getOrCreateTag(tag);
    }

    public static final DeferredHolder<Enchantment, Enchantment> SOUL_EATER = ENCHANTMENTS.register("soul_eater",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 5, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxSoulEaterLevel, 0), 5, 9, 20, 9, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> WANTING = ENCHANTMENTS.register("wanting",
            id -> simple(id, tag(ItemTags.DURABILITY_ENCHANTABLE), 2, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxWantingLevel, 0), 1, 10, 16, 10, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> POTENCY = ENCHANTMENTS.register("potency",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 2, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxPotencyLevel, 0), 15, 9, 65, 9, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> RADIUS = ENCHANTMENTS.register("radius",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 2, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxRadiusLevel, 0), 12, 20, 37, 20, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> BURNING = ENCHANTMENTS.register("burning",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 1, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxBurningLevel, 0), 10, 10, 30, 10, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> RANGE = ENCHANTMENTS.register("range",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 10, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxRangeLevel, 0), 1, 10, 16, 10, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> ABSORB = ENCHANTMENTS.register("absorb",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 1, 1, 25, 25, 75, 25, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> MAGNET = ENCHANTMENTS.register("magnet",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 1, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxMagnetLevel, 0), 10, 8, 30, 8, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> DURATION = ENCHANTMENTS.register("duration",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 5, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxDurationLevel, 0), 10, 8, 30, 8, 1, EquipmentSlotGroup.MAINHAND));

    public static final DeferredHolder<Enchantment, Enchantment> VELOCITY = ENCHANTMENTS.register("velocity",
            id -> simple(id, tag(ItemTags.WEAPON_ENCHANTABLE), 5, za.co.infernos.goety.utils.ConfigHelper.getInt(SpellConfig.MaxVelocityLevel, 0), 10, 8, 30, 8, 1, EquipmentSlotGroup.MAINHAND));
}