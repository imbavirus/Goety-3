package za.co.infernos.goety.init;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, Goety.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(Goety.MOD_ID,
            () -> CreativeModeTab.builder()
                    .icon(() -> ModItems.TOTEM_OF_SOULS.get().getDefaultInstance())
                    .title(Component.translatable("itemGroup.goety"))
                    .displayItems((parameters, output) -> {
                        for (DeferredHolder<Item, ? extends Item> holder : ModItems.ITEMS.getEntries()) {
                            Item item = holder.get();
                            if (!ModItems.shouldSkipCreativeModTab(item)) {
                                output.accept(item);
                            }
                        }
                    })
                    .build());
}
