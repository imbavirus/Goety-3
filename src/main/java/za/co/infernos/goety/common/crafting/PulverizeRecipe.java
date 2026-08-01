package za.co.infernos.goety.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.StreamCodec;

public class PulverizeRecipe implements Recipe<RecipeInput> {
    public static final Serializer SERIALIZER = new Serializer();
    public static final MapCodec<PulverizeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.ingredient),
            ItemStack.CODEC.optionalFieldOf("item_result", ItemStack.EMPTY).forGetter(r -> r.itemResult),
            net.minecraft.core.registries.BuiltInRegistries.BLOCK.byNameCodec()
                    .optionalFieldOf("block_result", Blocks.CAVE_AIR).forGetter(r -> r.blockResult))
            .apply(instance, PulverizeRecipe::new));

    // Custom StreamCodec for ItemStack that handles empty stacks by using a dummy item during encoding
    private static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ItemStack> ITEM_RESULT_STREAM_CODEC = 
            StreamCodec.of(
                    (buf, stack) -> {
                        // Encode: convert empty to dummy item for network transmission
                        ItemStack toEncode = stack.isEmpty() 
                                ? za.co.infernos.goety.common.items.ModItems.JEI_DUMMY_NONE.get().getDefaultInstance() 
                                : stack;
                        ItemStack.STREAM_CODEC.encode(buf, toEncode);
                    },
                    buf -> {
                        // Decode: convert dummy item back to empty if it's the dummy
                        ItemStack decoded = ItemStack.STREAM_CODEC.decode(buf);
                        if (decoded.getItem() == za.co.infernos.goety.common.items.ModItems.JEI_DUMMY_NONE.get()) {
                            return ItemStack.EMPTY;
                        }
                        return decoded;
                    }
            );

    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PulverizeRecipe> STREAM_CODEC = StreamCodec
            .composite(
                    Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
                    ITEM_RESULT_STREAM_CODEC, r -> r.itemResult,
                    net.minecraft.network.codec.ByteBufCodecs.registry(net.minecraft.core.registries.Registries.BLOCK),
                    r -> r.blockResult,
                    PulverizeRecipe::new);

    public final Ingredient ingredient;
    protected final ItemStack itemResult;
    protected final Block blockResult;

    public PulverizeRecipe(Ingredient pIngredient, ItemStack pResult, Block pBlock) {
        this.ingredient = pIngredient;
        this.itemResult = pResult;
        this.blockResult = pBlock;
    }

    @Override
    public boolean matches(RecipeInput pInv, Level pLevel) {
        return this.ingredient.test(pInv.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput p_44001_, net.minecraft.core.HolderLookup.Provider p_267165_) {
        return this.itemResult.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider p_267052_) {
        // Pulverize recipes can have empty item results (they produce blocks instead)
        // Return a dummy item for recipe encoding if itemResult is empty (Minecraft 1.21.1 doesn't allow empty ItemStacks in recipes)
        if (this.itemResult.isEmpty()) {
            return za.co.infernos.goety.common.items.ModItems.JEI_DUMMY_NONE.get().getDefaultInstance();
        }
        return this.itemResult;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    public Block getBlockResult() {
        return this.blockResult;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeSerializer.PULVERIZE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PulverizeRecipe> {
        @Override
        public MapCodec<PulverizeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PulverizeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}