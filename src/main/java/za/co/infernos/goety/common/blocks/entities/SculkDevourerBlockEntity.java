package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.blocks.IEnchantedBlock;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.blocks.SculkDevourerBlock;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.utils.SEHelper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public class SculkDevourerBlockEntity extends OwnedBlockEntity implements GameEventListener, IEnchantedBlock {
    private final BlockPositionSource blockPosSource = new BlockPositionSource(this.worldPosition);
    protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();

    public SculkDevourerBlockEntity(BlockPos p_222774_, BlockState p_222775_) {
        super(ModBlockEntities.SCULK_DEVOURER.get(), p_222774_, p_222775_);
    }

    public Object2IntMap<Enchantment> getEnchantments(){
        return this.enchantments;
    }

    @Override
    public void readNetwork(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries) {
        super.readNetwork(tag, pRegistries);
        this.loadEnchants(tag);
    }

    @Override
    public CompoundTag writeNetwork(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries) {
        this.saveEnchants(tag, ModBlocks.SCULK_DEVOURER.get().asItem());
        return super.writeNetwork(tag, pRegistries);
    }

    public PositionSource getListenerSource() {
        return this.blockPosSource;
    }

    public GameEventListener.DeliveryMode getDeliveryMode() {
        return GameEventListener.DeliveryMode.BY_DISTANCE;
    }

    public int getListenerRadius() {
        int radius = 8;
        radius *= this.enchantments.getOrDefault(ModEnchantments.RADIUS.get(), 0) + 1;
        return radius;
    }

    @Override
    public boolean handleGameEvent(ServerLevel p_222777_, net.minecraft.core.Holder<GameEvent> p_282184_, GameEvent.Context p_283014_, Vec3 p_282350_) {
        if (!this.isRemoved()) {
            if (p_282184_.is(GameEvent.ENTITY_DIE)) {
                Entity $ = p_283014_.sourceEntity();
                if ($ instanceof LivingEntity livingentity) {
                    if (!livingentity.wasExperienceConsumed() && this.getPlayer() != null && SEHelper.getSoulsContainer(this.getPlayer())) {
                        int i = livingentity.getExperienceReward(p_222777_, $);
                        if (livingentity.shouldDropExperience() && i > 0) {
                            i *= this.enchantments.getOrDefault(ModEnchantments.SOUL_EATER.get(), 0) + 1;
                            SEHelper.increaseSouls(this.getPlayer(), i);
                        }

                        livingentity.skipDropExperience();
                        SculkDevourerBlock.bloom(p_222777_, this.worldPosition, this.getBlockState(), p_222777_.getRandom());
                    }

                    return true;
                }
            }

        }
        return false;
    }
}