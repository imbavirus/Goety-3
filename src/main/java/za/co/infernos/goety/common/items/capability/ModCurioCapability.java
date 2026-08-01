package za.co.infernos.goety.common.items.capability;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import za.co.infernos.goety.compat.legacy.neoforge.capabilities.Capability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import za.co.infernos.goety.compat.legacy.neoforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModCurioCapability implements ICurio {
    private final ItemStack stack;

    public ModCurioCapability(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }
}