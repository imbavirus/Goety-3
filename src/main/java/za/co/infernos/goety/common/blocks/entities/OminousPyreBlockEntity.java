package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.entities.ally.illager.ITrainIllager;
import za.co.infernos.goety.api.entities.ally.illager.IllagerType;
import za.co.infernos.goety.common.entities.ally.illager.AbstractIllagerServant;
import za.co.infernos.goety.config.MobsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class OminousPyreBlockEntity extends BarracksBlockEntity {

    public OminousPyreBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.OMINOUS_PYRE.get(), p_155229_, p_155230_);
    }

    @Override
    public Predicate<Mob> trainingRequirements(Level level, BlockPos blockPos) {
        EntityType<? extends Mob> entityType = this.getTrainedMob(level, blockPos);
        if (entityType != null) {
            ITrainIllager illagerType = IllagerType.getIllagerFromType(level, blockPos, this.getRange(), entityType);
            return mob -> mob instanceof AbstractIllagerServant illager
                    && illagerType != null
                    && illagerType.mobCanTrainTo(illager, level, blockPos, this.getRange())
                    && (illager.getTrueOwner() == this.getTrueOwner() || illager.getTrueOwner() == null);
        }
        return mob -> false;
    }

    @Override
    public boolean autoMode() {
        return za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.IllagerServantAutoTrain, false);
    }

    @Override
    public void setVariant(Level level, BlockPos blockPos) {
        if (!IllagerType.getIllagerList().isEmpty()) {
            EntityType<? extends Mob> entityType = null;
            for (IllagerType illagerType : IllagerType.values()) {
                if (illagerType.getIllager() != null) {
                    if (illagerType.getIllager().canSpawn(level, blockPos, this.getRange())) {
                        entityType = illagerType.getIllager().getIllager(level, blockPos, this.getRange());
                        break;
                    }
                }
            }
            this.setEntityType(entityType);
        }
    }
    @Override
    public boolean handleGameEvent(net.minecraft.server.level.ServerLevel level, net.minecraft.core.Holder<net.minecraft.world.level.gameevent.GameEvent> event, net.minecraft.world.level.gameevent.GameEvent.Context context, net.minecraft.world.phys.Vec3 pos) {
        return false;
    }
}