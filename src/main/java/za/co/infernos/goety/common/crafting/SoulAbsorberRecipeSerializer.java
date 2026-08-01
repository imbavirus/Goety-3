package za.co.infernos.goety.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class SoulAbsorberRecipeSerializer <T extends SoulAbsorberRecipes>  implements RecipeSerializer<T> {
    private final int defaultSoulIncrease;
    private final int defaultCookingTime;
    private final IFactory<T> factory;
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public SoulAbsorberRecipeSerializer(IFactory<T> pFactory, int pDefaultSoulIncrease, int pDefaultCookingTime) {
        this.defaultSoulIncrease = pDefaultSoulIncrease;
        this.defaultCookingTime = pDefaultCookingTime;
        this.factory = pFactory;
        
        this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.ingredient),
                Codec.INT.optionalFieldOf("soulIncrease", defaultSoulIncrease).forGetter(r -> r.soulIncrease),
                Codec.INT.optionalFieldOf("cookingtime", defaultCookingTime).forGetter(r -> r.cookingTime)
        ).apply(instance, pFactory::create));

        this.streamCodec = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
                ByteBufCodecs.INT, r -> r.soulIncrease,
                ByteBufCodecs.INT, r -> r.cookingTime,
                pFactory::create
        );
    }

    @Override
    public MapCodec<T> codec() {
        return codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }

    public interface IFactory<T extends SoulAbsorberRecipes> {
        // Removed ResourceLocation
        T create(Ingredient ingredient, int soulIncrease, int cookingTime);
    }
}