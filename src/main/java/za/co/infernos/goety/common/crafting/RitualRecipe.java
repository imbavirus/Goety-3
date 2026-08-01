package za.co.infernos.goety.common.crafting;

import za.co.infernos.goety.common.ritual.ModRituals;
import za.co.infernos.goety.common.ritual.Ritual;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 1.21+ port note:
 * - Recipes now implement {@link Recipe} with {@link RecipeInput} and are serialized via codecs/stream codecs.
 * - This class is primarily used by {@link za.co.infernos.goety.common.blocks.entities.DarkAltarBlockEntity} via
 *   the custom {@link #matches(Level, BlockPos, Player, ItemStack)} method (not the crafting-table matcher).
 */
public class RitualRecipe implements Recipe<RecipeInput> {
    public static final RecipeSerializer<RitualRecipe> SERIALIZER = new Serializer();

    private final String group;
    private final String craftType;
    private final ResourceLocation ritualType;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final Ingredient activationItem;
    private final int duration;
    private final int summonLife;
    private final int soulCost;
    @Nullable private final TagKey<EntityType<?>> entityToSacrifice;
    private final String entityToSacrificeDisplayName;
    @Nullable private final TagKey<EntityType<?>> entityToConvert;
    private final String entityToConvertDisplayName;
    @Nullable private final EntityType<?> entityToSummon;
    @Nullable private final EntityType<?> entityToConvertInto;
    private final int xpLevelCost;
    private final String research;

    private final Ritual ritual;
    private final float durationPerIngredient;

    public RitualRecipe(
            String group,
            String craftType,
            ResourceLocation ritualType,
            ItemStack result,
            NonNullList<Ingredient> ingredients,
            Ingredient activationItem,
            int duration,
            int summonLife,
            int soulCost,
            @Nullable TagKey<EntityType<?>> entityToSacrifice,
            String entityToSacrificeDisplayName,
            @Nullable TagKey<EntityType<?>> entityToConvert,
            String entityToConvertDisplayName,
            @Nullable EntityType<?> entityToSummon,
            @Nullable EntityType<?> entityToConvertInto,
            int xpLevelCost,
            String research
    ) {
        this.group = group;
        this.craftType = craftType;
        this.ritualType = ritualType;
        this.result = result;
        this.ingredients = ingredients;
        this.activationItem = activationItem;
        this.duration = duration;
        this.summonLife = summonLife;
        this.soulCost = soulCost;
        this.entityToSacrifice = entityToSacrifice;
        this.entityToSacrificeDisplayName = entityToSacrificeDisplayName;
        this.entityToConvert = entityToConvert;
        this.entityToConvertDisplayName = entityToConvertDisplayName;
        this.entityToSummon = entityToSummon;
        this.entityToConvertInto = entityToConvertInto;
        this.xpLevelCost = xpLevelCost;
        this.research = research;

        this.ritual = ModRituals.REGISTRY.get(this.ritualType).create(this);
        this.durationPerIngredient = this.duration / (float) (this.ingredients.size() + 1);
    }

    public String getCraftType() {
        return this.craftType;
    }

    public int getSoulCost() {
        return this.soulCost;
    }

    public Ingredient getActivationItem() {
        return this.activationItem;
    }

    public int getDuration() {
        return this.duration;
    }

    public float getDurationPerIngredient() {
        return this.durationPerIngredient;
    }

    public boolean matches(Level world, BlockPos darkAltarPos, Player player, ItemStack activationItem) {
        return this.ritual.identify(world, darkAltarPos, player, activationItem);
    }

    public Ritual getRitual() {
        return this.ritual;
    }

    @Nullable
    public TagKey<EntityType<?>> getEntityToSacrifice() {
        return this.entityToSacrifice;
    }

    public boolean requiresSacrifice() {
        return this.entityToSacrifice != null;
    }

    @Nullable
    public TagKey<EntityType<?>> getEntityToConvert() {
        return this.entityToConvert;
    }

    public boolean isConversion() {
        return this.entityToConvert != null && this.entityToConvertInto != null;
    }

    public boolean isSummoning() {
        return this.entityToSummon != null;
    }

    @Nullable
    public EntityType<?> getEntityToSummon() {
        return this.entityToSummon;
    }

    @Nullable
    public EntityType<?> getEntityToConvertInto() {
        return this.entityToConvertInto;
    }

    public String getEntityToSacrificeDisplayName() {
        return this.entityToSacrificeDisplayName;
    }

    public String getEntityToConvertDisplayName() {
        return this.entityToConvertDisplayName;
    }

    public int getXPLevelCost() {
        return this.xpLevelCost;
    }

    public String getResearch() {
        return this.research;
    }

    public int getSummonLife() {
        return this.summonLife;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        // Ritual recipes are not intended to be matched through vanilla crafting grids.
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeSerializer.RITUAL_TYPE.get();
    }

    private static final class EntityTagWithName {
        static final MapCodec<EntityTagWithName> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                TagKey.codec(Registries.ENTITY_TYPE).fieldOf("tag").forGetter(v -> v.tag),
                Codec.STRING.fieldOf("display_name").forGetter(v -> v.displayName)
        ).apply(inst, EntityTagWithName::new));

        final TagKey<EntityType<?>> tag;
        final String displayName;

        EntityTagWithName(TagKey<EntityType<?>> tag, String displayName) {
            this.tag = tag;
            this.displayName = displayName;
        }
    }

    private static final class Serializer implements RecipeSerializer<RitualRecipe> {
        private static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(r -> r.group),
                Codec.STRING.optionalFieldOf("craftType", "").forGetter(r -> r.craftType),
                ResourceLocation.CODEC.fieldOf("ritual_type").forGetter(r -> r.ritualType),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                Ingredient.LIST_CODEC_NONEMPTY.xmap(
                        list -> {
                            NonNullList<Ingredient> nn = NonNullList.create();
                            nn.addAll(list);
                            return nn;
                        },
                        (NonNullList<Ingredient> nn) -> List.copyOf(nn)
                ).fieldOf("ingredients").forGetter(r -> r.ingredients),
                Ingredient.CODEC.fieldOf("activation_item").forGetter(r -> r.activationItem),
                Codec.INT.optionalFieldOf("duration", 30).forGetter(r -> r.duration),
                Codec.INT.optionalFieldOf("summonLife", -1).forGetter(r -> r.summonLife),
                Codec.INT.optionalFieldOf("soulCost", 0).forGetter(r -> r.soulCost),
                EntityTagWithName.CODEC.codec().optionalFieldOf("entity_to_sacrifice").forGetter(r -> r.entityToSacrifice == null ? java.util.Optional.empty() : java.util.Optional.of(new EntityTagWithName(r.entityToSacrifice, r.entityToSacrificeDisplayName))),
                EntityTagWithName.CODEC.codec().optionalFieldOf("entity_to_convert").forGetter(r -> r.entityToConvert == null ? java.util.Optional.empty() : java.util.Optional.of(new EntityTagWithName(r.entityToConvert, r.entityToConvertDisplayName))),
                ResourceLocation.CODEC.optionalFieldOf("entity_to_summon").forGetter(r -> java.util.Optional.ofNullable(r.entityToSummon).map(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE::getKey)),
                ResourceLocation.CODEC.optionalFieldOf("entity_to_convert_into").forGetter(r -> java.util.Optional.ofNullable(r.entityToConvertInto).map(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE::getKey)),
                Codec.INT.optionalFieldOf("xpLevelCost", 0).forGetter(r -> r.xpLevelCost),
                Codec.STRING.optionalFieldOf("research", "").forGetter(r -> r.research)
        ).apply(inst, (group, craftType, ritualType, result, ingredients, activationItem, duration, summonLife, soulCost,
                       sacrificeOpt, convertOpt, summonIdOpt, convertIntoIdOpt, xpLevelCost, research) -> {
            TagKey<EntityType<?>> sacrificeTag = sacrificeOpt.map(v -> v.tag).orElse(null);
            String sacrificeName = sacrificeOpt.map(v -> v.displayName).orElse("");
            TagKey<EntityType<?>> convertTag = convertOpt.map(v -> v.tag).orElse(null);
            String convertName = convertOpt.map(v -> v.displayName).orElse("");
            EntityType<?> toSummon = summonIdOpt.flatMap(id -> net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getOptional(id)).orElse(null);
            EntityType<?> toConvertInto = convertIntoIdOpt.flatMap(id -> net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getOptional(id)).orElse(null);
            return new RitualRecipe(group, craftType, ritualType, result, ingredients, activationItem, duration, summonLife, soulCost,
                    sacrificeTag, sacrificeName, convertTag, convertName, toSummon, toConvertInto, xpLevelCost, research);
        }));

        private static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

        @Override
        public MapCodec<RitualRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, RitualRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}

