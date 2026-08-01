package za.co.infernos.goety.common.advancements;

import za.co.infernos.goety.Goety;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModCriteriaTriggers {
    private static KilledTrigger SERVANT_KILLED_ENTITY_INTERNAL;
    private static PlayerTrigger SERVANT_RAID_VICTORY_INTERNAL;

    public static void register(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.TRIGGER_TYPE) {
            SERVANT_KILLED_ENTITY_INTERNAL = CriteriaTriggers.register(Goety.location("servant_killed_entity").toString(), new KilledTrigger());
            SERVANT_RAID_VICTORY_INTERNAL = CriteriaTriggers.register(Goety.location("servant_raid_victory").toString(), new PlayerTrigger());
        }
    }

    public static KilledTrigger SERVANT_KILLED_ENTITY() {
        if (SERVANT_KILLED_ENTITY_INTERNAL == null) {
            throw new IllegalStateException("ModCriteriaTriggers not registered! Ensure register() is called during RegisterEvent for TRIGGER_TYPE.");
        }
        return SERVANT_KILLED_ENTITY_INTERNAL;
    }

    public static PlayerTrigger SERVANT_RAID_VICTORY() {
        if (SERVANT_RAID_VICTORY_INTERNAL == null) {
            throw new IllegalStateException("ModCriteriaTriggers not registered! Ensure register() is called during RegisterEvent for TRIGGER_TYPE.");
        }
        return SERVANT_RAID_VICTORY_INTERNAL;
    }
}