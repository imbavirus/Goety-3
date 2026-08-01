package za.co.infernos.goetied.utils;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Helper utility for safely accessing config values with default fallbacks.
 * This prevents IllegalStateException when config is accessed before it's loaded,
 * and treats non-positive durability/level values as unloaded/broken config.
 */
public class ConfigHelper {

    public static double getDouble(ModConfigSpec.ConfigValue<Double> configValue, double defaultValue) {
        try {
            Double value = configValue.get();
            return value != null ? value : defaultValue;
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }

    /**
     * Integer config with fallback. If {@code rejectNonPositive} semantics are needed for
     * durability, use {@link #getPositiveInt}.
     */
    public static int getInt(ModConfigSpec.ConfigValue<Integer> configValue, int defaultValue) {
        try {
            Integer value = configValue.get();
            return value != null ? value : defaultValue;
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }

    /**
     * Like {@link #getInt}, but any loaded value {@code <= 0} is treated as invalid and replaced
     * with {@code defaultValue}. Use for durability, mining level, enchantability multipliers, etc.
     */
    public static int getPositiveInt(ModConfigSpec.ConfigValue<Integer> configValue, int defaultValue) {
        int value = getInt(configValue, defaultValue);
        return value > 0 ? value : defaultValue;
    }

    public static boolean getBoolean(ModConfigSpec.ConfigValue<Boolean> configValue, boolean defaultValue) {
        try {
            Boolean value = configValue.get();
            return value != null ? value : defaultValue;
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }

    public static float getFloat(ModConfigSpec.ConfigValue<Double> configValue, float defaultValue) {
        try {
            Double value = configValue.get();
            return value != null ? value.floatValue() : defaultValue;
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }

    public static float getFloatFromDouble(double value) {
        return (float) value;
    }

    @SuppressWarnings("unchecked")
    public static <T> java.util.List<T> getList(ModConfigSpec.ConfigValue<? extends java.util.List<? extends T>> configValue, java.util.List<T> defaultValue) {
        try {
            return (java.util.List<T>) configValue.get();
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }
}
