package za.co.infernos.goety.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class CursedInfuserRecipeSerializer<T extends CursedInfuserRecipes> implements RecipeSerializer<T>{
    private final int defaultCookingTime;
    private final IFactory<T> factory;
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public CursedInfuserRecipeSerializer(IFactory<T> pFactory, int pDefaultCookingTime) {
        this.defaultCookingTime = pDefaultCookingTime;
        this.factory = pFactory;
        Codec<ItemStack> resultCodec = Codec.withAlternative(ItemStack.CODEC, ItemStack.SIMPLE_ITEM_CODEC);
        this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(r -> r.group),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.ingredient),
                resultCodec.fieldOf("result").forGetter(r -> r.result),
                Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(r -> 0.0F), // Experience seems unused or constant 0.0F in factory create?
                Codec.INT.optionalFieldOf("cookingTime", defaultCookingTime).forGetter(r -> r.cookingTime),
                Codec.BOOL.optionalFieldOf("grim", false).forGetter(r -> r.grim)
        ).apply(instance, (group, ingredient, result, experience, cookingTime, grim) -> 
            pFactory.create(group, ingredient, result, experience, cookingTime, grim)
        ));
        
        this.streamCodec = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, r -> r.group,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
            ItemStack.STREAM_CODEC, r -> r.result,
            ByteBufCodecs.INT, r -> r.cookingTime,
            ByteBufCodecs.BOOL, r -> r.grim,
            (group, ingredient, result, cookingTime, grim) -> pFactory.create(group, ingredient, result, 0.0F, cookingTime, grim)
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

    public interface IFactory<T extends CursedInfuserRecipes> {
        // ID is no longer passed
        T create(String group, Ingredient ingredient, ItemStack result, float experience, int cookingTime, boolean grim);
    }
}