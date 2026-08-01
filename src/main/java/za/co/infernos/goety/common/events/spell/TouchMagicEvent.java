package za.co.infernos.goety.common.events.spell;

import za.co.infernos.goety.api.magic.ISpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * CastMagicEvent is fired when right-clicking on a target {@link za.co.infernos.goety.common.items.magic.DarkWand} with a spell. <br>
 * <br>
 * This event is fired via the {@link GoetyEventFactory#onTouchBasedSpell(LivingEntity, ItemStack, ISpell)}.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, the spell is not cast.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link NeoForge#EVENT_BUS}.
 **/
import net.neoforged.bus.api.ICancellableEvent;

public class TouchMagicEvent extends LivingEvent implements ICancellableEvent {
    private ISpell spell;
    private final ItemStack useItem;

    public TouchMagicEvent(LivingEntity entity, ItemStack useItem, ISpell spell) {
        super(entity);
        this.useItem = useItem;
        this.spell = spell;
    }

    public ISpell getSpell(){
        return this.spell;
    }

    public void setSpell(ISpell spell) {
        this.spell = spell;
    }

    public ItemStack getUseItem() {
        return this.useItem;
    }
}