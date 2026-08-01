package za.co.infernos.goety.common.events.spell;

import za.co.infernos.goety.api.magic.ISpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * CastingMagicEvent is fired when stopped using {@link za.co.infernos.goety.common.items.magic.DarkWand} to cast a spell. <br>
 * <br>
 * This event is fired via the {@link GoetyEventFactory#onStopSpell(LivingEntity, ItemStack, ISpell, int, int)}.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, the spell is not cast and no effects that happen when stopped in the middle of casting will occur.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link NeoForge#EVENT_BUS}.
 **/
import net.neoforged.bus.api.ICancellableEvent;

public class StopMagicEvent extends LivingEvent implements ICancellableEvent {
    private final ISpell spell;
    private final ItemStack useItem;
    private final int castTime;
    private final int timeRemaining;

    public StopMagicEvent(LivingEntity entity, ItemStack useItem, ISpell spell, int castTime, int timeRemaining) {
        super(entity);
        this.useItem = useItem;
        this.spell = spell;
        this.castTime = castTime;
        this.timeRemaining = timeRemaining;
    }

    public ISpell getSpell(){
        return this.spell;
    }

    public ItemStack getUseItem() {
        return this.useItem;
    }

    public int castingTime() {
        return this.castTime;
    }

    public int getTimeRemaining() {
        return this.timeRemaining;
    }
}