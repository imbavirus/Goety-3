package za.co.infernos.goetied.init;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    public static KeyMapping[] keyBindings = new KeyMapping[16];

    static {
        keyBindings[0] = new KeyMapping("key.goetied.wand", GLFW.GLFW_KEY_Z, "key.goetied.category");
        keyBindings[1] = new KeyMapping("key.goetied.focusCircle", GLFW.GLFW_KEY_X, "key.goetied.category");
        keyBindings[2] = new KeyMapping("key.goetied.bag", GLFW.GLFW_KEY_C, "key.goetied.category");
        keyBindings[3] = new KeyMapping("key.goetied.witch.robe", GLFW.GLFW_KEY_V, "key.goetied.witch.category");
        keyBindings[4] = new KeyMapping("key.goetied.ceaseFire", GLFW.GLFW_KEY_B, "key.goetied.category");
        keyBindings[5] = new KeyMapping("key.goetied.lich.magnet", GLFW.GLFW_KEY_R, "key.goetied.lich.category");
        keyBindings[6] = new KeyMapping("key.goetied.lich.nightVision", GLFW.GLFW_KEY_M, "key.goetied.lich.category");
        keyBindings[7] = new KeyMapping("key.goetied.witch.extractPotions", GLFW.GLFW_KEY_G, "key.goetied.witch.category");
        keyBindings[8] = new KeyMapping("key.goetied.witch.brewBag", GLFW.GLFW_KEY_H, "key.goetied.witch.category");
        keyBindings[9] = new KeyMapping("key.goetied.witch.brewCircle", GLFW.GLFW_KEY_J, "key.goetied.witch.category");
        keyBindings[10] = new KeyMapping("key.goetied.mount.roar", GLFW.GLFW_KEY_R, "key.goetied.mount.category");
        keyBindings[11] = new KeyMapping("key.goetied.mount.freeRoam", GLFW.GLFW_KEY_H, "key.goetied.mount.category");
        keyBindings[12] = new KeyMapping("key.goetied.lich.lichForm", GLFW.GLFW_KEY_N, "key.goetied.lich.category");
        keyBindings[13] = new KeyMapping("key.goetied.lich.laugh", GLFW.GLFW_KEY_K, "key.goetied.lich.category");
        keyBindings[14] = new KeyMapping("key.goetied.activate_curio", GLFW.GLFW_KEY_G, "key.goetied.category");
        keyBindings[15] = new KeyMapping("key.goetied.dismiss", GLFW.GLFW_KEY_KP_DECIMAL, "key.goetied.category");
    }

    public static void register(RegisterKeyMappingsEvent event) {
        for (KeyMapping keyBinding : keyBindings) {
            event.register(keyBinding);
        }
    }

    /** @deprecated kept as a no-op for legacy callers; registration now happens via {@link #register}. */
    @Deprecated
    public static void init() {
    }

    public static KeyMapping wandSlot(){
        if (keyBindings[0] != null) {
            return keyBindings[0];
        } else {
            return null;
        }
    }

    public static KeyMapping wandCircle(){
        if (keyBindings[1] != null) {
            return keyBindings[1];
        } else {
            return null;
        }
    }

    public static KeyMapping brewCircle(){
        if (keyBindings[9] != null) {
            return keyBindings[9];
        } else {
            return null;
        }
    }

}
