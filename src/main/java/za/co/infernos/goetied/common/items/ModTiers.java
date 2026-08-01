package za.co.infernos.goetied.common.items;

import za.co.infernos.goetied.config.ItemConfig;
import com.google.common.base.Suppliers;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum ModTiers implements Tier {
    SPECIAL(() -> getSpecialMiningLevel(),
            () -> getSpecialDurability(),
            () -> getSpecialBreakSpeed(),
            () -> getSpecialDamage(),
            () -> getSpecialEnchantability(), () -> {
        return Ingredient.of(ModItems.CURSED_METAL_INGOT.get());
    }),
    DARK(() -> getDarkMiningLevel(),
            () -> getDarkDurability(),
            () -> getDarkBreakSpeed(),
            () -> getDarkDamage(),
            () -> getDarkEnchantability(), () -> {
        return Ingredient.of(ModItems.DARK_ALLOY_INGOT.get());
    }),
    VOID(4,
            2031,
            0.0F,
            0.0F,
            15, () -> {
        return Ingredient.of(Items.ENDER_PEARL);
    }),
    DEATH(() -> 4,
            () -> getDeathScytheDurability(),
            () -> 12.0F,
            () -> getDeathScytheDamage(),
            () -> getDeathScytheEnchantability(), () -> {
        return Ingredient.of(Items.BONE);
    });

    private final Supplier<Integer> levelSupplier;
    private final Supplier<Integer> usesSupplier;
    private final Supplier<Float> speedSupplier;
    private final Supplier<Float> damageSupplier;
    private final Supplier<Integer> enchantmentValueSupplier;
    private final Supplier<Ingredient> repairIngredient;
    
    // Cached values (lazy evaluation)
    private Integer levelCache;
    private Integer usesCache;
    private Float speedCache;
    private Float damageCache;
    private Integer enchantmentValueCache;

    // Constructor for lazy config values
    ModTiers(Supplier<Integer> pLevel, Supplier<Integer> pUses, Supplier<Float> pSpeed, Supplier<Float> pDamage, Supplier<Integer> pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.levelSupplier = pLevel;
        this.usesSupplier = pUses;
        this.speedSupplier = pSpeed;
        this.damageSupplier = pDamage;
        this.enchantmentValueSupplier = pEnchantmentValue;
        this.repairIngredient = Suppliers.memoize(pRepairIngredient::get);
    }
    
    // Constructor for constant values (VOID tier)
    ModTiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.levelSupplier = () -> pLevel;
        this.usesSupplier = () -> pUses;
        this.speedSupplier = () -> pSpeed;
        this.damageSupplier = () -> pDamage;
        this.enchantmentValueSupplier = () -> pEnchantmentValue;
        this.repairIngredient = Suppliers.memoize(pRepairIngredient::get);
        // Pre-cache constant values
        this.levelCache = pLevel;
        this.usesCache = pUses;
        this.speedCache = pSpeed;
        this.damageCache = pDamage;
        this.enchantmentValueCache = pEnchantmentValue;
    }
    
    // Lazy getters — always pass the real default, and reject <=0 for durability/levels
    // so empty/broken configs cannot produce one-hit-break tools (#11).
    private static int getSpecialMiningLevel() {
        return za.co.infernos.goetied.utils.ConfigHelper.getPositiveInt(ItemConfig.SpecialToolsMiningLevel, 2);
    }

    private static int getSpecialDurability() {
        return za.co.infernos.goetied.utils.ConfigHelper.getPositiveInt(ItemConfig.SpecialToolsDurability, 500);
    }

    private static float getSpecialBreakSpeed() {
        float value = za.co.infernos.goetied.utils.ConfigHelper.getFloat(ItemConfig.SpecialToolsBreakSpeed, 6.0F);
        return value > 0.0F ? value : 6.0F;
    }

    private static float getSpecialDamage() {
        float value = za.co.infernos.goetied.utils.ConfigHelper.getFloat(ItemConfig.SpecialToolsDamage, 2.0F);
        return value > 0.0F ? value : 2.0F;
    }

    private static int getSpecialEnchantability() {
        return za.co.infernos.goetied.utils.ConfigHelper.getPositiveInt(ItemConfig.SpecialToolsEnchantability, 14);
    }

    private static int getDarkMiningLevel() {
        return za.co.infernos.goetied.utils.ConfigHelper.getPositiveInt(ItemConfig.DarkToolsMiningLevel, 3);
    }

    private static int getDarkDurability() {
        return za.co.infernos.goetied.utils.ConfigHelper.getPositiveInt(ItemConfig.DarkToolsDurability, 1000);
    }

    private static float getDarkBreakSpeed() {
        float value = za.co.infernos.goetied.utils.ConfigHelper.getFloat(ItemConfig.DarkToolsBreakSpeed, 8.0F);
        return value > 0.0F ? value : 8.0F;
    }

    private static float getDarkDamage() {
        float value = za.co.infernos.goetied.utils.ConfigHelper.getFloat(ItemConfig.DarkToolsDamage, 3.0F);
        return value > 0.0F ? value : 3.0F;
    }

    private static int getDarkEnchantability() {
        return za.co.infernos.goetied.utils.ConfigHelper.getPositiveInt(ItemConfig.DarkToolsEnchantability, 15);
    }

    private static int getDeathScytheDurability() {
        return za.co.infernos.goetied.utils.ConfigHelper.getPositiveInt(ItemConfig.DeathScytheDurability, 2031);
    }

    private static float getDeathScytheDamage() {
        float value = za.co.infernos.goetied.utils.ConfigHelper.getFloat(ItemConfig.DeathScytheDamage, 4.0F);
        return value > 0.0F ? value : 4.0F;
    }

    private static int getDeathScytheEnchantability() {
        return za.co.infernos.goetied.utils.ConfigHelper.getPositiveInt(ItemConfig.DeathScytheEnchantability, 15);
    }

    public int getUses() {
        if (usesCache == null) {
            usesCache = usesSupplier.get();
        }
        return usesCache;
    }

    public float getSpeed() {
        if (speedCache == null) {
            speedCache = speedSupplier.get();
        }
        return speedCache;
    }

    public float getAttackDamageBonus() {
        if (damageCache == null) {
            damageCache = damageSupplier.get();
        }
        return damageCache;
    }

    public int getLevel() {
        if (levelCache == null) {
            levelCache = levelSupplier.get();
        }
        return levelCache;
    }

    public int getEnchantmentValue() {
        if (enchantmentValueCache == null) {
            enchantmentValueCache = enchantmentValueSupplier.get();
        }
        return enchantmentValueCache;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return BlockTags.INCORRECT_FOR_IRON_TOOL;
    }

}
