package za.co.infernos.goety.common.items;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.effects.GoetyEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import za.co.infernos.goety.compat.fml.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, Goety.MOD_ID);

    public static void init() {
        ModPotions.POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredHolder<Potion, Potion> CLIMBING = POTIONS.register("climbing",
            () -> new Potion(new MobEffectInstance(GoetyEffects.CLIMBING, 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_CLIMBING = POTIONS.register("long_climbing",
            () -> new Potion("climbing", new MobEffectInstance(GoetyEffects.CLIMBING, 9600)));
}