package za.co.infernos.goetied.common.items.armor;

import za.co.infernos.goetied.Goetied;
import za.co.infernos.goetied.api.items.IPersist;
import za.co.infernos.goetied.client.render.ModModelLayer;
import za.co.infernos.goetied.client.render.model.CursedKnightArmorModel;
import za.co.infernos.goetied.common.items.ModItems;
import za.co.infernos.goetied.config.ItemConfig;
import za.co.infernos.goetied.utils.ItemHelper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class CursedKnightArmor extends ArmorItem implements IPersist {
    public CursedKnightArmor(ArmorItem.Type p_40387_) {
        super(ModArmorMaterials.getCURSED_KNIGHT(), p_40387_,
                ModItems.baseProperties().durability(p_40387_.getDurability(
                        Math.max(1, za.co.infernos.goetied.utils.ConfigHelper.getInt(ItemConfig.CursedKnightDurability, 15)))));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.isDamaged(stack);
    }

    public int getBarColor(ItemStack stack) {
        if (this.isBroken(stack)) {
            return 0x800000;
        }
        return super.getBarColor(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack){
        if (this.isBroken(stack)) {
            return 13;
        }
        return super.getBarWidth(stack);
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {
        if (ItemHelper.armorSet(entity, this.getMaterial().value())){
            if (entity.getRandom().nextBoolean()){
                return 0;
            }
        }
        if (za.co.infernos.goetied.utils.ConfigHelper.getBoolean(ItemConfig.CursedKnightPersist, false)) {
            if (stack.getDamageValue() + amount >= stack.getMaxDamage()) {
                if (stack.getDamageValue() != stack.getMaxDamage() - 1) {
                    stack.setDamageValue(stack.getMaxDamage() - 1);
                    onBroken.accept(stack.getItem());
                }
                return 0;
            }
        }
        return amount;
    }

    @Override
    public boolean isBroken(ItemStack stack) {
        return IPersist.super.isBroken(stack) && za.co.infernos.goetied.utils.ConfigHelper.getBoolean(ItemConfig.CursedKnightPersist, false);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
        if (slot == EquipmentSlot.LEGS) {
            return Goetied.location("textures/models/armor/cursed_knight_armor_layer.png").toString();
        } else {
            return Goetied.location("textures/models/armor/cursed_knight_armor.png").toString();
        }
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
           public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
               EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
               ModelPart root = modelSet.bakeLayer(equipmentSlot == EquipmentSlot.LEGS ? ModModelLayer.CURSED_KNIGHT_ARMOR_INNER : ModModelLayer.CURSED_KNIGHT_ARMOR_OUTER);
               CursedKnightArmorModel model = new CursedKnightArmorModel(root);
               model.hat.visible = equipmentSlot == EquipmentSlot.HEAD;
               model.body.visible = equipmentSlot == EquipmentSlot.CHEST;
               model.rightArm.visible = equipmentSlot == EquipmentSlot.CHEST;
               model.leftArm.visible = equipmentSlot == EquipmentSlot.CHEST;
               model.rightLeg.visible = equipmentSlot == EquipmentSlot.FEET;
               model.leftLeg.visible = equipmentSlot == EquipmentSlot.FEET;

               model.young = original.young;
               model.crouching = original.crouching;
               model.riding = original.riding;
               model.rightArmPose = original.rightArmPose;
               model.leftArmPose = original.leftArmPose;

               return model;
           }
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        if (za.co.infernos.goetied.utils.ConfigHelper.getBoolean(ItemConfig.CursedKnightPersist, false) && this.isBroken(stack)) {
            tooltip.add(Component.translatable("info.goetied.armor.broken").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
