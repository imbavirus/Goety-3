package za.co.infernos.goety.mixin;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.entities.ModEntityType;
import za.co.infernos.goety.config.MobsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Shadow
    @Final
    private ServerLevel level;
    @Unique
    private int goety$lastAugmentedWave = -1;

    @Shadow
    public abstract int getGroupsSpawned();

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), method = "spawnGroup")
    private Raider spawnCustomRaider(Raider raider, BlockPos blockPos) {
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.ArmoredRavagerRaid, false)){
            if (this.level.random.nextFloat() < (0.25F + this.level.getCurrentDifficultyAt(raider.blockPosition()).getSpecialMultiplier())) {
                if (raider.getType() == EntityType.RAVAGER){
                    raider = ModEntityType.ARMORED_RAVAGER.get().create(this.level);
                    if (raider != null){
                        return raider;
                    }
                }
            }
        }
        return raider;
    }

    @Inject(method = "spawnGroup", at = @At("TAIL"))
    private void goety$spawnConfiguredRaiders(BlockPos blockPos, CallbackInfo ci) {
        int wave = this.getGroupsSpawned();
        if (wave <= 0 || this.goety$lastAugmentedWave == wave) {
            return;
        }
        this.goety$lastAugmentedWave = wave;
        int injected = 0;

        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.WARLOCK.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.WarlockRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.WarlockRaidCount, java.util.Arrays.asList(0, 0, 0, 0, 1, 2, 0, 1)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.MAVERICK.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.MaverickRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.MaverickRaidCount, java.util.Arrays.asList(0, 1, 0, 1, 0, 0, 0, 1)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.HERETIC.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HereticRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.HereticRaidCount, java.util.Arrays.asList(0, 0, 0, 1, 0, 0, 2, 1)), wave);

        if (!za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.IllagerRaid, false)) {
            if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.RaidAugmentDebug, false) && injected > 0) {
                Goety.LOGGER.info("Goety raid augment: wave {} injected {} custom raiders", wave, injected);
            }
            return;
        }

        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.PIKER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.PikerRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.PikerRaidCount, java.util.Arrays.asList(0, 0, 0, 2, 0, 3, 3, 5)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.RIPPER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.RipperRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.RipperRaidCount, java.util.Arrays.asList(0, 0, 0, 4, 0, 6, 6, 10)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.CRUSHER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.CrusherRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.CrusherRaidCount, java.util.Arrays.asList(0, 0, 0, 0, 2, 2, 0, 2)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.STORM_CASTER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.StormCasterRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.StormCasterRaidCount, java.util.Arrays.asList(0, 0, 0, 0, 1, 1, 0, 2)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.CRYOLOGER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.CryologerRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.CryologerRaidCount, java.util.Arrays.asList(0, 0, 1, 1, 0, 0, 0, 2)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.PREACHER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.PreacherRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.PreacherRaidCount, java.util.Arrays.asList(0, 0, 1, 1, 0, 0, 0, 2)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.CONQUILLAGER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.ConquillagerRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.ConquillagerRaidCount, java.util.Arrays.asList(0, 4, 3, 3, 4, 4, 4, 2)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.INQUILLAGER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.InquillagerRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.InquillagerRaidCount, java.util.Arrays.asList(0, 0, 2, 0, 1, 2, 2, 3)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.ENVIOKER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.EnviokerRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.EnviokerRaidCount, java.util.Arrays.asList(0, 0, 0, 1, 0, 1, 1, 2)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.SORCERER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.SorcererRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.SorcererRaidCount, java.util.Arrays.asList(0, 0, 1, 0, 1, 0, 1, 1)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.MINISTER.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.MinisterRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.MinisterRaidCount, java.util.Arrays.asList(0, 0, 0, 0, 0, 0, 0, 1)), wave);
        injected += this.goety$spawnWaveRaiders(blockPos, ModEntityType.HOSTILE_REDSTONE_GOLEM.get(), za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HostileRedstoneGolemRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.HostileRedstoneGolemRaidCount, java.util.Arrays.asList(0, 0, 0, 0, 0, 1, 1, 0)), wave);

        EntityType<? extends Raider> hrmType = ModEntityType.HOSTILE_REDSTONE_MONSTROSITY.get();
        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HRMSpawnNoRaiders, false)) {
            hrmType = ModEntityType.RAID_BOSS_SUMMON.get();
        }
        injected += this.goety$spawnWaveRaiders(blockPos, hrmType, za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.HostileRedstoneMonstrosityRaid, true), za.co.infernos.goety.utils.ConfigHelper.getList(MobsConfig.HostileRedstoneMonstrosityRaidCount, java.util.Arrays.asList(0, 0, 0, 0, 0, 0, 0, 1)), wave);

        if (za.co.infernos.goety.utils.ConfigHelper.getBoolean(MobsConfig.RaidAugmentDebug, false) && injected > 0) {
            Goety.LOGGER.info("Goety raid augment: wave {} injected {} custom raiders", wave, injected);
        }
    }

    private int goety$spawnWaveRaiders(BlockPos center, EntityType<? extends Raider> type, boolean enabled, List<? extends Integer> waveCounts, int wave) {
        if (!enabled || waveCounts == null || waveCounts.isEmpty()) {
            return 0;
        }

        int index = Math.max(0, Math.min(waveCounts.size() - 1, wave - 1));
        int count = Math.max(0, waveCounts.get(index));
        int injected = 0;
        Raid raid = (Raid) (Object) this;
        for (int i = 0; i < count; i++) {
            Raider raider = type.create(this.level);
            if (raider == null) {
                continue;
            }
            BlockPos spawnPos = center.offset(this.level.random.nextInt(7) - 3, 0, this.level.random.nextInt(7) - 3);
            raider.finalizeSpawn(this.level, this.level.getCurrentDifficultyAt(spawnPos), MobSpawnType.EVENT, null);
            raider.setCanJoinRaid(true);
            raider.setCurrentRaid(raid);
            raider.setWave(wave);
            raider.setTicksOutsideRaid(0);
            raider.setPos(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D);
            this.level.addFreshEntity(raider);
            injected++;
        }
        return injected;
    }
}
