package za.co.infernos.goety.client.inventory.container;

import za.co.infernos.goety.Goety;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neoforged.neoforge.registries.DeferredHolder;

public class ModContainerType {
        public static DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(Registries.MENU,
                        Goety.MOD_ID);

        public static final DeferredHolder<MenuType<?>, MenuType<SoulItemContainer>> WAND = CONTAINER_TYPE.register(
                        "wand",
                        () -> IMenuTypeExtension.create(SoulItemContainer::createContainerClientSide));

        public static final DeferredHolder<MenuType<?>, MenuType<FocusBagContainer>> FOCUS_BAG = CONTAINER_TYPE
                        .register("focus_bag",
                                        () -> IMenuTypeExtension.create(FocusBagContainer::createContainerClientSide));

        public static final DeferredHolder<MenuType<?>, MenuType<FocusPackContainer>> FOCUS_PACK = CONTAINER_TYPE
                        .register("focus_pack",
                                        () -> IMenuTypeExtension.create(FocusPackContainer::createContainerClientSide));

        public static final DeferredHolder<MenuType<?>, MenuType<BrewBagContainer>> BREW_BAG = CONTAINER_TYPE.register(
                        "brew_bag",
                        () -> IMenuTypeExtension.create(BrewBagContainer::createContainerClientSide));

        public static final DeferredHolder<MenuType<?>, MenuType<DarkAnvilMenu>> DARK_ANVIL = CONTAINER_TYPE.register(
                        "dark_anvil",
                        () -> IMenuTypeExtension.create(DarkAnvilMenu::new));

        public static final DeferredHolder<MenuType<?>, MenuType<CraftingFocusMenu>> CRAFTING_FOCUS = CONTAINER_TYPE
                        .register("crafting_focus",
                                        () -> IMenuTypeExtension.create(CraftingFocusMenu::new));
}
