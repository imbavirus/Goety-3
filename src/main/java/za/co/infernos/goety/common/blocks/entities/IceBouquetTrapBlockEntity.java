package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.common.blocks.IceBouquetTrapBlock;
import za.co.infernos.goety.common.entities.projectiles.IceBouquet;
import za.co.infernos.goety.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

public class IceBouquetTrapBlockEntity extends OwnedBlockEntity {
    public int activated;
    public int ticks;
    public boolean firing;

    public IceBouquetTrapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ICE_BOUQUET_TRAP.get(), blockPos, blockState);
    }

    public void fire(){
        if (!this.firing) {
            this.playSound(ModSounds.SUMMON_SPELL.get());
            this.ticks = 0;
            this.firing = true;
        }
    }

    public void tick() {
        if (!this.getLevel().isClientSide) {
            if (this.firing) {
                ++this.ticks;
            }
            if (this.ticks == 1) {
                this.activated = 20;
                BlockPos blockPos = this.getBlockPos().above();
                IceBouquet ghostFire = new IceBouquet(this.getLevel(), blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F, this.getTrueOwner());
                ghostFire.setSoulEating(true);
                this.getLevel().addFreshEntity(ghostFire);
            }
            if (this.ticks >= 70) {
                this.firing = false;
                this.ticks = 0;
            }
            if (this.activated != 0) {
                --this.activated;
                this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(IceBouquetTrapBlock.POWERED, true), 3);
            } else {
                this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(IceBouquetTrapBlock.POWERED, false), 3);
            }
        }
    }

    public void playSound(SoundEvent sound) {
        this.getLevel().playSound(null, this.worldPosition, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void setRemoved() {
        super.setRemoved();
    }
}
