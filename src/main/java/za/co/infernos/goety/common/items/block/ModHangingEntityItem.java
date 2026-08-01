package za.co.infernos.goety.common.items.block;

import za.co.infernos.goety.common.entities.ModEntityType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Had to make a copy of the vanilla class to use Supplier since Items are loaded before EntityTypes.
 */
public class ModHangingEntityItem extends Item {
   private static final Component TOOLTIP_RANDOM_VARIANT = Component.translatable("painting.random").withStyle(ChatFormatting.GRAY);
   private final Supplier<EntityType<? extends HangingEntity>> type;

   public ModHangingEntityItem(Supplier<EntityType<? extends HangingEntity>> p_41324_, Properties p_41325_) {
      super(p_41325_);
      this.type = p_41324_;
   }

   public InteractionResult useOn(UseOnContext p_41331_) {
      BlockPos blockpos = p_41331_.getClickedPos();
      Direction direction = p_41331_.getClickedFace();
      BlockPos blockpos1 = blockpos.relative(direction);
      Player player = p_41331_.getPlayer();
      ItemStack itemstack = p_41331_.getItemInHand();
      if (player != null && !this.mayPlace(player, direction, itemstack, blockpos1)) {
         return InteractionResult.FAIL;
      } else {
         Level level = p_41331_.getLevel();
         HangingEntity hangingentity;
         if (this.type.get() == EntityType.PAINTING) {
            Optional<Painting> optional = Painting.create(level, blockpos1, direction);
            if (optional.isEmpty()) {
               return InteractionResult.CONSUME;
            }

            hangingentity = optional.get();
         } else if (this.type.get() == EntityType.ITEM_FRAME) {
            hangingentity = new ItemFrame(level, blockpos1, direction);
         } else {
            if (this.type.get() != EntityType.GLOW_ITEM_FRAME) {
               return InteractionResult.sidedSuccess(level.isClientSide);
            }

            hangingentity = new GlowItemFrame(level, blockpos1, direction);
         }

         net.minecraft.world.item.component.CustomData customData = itemstack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
         if (customData != null) {
            EntityType.updateCustomEntityTag(level, player, hangingentity, customData);
         }

         if (hangingentity.survives()) {
            if (!level.isClientSide) {
               hangingentity.playPlacementSound();
               level.gameEvent(player, GameEvent.ENTITY_PLACE, hangingentity.position());
               level.addFreshEntity(hangingentity);
            }

            itemstack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
         } else {
            return InteractionResult.CONSUME;
         }
      }
   }

   protected boolean mayPlace(Player p_41326_, Direction p_41327_, ItemStack p_41328_, BlockPos p_41329_) {
      return !p_41327_.getAxis().isVertical() && p_41326_.mayUseItemAt(p_41329_, p_41327_, p_41328_);
   }

   public void appendHoverText(ItemStack p_270235_, Item.TooltipContext context, List<Component> p_270630_, TooltipFlag p_270170_) {
      super.appendHoverText(p_270235_, context, p_270630_, p_270170_);
      if (this.type.get() == EntityType.PAINTING || this.type.get() == ModEntityType.MOD_PAINTING.get()) {
         net.minecraft.world.item.component.CustomData customData = p_270235_.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
         if (customData != null) {
             CompoundTag compoundtag = customData.copyTag();
             if (compoundtag.contains("EntityTag", 10)) {
                CompoundTag compoundtag1 = compoundtag.getCompound("EntityTag");
                if (compoundtag1.contains("variant", 8)) {
                    context.registries().lookupOrThrow(net.minecraft.core.registries.Registries.PAINTING_VARIANT).get(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.PAINTING_VARIANT, net.minecraft.resources.ResourceLocation.parse(compoundtag1.getString("variant")))).ifPresent((p_270767_) -> {
                       p_270767_.unwrapKey().ifPresent((p_270217_) -> {
                          p_270630_.add(Component.translatable(p_270217_.location().toLanguageKey("painting", "title")).withStyle(ChatFormatting.YELLOW));
                          p_270630_.add(Component.translatable(p_270217_.location().toLanguageKey("painting", "author")).withStyle(ChatFormatting.GRAY));
                       });
                       p_270630_.add(Component.translatable("painting.dimensions", Mth.positiveCeilDiv(p_270767_.value().width(), 16), Mth.positiveCeilDiv(p_270767_.value().height(), 16)));
                    });
                } else {
                   p_270630_.add(TOOLTIP_RANDOM_VARIANT);
                }
             }
         } else if (p_270170_.isCreative()) {
            p_270630_.add(TOOLTIP_RANDOM_VARIANT);
         }
      }

   }
}
