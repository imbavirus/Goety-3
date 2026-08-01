package za.co.infernos.goety.common.events.spell;

import za.co.infernos.goety.api.magic.ISpell;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * CastMagicEvent is fired when finished using {@link za.co.infernos.goety.common.items.magic.DarkWand} to cast spell. <br>
 * <br>
 * This event is fired via the {@link GoetyEventFactory#onCastSpell(LivingEntity, ISpell)}.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, the spell is not cast.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link NeoForge#EVENT_BUS}.
 **/
import net.neoforged.bus.api.ICancellableEvent;

public class CastMagicEvent extends LivingEvent implements ICancellableEvent {
    private ISpell spell;

    public CastMagicEvent(LivingEntity entity, ISpell spell) {
        super(entity);
        this.spell = spell;
    }

    public ISpell getSpell(){
        return this.spell;
    }

    public void setSpell(ISpell spell) {
        this.spell = spell;
    }
}