package za.co.infernos.goety.common.items.curios;

import za.co.infernos.goety.compat.iron.IronAttributes;
import za.co.infernos.goety.compat.iron.IronLoaded;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.MobUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class NecroGarbs extends SingleStackItem {
    public boolean isNameless;

    public NecroGarbs(boolean isNameless){
        super();
        this.isNameless = isNameless;
    }

    public static class NecroCrownItem extends NecroGarbs {
        public NecroCrownItem(boolean isNameless) {
            super(isNameless);
        }

        public NecroCrownItem() {
            super(false);
        }

        @Override
        public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
            if (!worldIn.isClientSide) {
                if (entityIn instanceof LivingEntity livingEntity) {
                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(ItemConfig.NecroCrownWeakness, false)) {
                        if (!this.isNameless) {
                            if (CuriosFinder.hasCurio(livingEntity, this)) {
                                if (MobUtil.isInSunlightNoRain(livingEntity)) {
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, false, false));
                                }
                            }
                        }
                    }
                }
            }

            super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        }
    }

    public static class NecroCapeItem extends NecroGarbs {
        public NecroCapeItem(boolean isNameless) {
            super(isNameless);
        }

        @Override
        public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
            if (!worldIn.isClientSide) {
                if (entityIn instanceof LivingEntity livingEntity) {
                    if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(ItemConfig.NecroCapeHunger, false)) {
                        if (!this.isNameless) {
                            if (CuriosFinder.hasCurio(livingEntity, this)) {
                                if (MobUtil.isInSunlightNoRain(livingEntity)) {
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 2, false, false));
                                }
                            }
                        }
                    }
                }
            }

            super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        }

    @Override
        public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                            UUID uuid, ItemStack stack) {
            Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
            if (IronLoaded.IRON_SPELLBOOKS.isLoaded()){
                if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.RobesIronResist, false)) {
                    map.put(IronAttributes.BLOOD_MAGIC_RESIST, new AttributeModifier(za.co.infernos.goety.Goety.location("robes_iron_spell_resist"), 0.25F, AttributeModifier.Operation.ADD_VALUE));
                }
            }
            return map;
        }
    }
}