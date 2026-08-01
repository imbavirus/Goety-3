package za.co.infernos.goety.common.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeInput;
import java.util.Optional;

public class BrewingRecipe implements Recipe<RecipeInput> {
    public static final MapCodec<BrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(BrewingRecipe::getInput),
            EntityInput.CODEC.optionalFieldOf("entity").forGetter(BrewingRecipe::getEntityInput),
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(BrewingRecipe::getOutput),
            Codec.INT.fieldOf("soulCost").forGetter(BrewingRecipe::getSoulCost),
            Codec.INT.optionalFieldOf("capacityExtra", 0).forGetter(BrewingRecipe::getCapacityExtra),
            Codec.INT.fieldOf("duration").forGetter(BrewingRecipe::getDuration)
    ).apply(instance, BrewingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BrewingRecipe> STREAM_CODEC = StreamCodec.of(
            BrewingRecipe::toNetwork, BrewingRecipe::fromNetwork
    );

    public final Ingredient input;
    private final TagKey<EntityType<?>> entityTypeTag;
    private final EntityType<?> entityType;
    public final Holder<MobEffect> output;
    public final int soulCost;
    public final int capacityExtra;
    public final int duration;

    public BrewingRecipe(Ingredient input, Optional<EntityInput> entityInput, Holder<MobEffect> output, int soulCost, int capacityExtra, int duration) {
        this.input = input;
        this.entityTypeTag = entityInput.flatMap(EntityInput::tag).orElse(null);
        this.entityType = entityInput.flatMap(EntityInput::type).orElse(null);
        this.output = output;
        this.soulCost = soulCost;
        this.capacityExtra = capacityExtra;
        this.duration = duration;
    }

    // Helper constructor for internal usage if needed, or keep logic in main constructor
    private Optional<EntityInput> getEntityInput() {
        if (entityTypeTag != null) return Optional.of(new EntityInput(Optional.of(entityTypeTag), Optional.empty()));
        if (entityType != null) return Optional.of(new EntityInput(Optional.empty(), Optional.of(entityType)));
        return Optional.empty();
    }

    private static void toNetwork(RegistryFriendlyByteBuf buf, BrewingRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
        buf.writeBoolean(recipe.entityTypeTag != null);
        if (recipe.entityTypeTag != null) {
            buf.writeResourceLocation(recipe.entityTypeTag.location());
        }
        buf.writeBoolean(recipe.entityType != null);
        if (recipe.entityType != null) {
            ByteBufCodecs.registry(Registries.ENTITY_TYPE).encode(buf, recipe.entityType);
        }
        ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT).encode(buf, recipe.output);
        buf.writeInt(recipe.soulCost);
        buf.writeInt(recipe.capacityExtra);
        buf.writeInt(recipe.duration);
    }

    private static BrewingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        TagKey<EntityType<?>> entityTag = null;
        EntityType<?> entityType = null;
        if (buf.readBoolean()) {
            entityTag = TagKey.create(Registries.ENTITY_TYPE, buf.readResourceLocation());
        }
        if (buf.readBoolean()) {
            entityType = ByteBufCodecs.registry(Registries.ENTITY_TYPE).decode(buf);
        }
        Holder<MobEffect> output = ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT).decode(buf);
        int soulCost = buf.readInt();
        int capacityExtra = buf.readInt();
        int duration = buf.readInt();
        
        Optional<EntityInput> entityInput = Optional.empty();
        if (entityTag != null) entityInput = Optional.of(new EntityInput(Optional.of(entityTag), Optional.empty()));
        if (entityType != null) entityInput = Optional.of(new EntityInput(Optional.empty(), Optional.of(entityType)));

        return new BrewingRecipe(input, entityInput, output, soulCost, capacityExtra, duration);
    }

    @Override
    public boolean matches(RecipeInput p_44002_, Level p_44003_) {
        // Implement actual matching logic if needed, previously it returned false?
        // Original code: return false;
        // Keeping it false for now as per original code, or maybe it was incomplete?
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput p_44001_, HolderLookup.Provider p_267052_) {
        return this.getResultItem(p_267052_);
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false; // Original was false
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Nullable
    public TagKey<EntityType<?>> getEntityTypeTag() {
        return entityTypeTag;
    }

    @Nullable
    public EntityType<?> getEntityType() {
        return this.entityType;
    }

    public int getSoulCost() {
        return this.soulCost;
    }

    public int getCapacityExtra() {
        return this.capacityExtra;
    }

    public int getDuration() {
        return this.duration;
    }

    public Holder<MobEffect> getOutput() {
        return this.output;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider p_267052_) {
        // Brewing recipes don't produce items, they modify brews in the cauldron
        // Return a dummy item for recipe encoding (Minecraft 1.21.1 doesn't allow empty ItemStacks in recipes)
        return za.co.infernos.goety.common.items.ModItems.JEI_DUMMY_NONE.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.BREWING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeSerializer.BREWING_TYPE.get();
    }
    
    // Helper record
    record EntityInput(Optional<TagKey<EntityType<?>>> tag, Optional<EntityType<?>> type) {
        public static final Codec<EntityInput> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            TagKey.codec(Registries.ENTITY_TYPE).optionalFieldOf("tag").forGetter(EntityInput::tag),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().optionalFieldOf("entity_type").forGetter(EntityInput::type)
        ).apply(inst, EntityInput::new));
    }
}