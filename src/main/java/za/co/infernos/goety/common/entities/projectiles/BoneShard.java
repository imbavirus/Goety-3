package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.api.entities.IOwned;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.MobUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import za.co.infernos.goety.common.items.ModItems;
import org.jetbrains.annotations.NotNull;

public class BoneShard extends AbstractArrow {
    public BoneShard(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    public BoneShard(double p_36712_, double p_36713_, double p_36714_, Level p_36715_) {
        super(ModEntityType.BONE_SHARD.get(), p_36712_, p_36713_, p_36714_, p_36715_, new ItemStack(ModItems.BONE_SHARD.get()), ItemStack.EMPTY);
    }

    public BoneShard(LivingEntity p_36718_, Level p_36719_) {
        super(ModEntityType.BONE_SHARD.get(), p_36718_, p_36719_, new ItemStack(ModItems.BONE_SHARD.get()), ItemStack.EMPTY);
    }

    protected boolean tryPickup(Player p_150196_) {
        return false;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.BONE_SHARD.get());
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.BONE_SHARD_IMPACT.get();
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null){
            if (pEntity == this.getOwner()){
                return false;
            }
            if (this.getOwner() instanceof Mob mob && mob.getTarget() == pEntity){
                return super.canHitEntity(pEntity);
            } else {
                if (MobUtil.areAllies(this.getOwner(), pEntity)){
                    return false;
                }
                if (pEntity instanceof IOwned owned0 && this.getOwner() instanceof IOwned owned1){
                    return !MobUtil.ownerStack(owned0, owned1);
                }
            }
        }
        return super.canHitEntity(pEntity);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity p_345759_) {
        return new net.minecraft.network.protocol.game.ClientboundAddEntityPacket(this, p_345759_);
    }
}