package za.co.infernos.goety.init;

import net.minecraft.world.entity.raid.Raid;

import java.util.ArrayList;
import java.util.List;

public class RaidAdditions {

    public static final List<Raid.RaiderType> NEW_RAID_MEMBERS = new ArrayList<>();

    public static void addRaiders(){
        // NeoForge 1.21+: raid augmentation is now handled in RaidMixin.spawnGroup tail.
    }
}