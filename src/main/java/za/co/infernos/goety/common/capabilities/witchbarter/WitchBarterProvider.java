package za.co.infernos.goety.common.capabilities.witchbarter;

import net.minecraft.nbt.CompoundTag;

/**
 * WitchBarterProvider now only provides save/load methods for the AttachmentType system.
 * The old capability system (CapabilityManager, ICapabilitySerializable) is no longer used.
 * See ModAttachments.WITCH_BARTER for the new registration.
 */
public class WitchBarterProvider {
    
    public static CompoundTag save(IWitchBarter witchBarter) {
        return save(new CompoundTag(), witchBarter);
    }

    public static CompoundTag save(CompoundTag tag, IWitchBarter witchBarter) {
        tag.putInt("barterTimer", witchBarter.getTimer());
        tag.putInt("barterTraderID", witchBarter.getTraderID());
        return tag;
    }

    public static IWitchBarter load(CompoundTag tag) {
        return load(tag, new WitchBarterImp());
    }

    public static IWitchBarter load(CompoundTag tag, IWitchBarter witchBarter) {
        if (tag.contains("barterTimer")) {
            witchBarter.setTimer(tag.getInt("barterTimer"));
        }
        if (tag.contains("barterTraderID")) {
            witchBarter.setTraderID(tag.getInt("barterTraderID"));
        }
        return witchBarter;
    }
}