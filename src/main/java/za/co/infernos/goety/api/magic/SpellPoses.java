package za.co.infernos.goety.api.magic;

import net.minecraft.client.model.HumanoidModel;

public class SpellPoses {
    /**
     * 1.21+: NeoForge switched ArmPose customization back to the enum extension system (no {@code ArmPose.create}).
     * Until we re-register these poses through the enum extension pipeline, fall back to vanilla poses.
     */
    public static final HumanoidModel.ArmPose SPELL = HumanoidModel.ArmPose.ITEM;

    public static final HumanoidModel.ArmPose FLIGHT_POSE = HumanoidModel.ArmPose.EMPTY;

    public static final HumanoidModel.ArmPose HOLD_STAFF = HumanoidModel.ArmPose.ITEM;
}
