package za.co.infernos.goety.common.blocks.entities;

import za.co.infernos.goety.api.blocks.IEnchantedBlock;
import za.co.infernos.goety.client.particles.AbsorbTrailParticleOption;
import za.co.infernos.goety.client.particles.GatherTrailParticle;
import za.co.infernos.goety.client.particles.SpirallingParticleOption;
import za.co.infernos.goety.common.blocks.ModBlocks;
import za.co.infernos.goety.common.effects.GoetyEffects;
import za.co.infernos.goety.common.enchantments.ModEnchantments;
import za.co.infernos.goety.config.ItemConfig;
import za.co.infernos.goety.init.ModSounds;
import za.co.infernos.goety.utils.ColorUtil;
import za.co.infernos.goety.utils.MobUtil;
import za.co.infernos.goety.utils.ModDamageSource;
import za.co.infernos.goety.utils.SEHelper;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BlackCrystalBlockEntity extends OwnedBlockEntity implements IEnchantedBlock {
    protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();
    public int tickCount;
    public int attackTick;
    @Nullable
    private LivingEntity target;

    public BlackCrystalBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.BLACK_CRYSTAL.get(), p_155229_, p_155230_);
    }

    @Override
    public void readNetwork(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries) {
        super.readNetwork(tag, pRegistries);
        this.loadEnchants(tag);
    }

    @Override
    public CompoundTag writeNetwork(CompoundTag tag, net.minecraft.core.HolderLookup.Provider pRegistries) {
        this.saveEnchants(tag, ModBlocks.BLACK_CRYSTAL.get().asItem());
        return super.writeNetwork(tag, pRegistries);
    }

    public Object2IntMap<Enchantment> getEnchantments(){
        return this.enchantments;
    }

    public void tick() {
        if (this.getLevel() != null) {
            ++this.tickCount;
            if (this.getLevel() instanceof ServerLevel serverLevel) {
                int radius = 8 + this.enchantments.getOrDefault(ModEnchantments.RADIUS.get(), 0);
                if (this.target == null) {
                    for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(this.worldPosition).inflate(radius * 2))) {
                        if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && livingEntity.distanceToSqr(this.getBlockPos().getCenter()) <= Mth.square(radius)) {
                            boolean flag = livingEntity.getHealth() >= livingEntity.getMaxHealth() * 0.25F;
                            if (this.getTrueOwner() != null) {
                                flag &= !MobUtil.areAllies(this.getTrueOwner(), livingEntity);
                            }
                            if (flag) {
                                this.target = livingEntity;
                            }
                        }
                    }
                } else {
                    if (!this.target.isAlive() || this.target.getHealth() < this.target.getMaxHealth() * 0.25F || this.target.distanceToSqr(this.getBlockPos().getCenter()) > Mth.square(radius)) {
                        this.attackTick = 0;
                        this.target = null;
                    } else {
                        ++this.attackTick;
                        if (this.attackTick % 20 == 0) {
                            Vec3 vector3d1 = this.getBlockPos().getCenter();
                            DamageSource damageSource = this.getTrueOwner() != null ? ModDamageSource.soulLeech(this.getTrueOwner(), this.getTrueOwner()) : this.getLevel().damageSources().magic();
                            this.target.addEffect(new MobEffectInstance(GoetyEffects.CURSED, 5, 0, false, false));
                            if (this.target.hurt(damageSource, this.target.getMaxHealth() * 0.1F)) {
                                ColorUtil colorUtil1 = new ColorUtil(0x5038dd);
                                serverLevel.sendParticles(new SpirallingParticleOption(1.0F, colorUtil1.red, colorUtil1.green, colorUtil1.blue, 5), vector3d1.x, vector3d1.y, vector3d1.z, 1, 0.0D, 0.0D, 0.0D, 1.0D);
                                Vec3 vec3 = new Vec3(this.target.getX(), this.target.getY() + (this.target.getBbHeight() / 2.0F), this.target.getZ());
                                serverLevel.sendParticles(new GatherTrailParticle.Option(colorUtil1, vector3d1), vec3.x, vec3.y, vec3.z, 0, 0.0F, 0.0F, 0.0F, 0.5F);
                                for (int i = 0; i < 8; ++i) {
                                    Vec3 vec31 = new Vec3(this.target.getRandomX(1.0F), this.target.getRandomY(), this.target.getRandomZ(1.0F));
                                    serverLevel.sendParticles(new AbsorbTrailParticleOption(vector3d1, 0x5038dd, 10), vec31.x, vec31.y, vec31.z, 1, 0.0, 0.0, 0.0, 0.0);
                                }
                                this.getLevel().playSound(null, this.worldPosition, ModSounds.SOUL_EAT.get(), SoundSource.BLOCKS, 2.0F, 1.0F);
                                if (this.getTrueOwner() instanceof Player player) {
                                    int enchantment = this.enchantments.getOrDefault(ModEnchantments.SOUL_EATER.get(), 0);
                                    int soulEater = Mth.clamp(enchantment + 1, 1, 10);
                                    SEHelper.increaseSouls(player, za.co.infernos.goety.utils.ConfigHelper.getInt(ItemConfig.DarkScytheSouls, 1) * soulEater);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
