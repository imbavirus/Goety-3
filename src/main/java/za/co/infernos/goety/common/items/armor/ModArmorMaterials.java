package za.co.infernos.goety.common.items.armor;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.compat.fml.FMLJavaModLoadingContext;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.utils.ConfigHelper;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

/**
 * Registered Goety armor materials. Must not fall back to vanilla leather — that made
 * {@code getMaterial() == BLACK_IRON/DARK} match every leather piece and cancel damage.
 */
public final class ModArmorMaterials {
    private ModArmorMaterials() {
    }

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, Goety.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CURSED_KNIGHT = ARMOR_MATERIALS.register(
            "cursed_knight",
            () -> create(
                    "cursed_knight",
                    ConfigHelper.getInt(ItemConfig.CursedKnightFeet, 2),
                    ConfigHelper.getInt(ItemConfig.CursedKnightLegs, 5),
                    ConfigHelper.getInt(ItemConfig.CursedKnightChest, 6),
                    ConfigHelper.getInt(ItemConfig.CursedKnightHead, 2),
                    ConfigHelper.getInt(ItemConfig.CursedKnightEnchantability, 15),
                    SoundEvents.ARMOR_EQUIP_IRON,
                    ConfigHelper.getFloat(ItemConfig.CursedKnightToughness, 0.5F),
                    ConfigHelper.getFloat(ItemConfig.CursedKnightKnockResist, 0.0F),
                    () -> Ingredient.of(ModItems.CURSED_METAL_INGOT.get())));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CURSED_PALADIN = ARMOR_MATERIALS.register(
            "cursed_paladin",
            () -> create(
                    "cursed_paladin",
                    ConfigHelper.getInt(ItemConfig.CursedPaladinFeet, 3),
                    ConfigHelper.getInt(ItemConfig.CursedPaladinLegs, 6),
                    ConfigHelper.getInt(ItemConfig.CursedPaladinChest, 7),
                    ConfigHelper.getInt(ItemConfig.CursedPaladinHead, 3),
                    ConfigHelper.getInt(ItemConfig.CursedPaladinEnchantability, 20),
                    SoundEvents.ARMOR_EQUIP_IRON,
                    ConfigHelper.getFloat(ItemConfig.CursedPaladinToughness, 1.0F),
                    ConfigHelper.getFloat(ItemConfig.CursedPaladinKnockResist, 0.0F),
                    () -> Ingredient.of(ModItems.CURSED_METAL_INGOT.get())));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BLACK_IRON = ARMOR_MATERIALS.register(
            "black_iron",
            () -> create(
                    "black_iron",
                    ConfigHelper.getInt(ItemConfig.BlackIronFeet, 2),
                    ConfigHelper.getInt(ItemConfig.BlackIronLegs, 5),
                    ConfigHelper.getInt(ItemConfig.BlackIronChest, 6),
                    ConfigHelper.getInt(ItemConfig.BlackIronHead, 2),
                    ConfigHelper.getInt(ItemConfig.BlackIronEnchantability, 15),
                    SoundEvents.ARMOR_EQUIP_IRON,
                    ConfigHelper.getFloat(ItemConfig.BlackIronToughness, 2.0F),
                    ConfigHelper.getFloat(ItemConfig.BlackIronKnockResist, 0.0F),
                    () -> Ingredient.of(ModItems.CURSED_METAL_INGOT.get())));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> DARK = ARMOR_MATERIALS.register(
            "dark",
            () -> create(
                    "dark",
                    ConfigHelper.getInt(ItemConfig.DarkArmorFeet, 3),
                    ConfigHelper.getInt(ItemConfig.DarkArmorLegs, 6),
                    ConfigHelper.getInt(ItemConfig.DarkArmorChest, 8),
                    ConfigHelper.getInt(ItemConfig.DarkArmorHead, 3),
                    ConfigHelper.getInt(ItemConfig.DarkArmorEnchantability, 20),
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    ConfigHelper.getFloat(ItemConfig.DarkArmorToughness, 2.0F),
                    ConfigHelper.getFloat(ItemConfig.DarkArmorKnockResist, 0.3F),
                    () -> Ingredient.of(ModItems.DARK_ALLOY_INGOT.get())));

    public static void init() {
        ARMOR_MATERIALS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static Holder<ArmorMaterial> getCURSED_KNIGHT() {
        return CURSED_KNIGHT;
    }

    public static Holder<ArmorMaterial> getCURSED_PALADIN() {
        return CURSED_PALADIN;
    }

    public static Holder<ArmorMaterial> getBLACK_IRON() {
        return BLACK_IRON;
    }

    public static Holder<ArmorMaterial> getDARK() {
        return DARK;
    }

    public static boolean isBlackIronOrDark(Holder<ArmorMaterial> material) {
        return material != null && (material.is(BLACK_IRON) || material.is(DARK));
    }

    private static ArmorMaterial create(
            String name,
            int boots,
            int leggings,
            int chestplate,
            int helmet,
            int enchantability,
            Holder<net.minecraft.sounds.SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            java.util.function.Supplier<Ingredient> repair) {
        EnumMap<ArmorItem.Type, Integer> defense = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
            map.put(ArmorItem.Type.BOOTS, Math.max(0, boots));
            map.put(ArmorItem.Type.LEGGINGS, Math.max(0, leggings));
            map.put(ArmorItem.Type.CHESTPLATE, Math.max(0, chestplate));
            map.put(ArmorItem.Type.HELMET, Math.max(0, helmet));
            map.put(ArmorItem.Type.BODY, Math.max(0, chestplate));
        });
        return new ArmorMaterial(
                defense,
                Math.max(0, enchantability),
                equipSound,
                repair,
                List.of(new ArmorMaterial.Layer(Goety.location(name))),
                Math.max(0.0F, toughness),
                Math.max(0.0F, knockbackResistance));
    }
}
