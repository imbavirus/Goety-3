package za.co.infernos.goetied.common.items.curios;

import za.co.infernos.goetied.common.items.ModItems;
import za.co.infernos.goetied.config.ItemConfig;
import za.co.infernos.goetied.init.ModKeybindings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SingleStackItem extends Item implements ICurioItem {

    public SingleStackItem() {
        this(new Properties().stacksTo(1));
    }

    public SingleStackItem(Properties properties){
        super(properties);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        if (stack.is(ModItems.CRONE_HAT.get())){
            return true;
        }
        return super.hasCraftingRemainingItem(stack);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        if (itemStack.is(ModItems.CRONE_HAT.get())) {
            return itemStack.copy();
        }
        return super.getCraftingRemainingItem(itemStack);
    }

    public boolean isEnchantable(ItemStack pStack) {
        return pStack.getCount() == 1;
    }

    public int getEnchantmentValue() {
        return 15;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        if (stack.getItem() == ModItems.SPITEFUL_BELT.get()) {
            return false;
        }
        return false;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        ChatFormatting main = ChatFormatting.DARK_PURPLE;
        ChatFormatting secondary = ChatFormatting.BLUE;

        if (stack.getItem() instanceof SingleStackItem) {
            if (stack.is(ModItems.AMETHYST_NECKLACE.get())) {
                tooltip.add(Component.translatable("info.goetied.amethyst_necklace").withStyle(main));
            }
            if (stack.getItem() instanceof WitchHatItem) {
                tooltip.add(Component.translatable("info.goetied.witch_hat").withStyle(main));
                if (stack.is(ModItems.CRONE_HAT.get())){
                    tooltip.add(Component.translatable("info.goetied.crone_hat_potion").withStyle(secondary));
                } else {
                    tooltip.add(Component.translatable("info.goetied.witch_hat_potion").withStyle(secondary));
                }
            }
            if (stack.getItem() instanceof NecroGarbs.NecroCrownItem crownItem) {
                tooltip.add(Component.translatable("info.goetied.necro_crown").withStyle(main));
                if (!crownItem.isNameless) {
                    tooltip.add(Component.translatable("info.goetied.necro_crown_cast").withStyle(secondary));
                    if (za.co.infernos.goetied.utils.ConfigHelper.getBoolean(ItemConfig.NecroCrownWeakness, false)){
                        tooltip.add(Component.translatable("info.goetied.necro_crown_weakness").withStyle(secondary));
                    }
                } else {
                    tooltip.add(Component.translatable("info.goetied.dark_hat").withStyle(secondary));
                }
            }
            if (stack.getItem() instanceof MagicHatItem) {
                if (stack.is(ModItems.GRAND_TURBAN.get())) {
                    tooltip.add(Component.translatable("info.goetied.grand_turban").withStyle(main));
                }
                tooltip.add(Component.translatable("info.goetied.dark_hat").withStyle(secondary));
            }
            if (stack.is(ModItems.FROST_CROWN.get())){
                tooltip.add(Component.translatable("info.goetied.frost_crown").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.frost_crown_cast").withStyle(secondary));
            }
            if (stack.is(ModItems.WILD_CROWN.get())){
                tooltip.add(Component.translatable("info.goetied.wild_crown").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.wild_crown_cast").withStyle(secondary));
            }
            if (stack.is(ModItems.ABYSS_CROWN.get())){
                tooltip.add(Component.translatable("info.goetied.abyss_crown").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.abyss_crown_cast").withStyle(secondary));
            }
            if (stack.is(ModItems.VOID_CROWN.get())){
                tooltip.add(Component.translatable("info.goetied.void_crown").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.void_crown_cast").withStyle(secondary));
            }
            if (stack.is(ModItems.NETHER_CROWN.get())){
                tooltip.add(Component.translatable("info.goetied.nether_crown").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.nether_crown_cast").withStyle(secondary));
            }
            if (stack.getItem() instanceof UnholyHatItem){
                tooltip.add(Component.translatable("info.goetied.unholy_hat").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.unholy_hat_cast").withStyle(secondary));
            }
            if (stack.getItem() instanceof NecroGarbs.NecroCapeItem capeItem) {
                tooltip.add(Component.translatable("info.goetied.necro_cape").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.necro_cape_power").withStyle(secondary));
                if (!capeItem.isNameless) {
                    if (za.co.infernos.goetied.utils.ConfigHelper.getBoolean(ItemConfig.NecroCapeHunger, false)){
                        tooltip.add(Component.translatable("info.goetied.necro_cape_hunger").withStyle(secondary));
                    }
                }
            }
            if (stack.getItem() instanceof MagicRobeItem) {
                if (stack.is(ModItems.GRAND_ROBE.get())) {
                    tooltip.add(Component.translatable("info.goetied.grand_robe").withStyle(main));
                }
                tooltip.add(Component.translatable("info.goetied.dark_robe").withStyle(secondary));
            }
            if (stack.getItem() instanceof IllusionRobeItem) {
                tooltip.add(Component.translatable("info.goetied.illusion_robe").withStyle(secondary));
            }
            if (stack.getItem() instanceof FrostRobeItem) {
                if (za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.FrostRobeResistance, 0) > 0) {
                    tooltip.add(Component.translatable("info.goetied.frost_robe", za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.FrostRobeResistance, 0)).withStyle(main));
                }
                tooltip.add(Component.translatable("info.goetied.frost_robe_discount").withStyle(secondary));
            }
            if (stack.getItem() instanceof WindyRobeItem) {
                tooltip.add(Component.translatable("info.goetied.wind_robe").withStyle(main));
                if (stack.is(ModItems.WIND_ROBE.get())) {
                    tooltip.add(Component.translatable("info.goetied.wind_robe_discount").withStyle(secondary));
                }
                if (stack.is(ModItems.STORM_ROBE.get())) {
                    if (za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.StormRobeResistance, 0) > 0) {
                        tooltip.add(Component.translatable("info.goetied.storm_robe", za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.StormRobeResistance, 0)).withStyle(main));
                    }
                    tooltip.add(Component.translatable("info.goetied.storm_robe_discount").withStyle(secondary));
                }
            }
            if (stack.getItem() instanceof WildRobeItem) {
                tooltip.add(Component.translatable("info.goetied.wild_robe").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.wild_robe_discount").withStyle(secondary));
            }
            if (stack.getItem() instanceof AbyssRobeItem) {
                tooltip.add(Component.translatable("info.goetied.abyss_robe").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.abyss_robe_discount").withStyle(secondary));
            }
            if (stack.getItem() instanceof VoidRobeItem) {
                tooltip.add(Component.translatable("info.goetied.void_robe", za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.VoidRobeTeleportChance, 0)).withStyle(main));
                tooltip.add(Component.translatable("info.goetied.void_robe_discount").withStyle(secondary));
            }
            if (stack.getItem() instanceof WitchRobeItem) {
                tooltip.add(Component.translatable("info.goetied.witch_robe_brew", ModKeybindings.keyBindings[3].getTranslatedKeyMessage().getString()).withStyle(main));
                if (za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.WitchRobeResistance, 0) > 0) {
                    tooltip.add(Component.translatable("info.goetied.witch_robe", za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.WitchRobeResistance, 0)).withStyle(secondary));
                }
            }
            if (stack.getItem() instanceof WarlockGarmentItem) {
                tooltip.add(Component.translatable("info.goetied.warlock_garment").withStyle(main));
                if (stack.getItem() instanceof WarlockRobeItem && za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.WarlockRobeResistance, 0) > 0) {
                    tooltip.add(Component.translatable("info.goetied.warlock_robe", za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.WarlockRobeResistance, 0)).withStyle(secondary));
                }
            }
            if (stack.getItem() instanceof NetherRobeItem) {
                if (za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.NetherRobeResistance, 0) > 0) {
                    tooltip.add(Component.translatable("info.goetied.nether_robe", za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.NetherRobeResistance, 0)).withStyle(main));
                }
                tooltip.add(Component.translatable("info.goetied.nether_robe_discount").withStyle(secondary));
            }
            if (stack.getItem() instanceof UnholyRobeItem) {
                tooltip.add(Component.translatable("info.goetied.unholy_robe").withStyle(main));
                tooltip.add(Component.translatable("info.goetied.unholy_robe_discount").withStyle(secondary));
            }
            if (stack.is(ModItems.RING_OF_WANT.get())){
                tooltip.add(Component.translatable("info.goetied.ring_of_want").withStyle(secondary));
            }
            if (stack.is(ModItems.RING_OF_FORCE.get())){
                tooltip.add(Component.translatable("info.goetied.ring_of_force").withStyle(secondary));
            }
            if (stack.is(ModItems.RING_OF_THE_FORGE.get())){
                tooltip.add(Component.translatable("info.goetied.ring_of_the_forge").withStyle(secondary));
            }
            if (stack.is(ModItems.RING_OF_THE_DRAGON.get())){
                tooltip.add(Component.translatable("info.goetied.ring_of_the_dragon").withStyle(secondary));
            }
            if (stack.is(ModItems.GRAVE_GLOVE.get())) {
                tooltip.add(Component.translatable("info.goetied.grave_gloves").withStyle(secondary));
            }
            if (stack.is(ModItems.THRASH_GLOVE.get())) {
                tooltip.add(Component.translatable("info.goetied.thrash_gloves").withStyle(secondary));
            }
            if (stack.is(ModItems.WAYFARERS_BELT.get())) {
                tooltip.add(Component.translatable("info.goetied.wayfarers_belt").withStyle(secondary));
            }
            if (stack.is(ModItems.SPITEFUL_BELT.get())) {
                tooltip.add(Component.translatable("info.goetied.spiteful_belt").withStyle(secondary));
            }
            if (stack.is(ModItems.STAR_AMULET.get())) {
                tooltip.add(Component.translatable("info.goetied.star_amulet").withStyle(secondary));
            }
            if (stack.is(ModItems.ALARMING_CHARM.get())) {
                tooltip.add(Component.translatable("info.goetied.alarming_charm").withStyle(secondary));
            }
            if (stack.is(ModItems.OMINOUS_CHARM.get())) {
                tooltip.add(Component.translatable("info.goetied.ominous_charm", ModKeybindings.keyBindings[14].getTranslatedKeyMessage().getString()).withStyle(secondary));
                tooltip.add(Component.translatable("info.goetied.ominous_charm.level()").withStyle(ChatFormatting.DARK_AQUA).append(Component.literal(" " + OminousCharmItem.getOmenAmount(stack))));
            }
        }
    }
}
