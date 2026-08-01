package za.co.infernos.goety.common.crafting;

import za.co.infernos.goety.common.items.magic.TaglockKit;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class TaglockRecipe extends CustomRecipe {
    public static final RecipeSerializer<TaglockRecipe> SERIALIZER = new Serializer();
    public static final MapCodec<TaglockRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(r -> r.ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)
    ).apply(inst, TaglockRecipe::new));
    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, TaglockRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
            ItemStack.STREAM_CODEC, r -> r.result,
            TaglockRecipe::new
    );

    public final Ingredient ingredient;
    public final ItemStack result;

    public TaglockRecipe(CraftingBookCategory category) {
        super(category);
        // Default values for ingredient and result, as this constructor doesn't provide them.
        // This might need adjustment based on how this recipe is intended to be used with the new fields.
        this.ingredient = Ingredient.EMPTY;
        // Use dummy item instead of EMPTY for recipe encoding compatibility (Minecraft 1.21.1 doesn't allow empty ItemStacks)
        this.result = za.co.infernos.goety.common.items.ModItems.JEI_DUMMY_NONE.get().getDefaultInstance();
    }

    // New constructor to match CODEC and STREAM_CODEC
    public TaglockRecipe(Ingredient ingredient, ItemStack result) {
        super(CraftingBookCategory.MISC); // Assuming a default category if not provided
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput p_44002_, Level p_44003_) {
        List<ItemStack> list = Lists.newArrayList();

        for(int i = 0; i < p_44002_.size(); ++i) {
            ItemStack itemstack = p_44002_.getItem(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if (itemstack1.getCount() != 1 || itemstack.getCount() != 1) {
                        return false;
                    }
                }
            }
        }

        return list.size() == 2;
    }

    @Override
    public ItemStack assemble(CraftingInput p_44001_, net.minecraft.core.HolderLookup.Provider p_267165_) {
        List<ItemStack> list = Lists.newArrayList();
        for(int i = 0; i < p_44001_.size(); ++i) {
            ItemStack itemstack = p_44001_.getItem(i);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
                if (list.size() > 1) {
                    ItemStack itemstack1 = list.get(0);
                    if (itemstack1.getCount() != 1 || itemstack.getCount() != 1) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (list.size() == 2) {
            ItemStack itemstack3 = list.get(0);
            ItemStack itemstack4 = list.get(1);
            if (itemstack3.getCount() == 1 && itemstack4.getCount() == 1) {
                if (itemstack3.getItem() instanceof TaglockKit
                && TaglockKit.hasEntity(itemstack3)){
                    LivingEntity livingEntity = TaglockKit.getEntity(itemstack3);
                    if (livingEntity != null){
                        ItemStack itemstack2 = new ItemStack(itemstack4.getItem());
                        TaglockKit.setEntity(itemstack2, livingEntity);
                        return itemstack2;
                    }
                } else if (itemstack4.getItem() instanceof TaglockKit
                        && TaglockKit.hasEntity(itemstack4)){
                    LivingEntity livingEntity = TaglockKit.getEntity(itemstack4);
                    if (livingEntity != null){
                        ItemStack itemstack2 = new ItemStack(itemstack3.getItem());
                        TaglockKit.setEntity(itemstack2, livingEntity);
                        return itemstack2;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return p_43999_ * p_44000_ >= 2;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider pAccess) {
        // Return the result item for recipe encoding
        // If result is empty (from default constructor), return dummy item
        if (this.result.isEmpty()) {
            return za.co.infernos.goety.common.items.ModItems.JEI_DUMMY_NONE.get().getDefaultInstance();
        }
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializer.TAGLOCK.get();
    }

    public static class Serializer implements RecipeSerializer<TaglockRecipe> {
        @Override
        public MapCodec<TaglockRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, TaglockRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}