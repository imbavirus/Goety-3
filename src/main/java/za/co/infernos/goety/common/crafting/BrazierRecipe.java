package za.co.infernos.goety.common.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.RecipeInput;

public class BrazierRecipe implements Recipe<RecipeInput> {
    public static final Serializer SERIALIZER = new Serializer();
    public static final MapCodec<BrazierRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients),
            net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.fieldOf("soulCost").forGetter(r -> r.soulCost))
            .apply(instance, BrazierRecipe::new));

    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, BrazierRecipe> STREAM_CODEC = StreamCodec
            .composite(
                    ItemStack.STREAM_CODEC, r -> r.result,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(
                            net.minecraft.network.codec.ByteBufCodecs.collection(NonNullList::createWithCapacity)),
                    r -> r.ingredients,
                    net.minecraft.network.codec.ByteBufCodecs.VAR_INT, r -> r.soulCost,
                    BrazierRecipe::new);

    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final int soulCost;

    public BrazierRecipe(ItemStack p_44248_, List<Ingredient> p_44249_, int soulCost) {
        this.result = p_44248_;
        this.ingredients = NonNullList.create();
        this.ingredients.addAll(p_44249_);
        this.soulCost = soulCost;
    }

    /**
     * Based on Runic Altar Recipe code by @Vazkii
     */
    @Override
    public boolean matches(RecipeInput container, Level p_44003_) {
        List<Ingredient> missingIngredients = Lists.newArrayList(this.ingredients);

        for (int i = 0; i < container.size(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.isEmpty()) {
                break;
            }

            int index = -1;

            for (int j = 0; j < missingIngredients.size(); j++) {
                Ingredient ingredient = missingIngredients.get(j);
                if (ingredient.test(itemStack)) {
                    index = j;
                    break;
                }
            }

            if (index == -1) {
                return false;
            } else {
                missingIngredients.remove(index);
            }
        }

        return missingIngredients.isEmpty();
    }

    @Override
    public ItemStack assemble(RecipeInput p_44001_, HolderLookup.Provider pAccess) {
        return this.result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pAccess) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeSerializer.BRAZIER_TYPE.get();
    }

    public int getSoulCost() {
        return this.soulCost;
    }

    public static class Serializer implements RecipeSerializer<BrazierRecipe> {
        @Override
        public MapCodec<BrazierRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, BrazierRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}