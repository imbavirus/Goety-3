package za.co.infernos.goety.utils;

import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import javax.annotation.Nullable;

public class ModTradeUtil {
    public static final int NOVICE = 1;
    public static final int APPRENTICE = 2;
    public static final int JOURNEYMAN = 3;
    public static final int EXPERT = 4;
    public static final int MASTER = 5;

    public static void addVillagerTrades(VillagerTradesEvent event, int profLevel, VillagerTrades.ItemListing... trades) {
        for (VillagerTrades.ItemListing trade : trades) {
            event.getTrades().get(profLevel).add(trade);
        }
    }

    public static void addVillagerTrades(VillagerTradesEvent event, VillagerProfession profession, int profLevel, VillagerTrades.ItemListing... trades) {
        if (event.getType().equals(profession)) {
            addVillagerTrades(event, profLevel, trades);
        }
    }

    public static class ItemsForEmeralds implements VillagerTrades.ItemListing {
        private final Item item;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemsForEmeralds(ItemLike item, int cost, int maxUses, int villagerXp) {
            this.item = item.asItem();
            this.cost = cost;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
            return new MerchantOffer(new ItemCost(Items.EMERALD, this.cost), new ItemStack(this.item), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class TreasureMapForEmeralds implements VillagerTrades.ItemListing {
        private final int emeraldCost;
        private final TagKey<Structure> destination;
        private final String displayName;
        private final net.minecraft.core.Holder<MapDecorationType> destinationType;
        private final int maxUses;
        private final int villagerXp;

        public TreasureMapForEmeralds(int emeraldCost, TagKey<Structure> destination, String displayName, net.minecraft.core.Holder<MapDecorationType> mapMarker, int maxUses, int villagerXp) {
            this.emeraldCost = emeraldCost;
            this.destination = destination;
            this.displayName = displayName;
            this.destinationType = mapMarker;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
        }

        @Nullable
        public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
            return new MerchantOffer(new ItemCost(Items.EMERALD, this.emeraldCost), new ItemStack(Items.COMPASS), this.maxUses, this.villagerXp, 0.2F);
        }
    }
}
