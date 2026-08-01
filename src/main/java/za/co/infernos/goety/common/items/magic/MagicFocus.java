package za.co.infernos.goety.common.items.magic;

import za.co.infernos.goety.api.items.magic.IFocus;
import za.co.infernos.goety.api.magic.ISpell;
import za.co.infernos.goety.utils.ItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.List;

public class MagicFocus extends Item implements IFocus {
    public ISpell spell;
    public int soulCost;

    public MagicFocus(ISpell spell){
        super(new Properties()
                .rarity(Rarity.UNCOMMON)
                .setNoRepair()
                .stacksTo(1)
        );
        this.spell = spell;
        // Lazy evaluation to avoid accessing config before it's loaded
        this.soulCost = 0; // Will be set lazily when needed
    }

    // Lazy getter for soul cost
    public int getSoulCost() {
        if (this.soulCost == 0 && this.spell != null) {
            this.soulCost = this.spell.defaultSoulCost();
        }
        return this.soulCost;
    }

    public boolean isEnchantable(ItemStack pStack) {
        return pStack.getCount() == 1;
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 1;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (stack.getItem() instanceof MagicFocus magicFocus){
            if (magicFocus.getSpell() != null){
                if (!magicFocus.getSpell().acceptedEnchantments().isEmpty()){
                    return magicFocus.getSpell().acceptedEnchantments().contains(enchantment);
                }
            }
        }
        return false;
    }

    public ISpell getSpell(){
        return this.spell;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        int cost = getSoulCost();
        tooltip.add(Component.translatable("info.goety.focus.cost", cost));
        tooltip.add(Component.translatable("info.goety.focus.spellType", spell.getSpellType().getName()));
        tooltip.add(Component.translatable("item.goety.focus.info").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.UNDERLINE));
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable(this.getDescriptionId() + ".info").withStyle(ChatFormatting.GRAY));
    }

}

