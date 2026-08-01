package za.co.infernos.goety.common.items;

import za.co.infernos.goety.common.blocks.entities.ArcaBlockEntity;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ArcaCompassItem extends Item {
    public static final String TAG_PLAYER = "TrackedPlayer";
    public static final String TAG_PLAYER_NAME = "TrackedPlayerName";
    public static final String TAG_ARCA_POS = "ArcaPos";
    public static final String TAG_ARCA_DIMENSION = "ArcaDimension";
    public static final String TAG_ARCA_TRACKED = "ArcaTracked";

    public ArcaCompassItem() {
        super(new Item.Properties().fireResistant());
    }

    public static boolean hasPlayer(ItemStack p_40737_) {
        return p_40737_.has(DataComponents.CUSTOM_DATA) && p_40737_.get(DataComponents.CUSTOM_DATA).contains(TAG_PLAYER);
    }

    public static boolean hasArca(ItemStack p_40737_) {
        CompoundTag compoundtag = p_40737_.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return compoundtag.contains(TAG_ARCA_DIMENSION) || compoundtag.contains(TAG_ARCA_POS);
    }

    private static Optional<ResourceKey<Level>> getArcaDimension(CompoundTag p_40728_) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, p_40728_.get(TAG_ARCA_DIMENSION)).result();
    }

    @Nullable
    public static GlobalPos getArcaPosition(CompoundTag p_220022_) {
        boolean flag = p_220022_.contains(TAG_ARCA_POS);
        boolean flag1 = p_220022_.contains(TAG_ARCA_DIMENSION);
        if (flag && flag1) {
            Optional<ResourceKey<Level>> optional = getArcaDimension(p_220022_);
            if (optional.isPresent()) {
                BlockPos blockpos = NbtUtils.readBlockPos(p_220022_, TAG_ARCA_POS).orElse(null);
                return GlobalPos.of(optional.get(), blockpos);
            }
        }

        return null;
    }

    public boolean isFoil(ItemStack p_40739_) {
        return hasPlayer(p_40739_) || super.isFoil(p_40739_);
    }

    public void inventoryTick(ItemStack p_40720_, Level p_40721_, Entity p_40722_, int p_40723_, boolean p_40724_) {
        if (!p_40721_.isClientSide) {
            CustomData customData = p_40720_.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag compoundtag = customData.copyTag();
            boolean changed = false;

            if (!p_40722_.isAlive()){
                if (compoundtag.contains(TAG_PLAYER)){
                    compoundtag.remove(TAG_PLAYER);
                    changed = true;
                }
                if (compoundtag.contains(TAG_PLAYER_NAME)){
                    compoundtag.remove(TAG_PLAYER_NAME);
                    changed = true;
                }
                if (compoundtag.contains(TAG_ARCA_POS)){
                    compoundtag.remove(TAG_ARCA_POS);
                    changed = true;
                }
                if (compoundtag.contains(TAG_ARCA_DIMENSION)){
                    compoundtag.remove(TAG_ARCA_DIMENSION);
                    changed = true;
                }
                if (compoundtag.contains(TAG_ARCA_TRACKED)){
                    compoundtag.remove(TAG_ARCA_TRACKED);
                    changed = true;
                }
            }
            if (hasPlayer(p_40720_)){
                if (compoundtag.contains(TAG_PLAYER)){
                    Player player = p_40721_.getPlayerByUUID(compoundtag.getUUID(TAG_PLAYER));
                    if (player == null) {
                        return;
                    }
                    if (SEHelper.getArcaBlock(player) != null){
                        if (!compoundtag.contains(TAG_ARCA_POS)){
                            compoundtag.put(TAG_ARCA_POS, NbtUtils.writeBlockPos(SEHelper.getArcaBlock(player)));
                            changed = true;
                            if (SEHelper.getArcaDimension(player) != null){
                                Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, SEHelper.getArcaDimension(player)).result().ifPresent((p_40731_) -> {
                                    compoundtag.put(TAG_ARCA_DIMENSION, p_40731_);
                                });
                                // Changed set inside lambda? effectively final issue? 
                                // CompoundTag is mutable.
                                // We need to mark changed.
                                changed = true;
                            }
                        }
                    }
                }
                if (hasArca(p_40720_)) {
                    if (compoundtag.contains(TAG_ARCA_TRACKED) && !compoundtag.getBoolean(TAG_ARCA_TRACKED)) {
                        return;
                    }

                    Optional<ResourceKey<Level>> optional = getArcaDimension(compoundtag);
                    if (optional.isPresent() && optional.get() == p_40721_.dimension() && compoundtag.contains(TAG_ARCA_POS)) {
                        BlockPos blockpos = NbtUtils.readBlockPos(compoundtag, TAG_ARCA_POS).orElse(null);
                        if (!p_40721_.isInWorldBounds(blockpos) || !(p_40721_.getBlockEntity(blockpos) instanceof ArcaBlockEntity)) {
                            compoundtag.remove(TAG_ARCA_POS);
                            changed = true;
                        }
                    }
                }
            } else if (compoundtag.contains(TAG_PLAYER_NAME)){
                compoundtag.remove(TAG_PLAYER_NAME);
                changed = true;
            }

            if (changed) {
                p_40720_.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundtag));
            }
        }
    }

    public static void addPlayer(Player player, CompoundTag p_40735_) {
        p_40735_.putUUID(TAG_PLAYER, player.getUUID());
        p_40735_.putString(TAG_PLAYER_NAME, player.getDisplayName().getString());
        p_40735_.putBoolean(TAG_ARCA_TRACKED, true);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            CompoundTag tag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            if (tag.contains(TAG_PLAYER_NAME)) {
                tooltip.add(Component.translatable("tooltip.goety.arca_compass_track", tag.getString(TAG_PLAYER_NAME)).withStyle(ChatFormatting.AQUA));
            }
            GlobalPos globalPos = getArcaPosition(tag);
            if (globalPos != null) {
                BlockPos blockPos = globalPos.pos();
                tooltip.add(Component.translatable("tooltip.goety.arca").withStyle(ChatFormatting.GOLD)
                        .append(Component.translatable("tooltip.goety.arcaCoords", blockPos.getX(), blockPos.getY(), blockPos.getZ())));
                if (getArcaDimension(tag).isPresent()){
                    ResourceKey<Level> dimension = getArcaDimension(tag).get();
                    tooltip.add(Component.translatable("tooltip.goety.arcaDimension", dimension.location().toString()));
                }
            }
        } else {
            tooltip.add(Component.translatable("tooltip.goety.arca_compass").withStyle(ChatFormatting.GOLD));
        }
        super.appendHoverText(stack, context, tooltip, flagIn);
    }
}