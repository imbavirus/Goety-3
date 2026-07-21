package za.co.infernos.goety.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.entities.CursedCageBlockEntity;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.init.ModSounds;

public class FlameCaptureItem extends Item {
    private static final String TAG_MOB = "mob";

    public FlameCaptureItem() {
        super(new Properties().stacksTo(1));
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (hasEntity(stack)) {
            if (level.getBlockState(pos).is(ModBlocks.CURSED_CAGE_BLOCK.get())) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof CursedCageBlockEntity cursedCageBlock) {
                    if (!cursedCageBlock.getItem().isEmpty()) {
                        return InteractionResult.PASS;
                    }
                    EntityType<?> capturedType = getEntityType(stack);
                    if (capturedType == null) {
                        return InteractionResult.PASS;
                    }
                    if (!level.isClientSide) {
                        level.setBlockAndUpdate(pos, Blocks.SPAWNER.defaultBlockState());
                        BlockEntity newBlockEntity = level.getBlockEntity(pos);
                        if (newBlockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                            spawnerBlockEntity.getSpawner().setEntityId(capturedType, level, level.random, pos);
                        }
                    }
                    clearEntity(stack);
                    level.playSound(null, pos, ModSounds.FLAME_CAPTURE_RELEASE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    stack.shrink(1);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        } else {
            if (ItemConfig.FireSpawnCage.get()) {
                if (level.getBlockState(pos).is(Blocks.SPAWNER)) {
                    if (!level.isClientSide()) {
                        BlockEntity blockEntity = level.getBlockEntity(pos);
                        if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                            Entity displayEntity = spawnerBlockEntity.getSpawner().getOrCreateDisplayEntity(level, pos);
                            if (displayEntity != null) {
                                setEntity(displayEntity, stack);
                                level.destroyBlock(pos, false);
                            }
                        }
                    }
                    level.playSound(null, pos, ModSounds.FLAME_CAPTURE_CATCH.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull Item.TooltipContext context, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        if (ItemConfig.FireSpawnCage.get()) {
            EntityType<?> entityType = getEntityType(stack);
            if (entityType != null) {
                ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
                MutableComponent textComponent = Component.translatable("tooltip.goety.entity")
                        .append(": ")
                        .append(Component.literal(key.toString()))
                        .withStyle(ChatFormatting.GREEN);
                tooltip.add(textComponent);
            }
        } else {
            tooltip.add(Component.translatable("tooltip.goety.disabled").withStyle(ChatFormatting.DARK_RED));
        }
    }

    public static boolean hasEntity(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(TAG_MOB);
    }

    @Nullable
    public static EntityType<?> getEntityType(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains(TAG_MOB)) {
            return null;
        }
        Optional<ResourceLocation> id = ResourceLocation.read(tag.getString(TAG_MOB)).result();
        if (id.isEmpty()) {
            return null;
        }
        return BuiltInRegistries.ENTITY_TYPE.getOptional(id.get()).orElse(null);
    }

    private static void setEntity(Entity entity, ItemStack stack) {
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (key == null) {
            return;
        }
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(TAG_MOB, key.toString()));
    }

    private static void clearEntity(ItemStack stack) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.remove(TAG_MOB));
    }
}
