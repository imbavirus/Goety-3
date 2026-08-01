package za.co.infernos.goetied.init;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.common.loot.AddItemLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Goetied.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddItemLootModifier>> ADD_LOOT_MODIFIER =
            GLOBAL_LOOT_MODIFIER.register("add_loot", () -> AddItemLootModifier.CODEC);
}
