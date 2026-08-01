package za.co.infernos.goety.common.entities.projectiles;

import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.init.ModSounds;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import za.co.infernos.goety.common.items.ModItems;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Harpoon extends AbstractArrow {
    public Harpoon(EntityType<? extends AbstractArrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    public Harpoon(Level p_36866_, LivingEntity p_36867_, ItemStack itemStack) {
        super(ModEntityType.HARPOON.get(), p_36867_, p_36866_, itemStack, null);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(net.minecraft.world.item.Items.ARROW);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntityType.HARPOON.get();
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.HARPOON_HIT.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInWater()){
            this.setSoundEvent(ModSounds.HARPOON_HIT_WATER.get());
        } else {
            this.setSoundEvent(ModSounds.HARPOON_HIT.get());
        }
    }

    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        if (this.isInWater()){
            this.setSoundEvent(ModSounds.HARPOON_HIT_WATER.get());
        } else {
            this.setSoundEvent(ModSounds.HARPOON_HIT.get());
        }
    }



    // @Override
    // public Packet<ClientGamePacketListener> getAddEntityPacket() {
    //    return new net.minecraft.network.protocol.game.ClientboundAddEntityPacket(this);
    // }
}