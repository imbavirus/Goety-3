package za.co.infernos.goety.init;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.capabilities.lichdom.ILichdom;
import za.co.infernos.goety.common.capabilities.lichdom.LichImp;
import za.co.infernos.goety.common.capabilities.misc.IMisc;
import za.co.infernos.goety.common.capabilities.misc.MiscImp;
import za.co.infernos.goety.common.capabilities.soulenergy.ISoulEnergy;
import za.co.infernos.goety.common.capabilities.soulenergy.SEImp;
import za.co.infernos.goety.common.capabilities.witchbarter.IWitchBarter;
import za.co.infernos.goety.common.capabilities.witchbarter.WitchBarterImp;
import za.co.infernos.goety.utils.LichdomHelper;
import za.co.infernos.goety.utils.MiscCapHelper;
import za.co.infernos.goety.utils.SEHelper;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Registry for data attachments (replacement for the old Capability system in NeoForge 1.21+)
 */
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Goety.MOD_ID);

    private static CompoundTag saveLichdom(LichImp lichdom) {
        return LichdomHelper.save((ILichdom) lichdom);
    }

    private static LichImp loadLichdom(CompoundTag tag) {
        ILichdom loaded = LichdomHelper.load(tag);
        return loaded instanceof LichImp ? (LichImp) loaded : new LichImp();
    }

    private static CompoundTag saveSoulEnergy(SEImp se) {
        return SEHelper.save((ISoulEnergy) se);
    }

    private static SEImp loadSoulEnergy(CompoundTag tag) {
        ISoulEnergy loaded = SEHelper.load(tag);
        return loaded instanceof SEImp ? (SEImp) loaded : new SEImp();
    }

    private static CompoundTag saveMisc(MiscImp misc) {
        return MiscCapHelper.save((IMisc) misc);
    }

    private static MiscImp loadMisc(CompoundTag tag) {
        IMisc loaded = MiscCapHelper.load(tag);
        return loaded instanceof MiscImp ? (MiscImp) loaded : new MiscImp();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Codec<LichImp> createLichdomCodec() {
        Codec tagCodec = CompoundTag.CODEC;
        return (Codec<LichImp>) tagCodec.xmap(
                (Object tag) -> loadLichdom((CompoundTag) tag),
                (Object lichdom) -> saveLichdom((LichImp) lichdom)
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Codec<SEImp> createSoulEnergyCodec() {
        Codec tagCodec = CompoundTag.CODEC;
        return (Codec<SEImp>) tagCodec.xmap(
                (Object tag) -> loadSoulEnergy((CompoundTag) tag),
                (Object se) -> saveSoulEnergy((SEImp) se)
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Codec<MiscImp> createMiscCodec() {
        Codec tagCodec = CompoundTag.CODEC;
        return (Codec<MiscImp>) tagCodec.xmap(
                (Object tag) -> loadMisc((CompoundTag) tag),
                (Object misc) -> saveMisc((MiscImp) misc)
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final Supplier<AttachmentType<ILichdom>> LICHDOM = ATTACHMENT_TYPES.register(
            "lichdom", () -> {
                Codec codec = createLichdomCodec();
                AttachmentType.Builder builder = AttachmentType.builder(() -> new LichImp());
                return builder.serialize(codec).copyOnDeath().build();
            }
    );

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final Supplier<AttachmentType<ISoulEnergy>> SOUL_ENERGY = ATTACHMENT_TYPES.register(
            "soul_energy", () -> {
                Codec codec = createSoulEnergyCodec();
                AttachmentType.Builder builder = AttachmentType.builder(() -> new SEImp());
                return builder.serialize(codec).copyOnDeath().build();
            }
    );

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final Supplier<AttachmentType<IMisc>> MISC = ATTACHMENT_TYPES.register(
            "misc", () -> {
                Codec codec = createMiscCodec();
                AttachmentType.Builder builder = AttachmentType.builder(() -> new MiscImp());
                return builder.serialize(codec).copyOnDeath().build();
            }
    );

    public static final Supplier<AttachmentType<IWitchBarter>> WITCH_BARTER = ATTACHMENT_TYPES.register(
            "witch_barter", () -> AttachmentType.builder(() -> (IWitchBarter) new WitchBarterImp())
                    .build()
    );

    public static void init(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
