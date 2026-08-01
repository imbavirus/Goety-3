package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.utils.ModTicketTypes;
import za.co.infernos.goety.utils.SEHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

public class ArcaBlockEntity extends OwnedBlockEntity {
    public int tickCount;
    public long ticketTime = 0;
    private float activeRotation;

    public ArcaBlockEntity(BlockPos p_155301_, BlockState p_155302_) {
        super(ModBlockEntities.ARCA.get(), p_155301_, p_155302_);
    }

    public void tick() {
        ++this.tickCount;
        ++this.activeRotation;
        if (this.getLevel() instanceof ServerLevel world) {
            ChunkPos chunkPos = this.getLevel().getChunkAt(this.worldPosition).getPos();
            if (--this.ticketTime <= 0L) {
                world.getChunkSource().addRegionTicket(ModTicketTypes.BLOCK, chunkPos, 5, this.worldPosition);
                this.ticketTime = ModTicketTypes.BLOCK.timeout() - 1L;
            }
        }
    }

    public float getActiveRotation(float p_205036_1_) {
        return (activeRotation + p_205036_1_) * -0.0375F;
    }

    public void generateParticles() {
        if (SEHelper.getSESouls(getPlayer()) <= 0){
            return;
        }
        BlockPos blockpos = this.getBlockPos();

        if (this.getLevel() != null) {
            if (!this.getLevel().isClientSide) {
                ServerLevel serverWorld = (ServerLevel) this.getLevel();
                double d0 = (double) blockpos.getX() + this.getLevel().random.nextDouble();
                double d1 = (double) blockpos.getY() + this.getLevel().random.nextDouble();
                double d2 = (double) blockpos.getZ() + this.getLevel().random.nextDouble();
                for (int p = 0; p < 4; ++p) {
                    serverWorld.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, 0, 0, 0, 0);
                    serverWorld.sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0.0D, 5.0E-4D, 0.0D, 5.0E-4D);
                }
            }
        }
    }
}
