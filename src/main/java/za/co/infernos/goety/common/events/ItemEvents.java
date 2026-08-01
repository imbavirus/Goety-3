package za.co.infernos.goety.common.events;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.api.items.ISoulRepair;
import za.co.infernos.goety.api.items.magic.IWand;
import za.co.infernos.goety.client.particles.ShockwaveParticleOption;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.common.items.ModItems;
import za.co.infernos.goety.common.items.ModTiers;
import za.co.infernos.goety.common.items.armor.ModArmorMaterials;
import za.co.infernos.goety.common.items.brew.BrewItem;
import za.co.infernos.goety.common.items.curios.WitchHatItem;
import za.co.infernos.goety.common.items.equipment.*;
import za.co.infernos.goety.common.items.magic.DarkStaff;
import za.co.infernos.goety.common.items.revive.ReviveServantItem;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.config.MainConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.init.ModTags;
import za.co.infernos.goety.utils.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import za.co.infernos.goety.utils.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = Goety.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ItemEvents {

    @SubscribeEvent
    public static void PlayerTick(PlayerTickEvent.Post event){
        Player player = event.getEntity();
        if (ItemHelper.findHelmet(player, ModItems.DARK_HELMET.get())){
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(ItemConfig.DarkHelmetDarkness, false)) {
                if (player.getEffect(MobEffects.DARKNESS) != null) {
                    player.removeEffect(MobEffects.DARKNESS);
                }
            }
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(ItemConfig.DarkHelmetBlindness, false)) {
                if (player.getEffect(MobEffects.BLINDNESS) != null) {
                    player.removeEffect(MobEffects.BLINDNESS);
                }
            }
        }

        Inventory inventory = player.getInventory();

        List<NonNullList<ItemStack>> compartments = ImmutableList.of(inventory.items, inventory.armor, inventory.offhand);

        for (NonNullList<ItemStack> nonnulllist : compartments) {
            for (int i = 0; i < nonnulllist.size(); ++i) {
                if (!nonnulllist.get(i).isEmpty()) {
                    ItemStack itemStack = nonnulllist.get(i);
                    if (itemStack.getItem() instanceof ISoulRepair soulRepair) {
                        soulRepair.repairTick(nonnulllist.get(i), player, inventory.selected == i);
                    } else if (itemStack.getItem() instanceof TieredItem item && item.getTier() == ModTiers.DARK){
                        ItemHelper.repairTick(itemStack, player, inventory.selected == i);
                    }
                }
            }
        }

        if (ModArmorMaterials.DARK != null && ItemHelper.armorSet(player, ModArmorMaterials.DARK.value())){
            if (player.getFoodData().needsFood()){
                if (player.tickCount % 40 == 0){
                    player.heal(1.0F);
                }
            }
        }

        AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
        boolean scythe = player.getMainHandItem().getItem() instanceof DarkScytheItem;

        float increaseAttackSpeed0 = 0.25F;
        AttributeModifier attributemodifier0 = new AttributeModifier(Goety.location("item_modifiers/two_handed_scythe"), (double)increaseAttackSpeed0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        boolean flag0 = scythe && player.getOffhandItem().isEmpty();
        if (attackSpeed != null){
            if (flag0){
                if (!attackSpeed.hasModifier(attributemodifier0.id())){
                    attackSpeed.addPermanentModifier(attributemodifier0);
                }
            } else {
                if (attackSpeed.hasModifier(attributemodifier0.id())){
                    attackSpeed.removeModifier(attributemodifier0.id());
                }
            }
        }

        float increaseAttackSpeed = 0.5F;
        AttributeModifier attributemodifier = new AttributeModifier(Goety.location("item_modifiers/scythe_proficiency"), (double)increaseAttackSpeed, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        boolean flag = CuriosFinder.hasCurio(player, ModItems.GRAVE_GLOVE.get()) && (scythe || player.getMainHandItem().is(ModTags.Items.GRAVE_GLOVE_BOOST));
        if (attackSpeed != null){
            if (flag){
                if (!attackSpeed.hasModifier(attributemodifier.id())){
                    attackSpeed.addPermanentModifier(attributemodifier);
                }
            } else {
                if (attackSpeed.hasModifier(attributemodifier.id())){
                    attackSpeed.removeModifier(attributemodifier.id());
                }
            }
        }

        boolean hammer = player.getMainHandItem().getItem() instanceof HammerItem;

        float increaseAttackSpeed1 = 0.25F;
        AttributeModifier attributemodifier1 = new AttributeModifier(Goety.location("item_modifiers/two_handed_hammer"), (double)increaseAttackSpeed1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        boolean flag1 = hammer && player.getOffhandItem().isEmpty();
        if (attackSpeed != null){
            if (flag1){
                if (!attackSpeed.hasModifier(attributemodifier1.id())){
                    attackSpeed.addPermanentModifier(attributemodifier1);
                }
            } else {
                if (attackSpeed.hasModifier(attributemodifier1.id())){
                    attackSpeed.removeModifier(attributemodifier1.id());
                }
            }
        }

        float increaseAttackSpeed2 = 0.5F;
        AttributeModifier attributemodifier2 = new AttributeModifier(Goety.location("item_modifiers/hammer_proficiency"), (double)increaseAttackSpeed2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        boolean flag2 = CuriosFinder.hasCurio(player, ModItems.THRASH_GLOVE.get()) && (hammer || player.getMainHandItem().is(ModTags.Items.THRASH_GLOVE_BOOST));
        if (attackSpeed != null){
            if (flag2){
                if (!attackSpeed.hasModifier(attributemodifier2.id())){
                    attackSpeed.addPermanentModifier(attributemodifier2);
                }
            } else {
                if (attackSpeed.hasModifier(attributemodifier2.id())){
                    attackSpeed.removeModifier(attributemodifier2.id());
                }
            }
        }

        boolean staff = player.getOffhandItem().getItem() instanceof DarkStaff && za.co.infernos.goety.utils.ConfigHelper.getBoolean(ItemConfig.StaffOffhandBuff, false);

        AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);

        AttributeModifier attributemodifier3 = new AttributeModifier(Goety.location("item_modifiers/dark_staff_proficiency"), 0.25D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        if (attackDamage != null){
            if (staff){
                if (!attackDamage.hasModifier(attributemodifier3.id())){
                    attackDamage.addPermanentModifier(attributemodifier3);
                }
            } else {
                if (attackDamage.hasModifier(attributemodifier3.id())){
                    attackDamage.removeModifier(attributemodifier3.id());
                }
            }
        }
        if (MobUtil.starAmuletActive(player)){
            player.getAbilities().flying &= player.isCreative();
        }
    }

    @SubscribeEvent
    public static void LivingEffects(EntityTickEvent.Post event){
        // Removed ticking armor set modifier logic
    }

    @SubscribeEvent
    public static void ArmorSetEvents(LivingEquipmentChangeEvent event){
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity != null && livingEntity.isAlive()){
            if (event.getSlot().isArmor()) {
                updateArmorSetBonus(livingEntity);
            }
        }
    }

    @SubscribeEvent
    public static void EntityJoinEvents(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof LivingEntity livingEntity){
            updateArmorSetBonus(livingEntity);
        }
    }

    private static void updateArmorSetBonus(LivingEntity livingEntity) {
        AttributeModifier attributemodifier = new AttributeModifier(Goety.location("item_modifiers/increase_armor"), 4.0D, AttributeModifier.Operation.ADD_VALUE);
        AttributeInstance armor = livingEntity.getAttribute(Attributes.ARMOR);
        AttributeModifier attributemodifier1 = new AttributeModifier(Goety.location("item_modifiers/increase_toughness"), 4.0D, AttributeModifier.Operation.ADD_VALUE);
        AttributeInstance toughness = livingEntity.getAttribute(Attributes.ARMOR_TOUGHNESS);

        if (armor != null) {
            if ((ModArmorMaterials.CURSED_KNIGHT != null && ItemHelper.armorSet(livingEntity, ModArmorMaterials.CURSED_KNIGHT.value())) ||
                    (ModArmorMaterials.CURSED_PALADIN != null && ItemHelper.armorSet(livingEntity, ModArmorMaterials.CURSED_PALADIN.value()))) {
                if (!armor.hasModifier(attributemodifier.id())) {
                    armor.addPermanentModifier(attributemodifier);
                }
            } else {
                if (armor.hasModifier(attributemodifier.id())) {
                    armor.removeModifier(attributemodifier.id());
                }
            }
        }
        if (toughness != null) {
            if (ModArmorMaterials.CURSED_PALADIN != null && ItemHelper.armorSet(livingEntity, ModArmorMaterials.CURSED_PALADIN.value())) {
                if (!toughness.hasModifier(attributemodifier1.id())) {
                    toughness.addPermanentModifier(attributemodifier1);
                }
            } else {
                if (toughness.hasModifier(attributemodifier1.id())) {
                    toughness.removeModifier(attributemodifier1.id());
                }
            }
        }
    }



    @SubscribeEvent
    public static void HurtEvent(LivingDamageEvent.Post event){
        LivingEntity victim = event.getEntity();
        Entity directEntity = event.getSource().getDirectEntity();
        if (event.getNewDamage() > 0.0F) {
            if (directEntity instanceof LivingEntity livingAttacker) {
                if (ModDamageSource.physicalAttacks(event.getSource())) {
                    ItemHelper.setItemEffect(livingAttacker.getMainHandItem(), victim);
                    if (livingAttacker.getMainHandItem().getItem() instanceof TieredItem weapon) {
                        if (weapon == ModItems.FANGED_DAGGER.get()){
                            Holder<MobEffect> effect = MobEffects.POISON;
                            if (CuriosFinder.hasWildRobe(livingAttacker)){
                                effect = GoetyEffects.ACID_VENOM;
                            }
                            if (livingAttacker.hasEffect(GoetyEffects.VENOMOUS_HANDS)){
                                EffectsUtil.increaseDuration(victim, effect.value(), 600);
                            } else {
                                victim.addEffect(new MobEffectInstance(effect, 200));
                            }
                        }
                        if (weapon == ModItems.HUNGRY_DAGGER.get()){
                            int soulEat = livingAttacker.getMainHandItem().getEnchantmentLevel(livingAttacker.level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT).getOrThrow(ModEnchantments.SOUL_EATER.getKey()));
                            livingAttacker.heal(event.getNewDamage() * (0.05F * (soulEat + 1)));
                        }
                        if (weapon instanceof BladeOfEnderItem) {
                            Holder<MobEffect> effect = GoetyEffects.VOID_TOUCHED;
                            int amp = 0;
                            if (livingAttacker instanceof Player player) {
                                if (!player.isSpectator()) {
                                    if (player.getAttackStrengthScale(0.5F) > 0.9F) {
                                        amp += 1;
                                    }
                                }
                            }
                            if (!livingAttacker.hasEffect(GoetyEffects.VOID_TOUCHED)) {
                                victim.addEffect(new MobEffectInstance(effect, MathHelper.secondsToTicks(5), amp, false, true));
                            }
                        }
                        if (weapon instanceof DarkScytheItem) {
                            victim.playSound(ModSounds.SCYTHE_HIT_MEATY.get());
                        }
                        if (weapon instanceof DeathScytheItem) {
                            if (!victim.hasEffect(GoetyEffects.SAPPED)) {
                                victim.addEffect(new MobEffectInstance(GoetyEffects.SAPPED, 100));
                                victim.playSound(SoundEvents.SHIELD_BREAK, 2.0F, 1.0F);
                            } else {
                                MobEffectInstance effectinstance = victim.getEffect(GoetyEffects.SAPPED);
                                if (effectinstance != null) {
                                    EffectsUtil.amplifyEffect(victim, GoetyEffects.SAPPED.get(), 100);
                                } else {
                                    EffectsUtil.resetDuration(victim, GoetyEffects.SAPPED.get(), 100);
                                }
                                victim.playSound(SoundEvents.SHIELD_BREAK, 2.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }
        if (victim instanceof Player player) {
            if (CuriosFinder.hasCurio(victim, ModItems.SPITEFUL_BELT.get())) {
                int a = EnchantmentHelper.getItemEnchantmentLevel(victim.registryAccess().holderOrThrow(Enchantments.THORNS), CuriosFinder.findCurio(victim, ModItems.SPITEFUL_BELT.get()));
                if (SEHelper.getSoulsAmount(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.SpitefulBeltUseAmount, 0) * (a + 1))) {
                    if (!event.getSource().is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS) && !event.getSource().is(DamageTypes.THORNS) && event.getSource().getEntity() instanceof LivingEntity livingentity && livingentity != victim) {
                        livingentity.hurt(livingentity.damageSources().thorns(victim), 2.0F + a);
                        SEHelper.decreaseSouls(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.SpitefulBeltUseAmount, 0) * (a + 1));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void OnLivingJump(LivingEvent.LivingJumpEvent event){
        if (event.getEntity() instanceof Player player) {
            if (CuriosFinder.hasCurio(player, ModItems.WAYFARERS_BELT.get())){
                float f = 0.625F;
                if (player.hasEffect(MobEffects.JUMP)){
                    f += 0.1F * (float)(player.getEffect(MobEffects.JUMP).getAmplifier() + 1);
                }
                Vec3 vector3d = player.getDeltaMovement();
                player.setDeltaMovement(vector3d.x, f, vector3d.z);
            }
        }

    }

    @SubscribeEvent
    public static void OnLivingFall(LivingFallEvent event){
        if (event.getEntity() instanceof Player player) {
            if (CuriosFinder.hasCurio(player, ModItems.WAYFARERS_BELT.get())){
                event.setDistance(event.getDistance() / 2);
            }
        }
    }

    @SubscribeEvent
    public static void usingItemEvents(LivingEntityUseItemEvent.Tick event){
        if (!event.getEntity().level().isClientSide) {
            if (event.getItem().getItem() instanceof IWand && CuriosFinder.hasCurio(event.getEntity(), ModItems.TARGETING_MONOCLE.get())) {
                Entity entity = MobUtil.getSingleTarget(event.getEntity().level(), event.getEntity(), 16, 3);
                if (entity instanceof LivingEntity living && !MobUtil.areAllies(entity, event.getEntity())) {
                    event.getEntity().lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(living.getX(), living.getEyeY(), living.getZ()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void PlayerInteractBlockEvents(PlayerInteractEvent.RightClickBlock event){
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockHitResult blockHitResult = event.getHitVec();
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack itemStack = event.getItemStack();
        if (PotionUtils.getPotion(itemStack) == Potions.WATER){
            if (event.getFace() != Direction.DOWN && blockState.is(ModBlocks.END_SOIL.get())) {
                level.playSound(null, blockPos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                player.setItemInHand(event.getHand(), ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                if (!level.isClientSide) {
                    ServerLevel serverlevel = (ServerLevel)level;

                    for(int i = 0; i < 5; ++i) {
                        serverlevel.sendParticles(ParticleTypes.SPLASH, (double)blockPos.getX() + level.random.nextDouble(), (double)(blockPos.getY() + 1), (double)blockPos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
                    }
                }

                level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                level.setBlockAndUpdate(blockPos, ModBlocks.END_MUD.get().defaultBlockState());
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            }
        } else if (itemStack.is(Items.GLASS_BOTTLE)) {
            if (event.getFace() != Direction.DOWN && blockState.is(ModBlocks.END_MUD.get())) {
                level.playSound(null, blockPos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                player.setItemInHand(event.getHand(), ItemUtils.createFilledResult(itemStack, player, new ItemStack(ModItems.END_MUD_BOTTLE.get())));
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                if (!level.isClientSide) {
                    ServerLevel serverlevel = (ServerLevel)level;

                    for(int i = 0; i < 5; ++i) {
                        serverlevel.sendParticles(ParticleTypes.SPLASH, (double)blockPos.getX() + level.random.nextDouble(), (double)(blockPos.getY() + 1), (double)blockPos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
                    }
                }

                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                level.setBlockAndUpdate(blockPos, ModBlocks.END_SOIL.get().defaultBlockState());
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            }
        }
    }

    @SubscribeEvent
    public static void UseItemEvent(LivingEntityUseItemEvent.Finish event){
        if (CuriosFinder.hasCurio(event.getEntity(), ModItems.CRONE_HAT.get())){
            if (event.getEntity().level().random.nextFloat() <= 0.25F){
                if (event.getItem().getItem() instanceof PotionItem){
                    event.setResultStack(event.getItem());
                }
            }
            if (event.getEntity().level().random.nextFloat() <= 0.1F){
                if (event.getItem().getItem() instanceof BrewItem){
                    event.setResultStack(event.getItem());
                }
            }
        } else if (CuriosFinder.hasCurio(event.getEntity(), itemStack -> itemStack.getItem() instanceof WitchHatItem)){
            if (event.getEntity().level().random.nextFloat() <= 0.1F){
                if (event.getItem().getItem() instanceof PotionItem){
                    event.setResultStack(event.getItem());
                }
            }
        }
        if (event.getEntity() instanceof Player player) {
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MainConfig.WandCoolItemUse, false)) {
                if (!(event.getItem().getItem() instanceof IWand)) {
                    Item main = event.getEntity().getMainHandItem().getItem();
                    Item off = event.getEntity().getOffhandItem().getItem();
                    if (main instanceof IWand) {
                        player.getCooldowns().addCooldown(main, 10);
                    } else if (off instanceof IWand) {
                        player.getCooldowns().addCooldown(off, 10);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void AxeDeath(LivingDeathEvent event){
        LivingEntity killed = event.getEntity();
        Entity killer = event.getSource().getEntity();
        Level world = killed.getCommandSenderWorld();
        if (killer instanceof LivingEntity livingEntity) {
            if (ModDamageSource.physicalAttacks(event.getSource()) && livingEntity.getMainHandItem().getItem() instanceof RampagingAxeItem) {
                MobEffectInstance effectinstance1 = livingEntity.getEffect(GoetyEffects.RAMPAGE);
                if (!livingEntity.hasEffect(GoetyEffects.RAMPAGE)){
                    livingEntity.addEffect(new MobEffectInstance(GoetyEffects.RAMPAGE, MathHelper.secondsToTicks(za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.RampagingAxeDuration, 0))));
                } else if (effectinstance1 != null){
                    int random = killed.getMaxHealth() >= 20 ? 0 : world.random.nextInt(4);
                    if (effectinstance1.getAmplifier() < 4) {
                        if (random == 0) {
                            EffectsUtil.amplifyEffect(livingEntity, GoetyEffects.RAMPAGE.value(), MathHelper.secondsToTicks(za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.RampagingAxeDuration, 0)));
                        }
                    } else {
                        livingEntity.removeEffect(GoetyEffects.RAMPAGE);
                        if (world instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(new ShockwaveParticleOption(), livingEntity.getX(), livingEntity.getY() + 0.5F, livingEntity.getZ(), 0, 0.0D, 0.0D, 0.0D, 0);
                            serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, 1.0D, 0.0D, 0.0D, 0.5F);
                        }
                        LootingExplosion.Mode lootMode = CuriosFinder.hasWanting(livingEntity) ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
                        ExplosionUtil.lootExplode(world, livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 3.0F, false, Explosion.BlockInteraction.KEEP, lootMode);
                    }
                }
            }
        }
    }

    // TODO(1.21): LootingLevelEvent no longer exists. If this behavior is still desired,
    // port it to LivingDropsEvent / loot context hooks.
    // Removed @SubscribeEvent annotation since the event no longer exists
    // public static void HunterLoot(LootingLevelEvent event) {
    // }

    @SubscribeEvent
    public static void EmptyClickEvents(PlayerInteractEvent.LeftClickEmpty event){
        DeathScytheItem.emptyClick(event.getItemStack());
    }

    @SubscribeEvent
    public static void PlayerAttackEvents(AttackEntityEvent event){
        DeathScytheItem.entityClick(event.getEntity(), event.getEntity().level());
    }

    @SubscribeEvent
    public static void InteractEntityEvents(PlayerInteractEvent.EntityInteract event){
        Item item = event.getItemStack().getItem();
        if (item instanceof ReviveServantItem){
            if (SEHelper.getFocusCoolDown(event.getEntity()).isOnCooldown(event.getItemStack().getItem())){
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
            }
        }
        /*if (item instanceof IWand || item instanceof GoodwillGrimoire || item instanceof GrudgeGrimoire) {
            if (event.getTarget() instanceof Villager villager) {
                InteractionResult result = event.getItemStack().interactLivingEntity(event.getEntity(), villager, event.getHand());
                if (result.consumesAction()) {
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }*/
    }

    @SubscribeEvent
    public static void GeneralInteractEvents(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().is(Items.GLASS_BOTTLE)) {
            InteractionResultHolder<ItemStack> result = ItemHelper.getVoidBottle(event.getEntity(), event.getLevel(), event.getHand());
            if (result.getResult().consumesAction()) {
                event.setCanceled(true);
                event.setCancellationResult(result.getResult());
            }
        }
    }
}
