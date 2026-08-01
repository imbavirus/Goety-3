package za.co.infernos.goety.utils;

import net.minecraft.util.RandomSource;

public class RandomUtil {

    public static int nextInt(RandomSource source, int chance) {
        if (chance <= 0) {
            chance = 1;
        }
        return source.nextInt(chance);
    }
}