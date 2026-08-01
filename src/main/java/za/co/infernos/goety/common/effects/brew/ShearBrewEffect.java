package za.co.infernos.goety.common.effects.brew;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.IShearable;

import javax.annotation.Nullable;

public class ShearBrewEffect extends BrewEffect {
    public ShearBrewEffect(int soulCost) {
        super("shear", soulCost, MobEffectCategory.BENEFICIAL, 0x6f352b);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public boolean canLinger() {
        return true;
    }

    public void applyEntityEffect(LivingEntity pTarget, @Nullable Entity pSource, @Nullable Entity pIndirectSource, int pAmplifier){
        if (pTarget instanceof IShearable shearable){
            Player player = pIndirectSource instanceof Player ? (Player) pIndirectSource : null;
            if (shearable.isShearable(player, new ItemStack(Items.SHEARS), pTarget.level(), pTarget.blockPosition())){
                java.util.List<ItemStack> drops = shearable.onSheared(player, new ItemStack(Items.SHEARS), pTarget.level(), pTarget.blockPosition());
                java.util.Random rand = new java.util.Random();
                drops.forEach(d -> {
                    ItemEntity ent = pTarget.spawnAtLocation(d, 1.0F);
                    if (ent != null){
                        ent.setDeltaMovement(ent.getDeltaMovement().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                    }
                });
            }
        }
    }
}
