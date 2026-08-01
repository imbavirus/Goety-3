package za.co.infernos.goety.utils;

import za.co.infernos.goety.Goety;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ModLootTables {
    private static final Set<ResourceKey<LootTable>> LOCATIONS = Sets.newHashSet();
    private static final Set<ResourceKey<LootTable>> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
    public static final ResourceKey<LootTable> EMPTY = register("empty");
    public static final ResourceKey<LootTable> CRYPT_TOMB = register("chests/crypt_tomb");

    public static final ResourceKey<LootTable> TALL_SKULL = register("entities/tall_skull_mobs");
    public static final ResourceKey<LootTable> PLAYER_WITCH = register("entities/player_witch");
    public static final ResourceKey<LootTable> CULTISTS = register("entities/cultist_extra");
    public static final ResourceKey<LootTable> CRYPT_SLIME = register("entities/crypt_slime");
    public static final ResourceKey<LootTable> TROPICAL_SLIME = register("entities/tropical_slime");
    public static final ResourceKey<LootTable> INFERNO = register("entities/inferno_extra");
    public static final ResourceKey<LootTable> APOSTLE_HARD = register("entities/apostle_2");

    public static final ResourceKey<LootTable> WITCH_BARTER = register("gameplay/witch_bartering");
    public static final ResourceKey<LootTable> WARLOCK_BARTER = register("gameplay/warlock_bartering");
    public static final ResourceKey<LootTable> MAVERICK_BARTER = register("gameplay/maverick_bartering");
    public static final ResourceKey<LootTable> HERETIC_BARTER = register("gameplay/heretic_bartering");
    public static final ResourceKey<LootTable> CRONE_BARTER = register("gameplay/crone_bartering");

    public static final ResourceKey<LootTable> TREASURE_POUCH = register("gameplay/treasure_pouch");
    public static final ResourceKey<LootTable> VOID_SPAWNER_LOOT = register("gameplay/void_spawner_loot");
    public static final ResourceKey<LootTable> VOID_SPAWNER_KEY = register("gameplay/void_spawner_key");
    public static final ResourceKey<LootTable> VOID_VAULT_REWARD = register("gameplay/void_vault_reward");

    private static ResourceKey<LootTable> register(String pId) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, Goety.location(pId)));
    }

    private static ResourceKey<LootTable> register(ResourceKey<LootTable> pId) {
        if (LOCATIONS.add(pId)) {
            return pId;
        } else {
            throw new IllegalArgumentException(pId.location() + " is already a registered built-in loot table");
        }
    }

    public static LootParams.Builder createLootParams(LivingEntity target, boolean checkPlayerKill,
            DamageSource source) {
        LootParams.Builder lootcontext$builder = (new LootParams.Builder((ServerLevel) target.level()))
                .withParameter(LootContextParams.THIS_ENTITY, target)
                .withParameter(LootContextParams.ORIGIN, target.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());
        if (checkPlayerKill && target.getKillCredit() instanceof Player player) {
            lootcontext$builder = lootcontext$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                    .withLuck(player.getLuck());
        }

        return lootcontext$builder;
    }

    public static void shuffleAndSplitItems(ObjectArrayList<ItemStack> p_230925_, int p_230926_,
            RandomSource p_230927_) {
        List<ItemStack> list = Lists.newArrayList();
        Iterator<ItemStack> iterator = p_230925_.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = iterator.next();
            if (itemstack.isEmpty()) {
                iterator.remove();
            } else if (itemstack.getCount() > 1) {
                list.add(itemstack);
                iterator.remove();
            }
        }

        while (p_230926_ - p_230925_.size() - list.size() > 0 && !list.isEmpty()) {
            ItemStack itemstack2 = list.remove(Mth.nextInt(p_230927_, 0, list.size() - 1));
            int i = Mth.nextInt(p_230927_, 1, itemstack2.getCount() / 2);
            ItemStack itemstack1 = itemstack2.split(i);
            if (itemstack2.getCount() > 1 && p_230927_.nextBoolean()) {
                list.add(itemstack2);
            } else {
                p_230925_.add(itemstack2);
            }

            if (itemstack1.getCount() > 1 && p_230927_.nextBoolean()) {
                list.add(itemstack1);
            } else {
                p_230925_.add(itemstack1);
            }
        }

        p_230925_.addAll(list);
        Util.shuffle(p_230925_, p_230927_);
    }

    public static void createLootChest(LivingEntity target, BlockState blockState, BlockPos blockPos,
            DamageSource cause) {
        if (target.level().getServer() != null) {
            target.level().setBlockAndUpdate(blockPos, blockState);
            LootParams lootParams = ModLootTables.createLootParams(target, true, cause)
                    .create(LootContextParamSets.ENTITY);
            LootTable table = target.level().getServer().reloadableRegistries().getLootTable(target.getLootTable());
            ObjectArrayList<ItemStack> lootItems = table.getRandomItems(lootParams);
            List<Integer> availableSlots = getAvailableSlots(target.getRandom());
            ModLootTables.shuffleAndSplitItems(lootItems, availableSlots.size(), target.getRandom());
            NonNullList<ItemStack> finalLoot = NonNullList.withSize(27, ItemStack.EMPTY);
            for (ItemStack itemstack : lootItems) {
                if (!availableSlots.isEmpty()) {
                    if (itemstack.isEmpty()) {
                        finalLoot.set(availableSlots.remove(availableSlots.size() - 1), ItemStack.EMPTY);
                    } else {
                        finalLoot.set(availableSlots.remove(availableSlots.size() - 1), itemstack);
                    }
                }
            }
            if (target.level().getBlockEntity(blockPos) instanceof Container container) {
                for (int i = 0; i < container.getContainerSize(); i++) {
                    container.setItem(i, finalLoot.get(i));
                }
            }
        }
    }

    public static List<Integer> getAvailableSlots(RandomSource random) {
        ObjectArrayList<Integer> arrayList = new ObjectArrayList<>();
        for (int i = 0; i < 27; ++i) {
            arrayList.add(i);
        }
        Util.shuffle(arrayList, random);
        return arrayList;
    }

    public static Set<ResourceKey<LootTable>> all() {
        return IMMUTABLE_LOCATIONS;
    }

}
