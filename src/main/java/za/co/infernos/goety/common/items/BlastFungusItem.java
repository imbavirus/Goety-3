package za.co.infernos.goety.common.items;

import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.CuriosFinder;
import za.co.infernos.goety.utils.MathHelper;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlastFungusItem extends Item {
    public BlastFungusItem() {
        super(new Properties());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        int random = Mth.nextInt(level.random, Math.min(6, itemstack.getCount()), Math.min(10, itemstack.getCount()));
        int used = random;
        for (int i = 0; i < random; ++i) {
            MobUtil.throwBlastFungus(player, level);
            if (CuriosFinder.hasWarlockRobe(player)){
                if (level.random.nextFloat() <= 0.1F){
                    used -= 1;
                }
            }
        }
        if (!player.isSilent()) {
            level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), ModSounds.BLAST_FUNGUS_THROW.get(), player.getSoundSource(), 2.0F, 0.8F + level.random.nextFloat() * 0.4F);
        }
        itemstack.shrink(used);
        player.getCooldowns().addCooldown(ModItems.BLAST_FUNGUS.get(), MathHelper.secondsToTicks(random));
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

}
