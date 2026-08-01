package za.co.infernos.goetied.common.research;

import za.co.infernos.goetied.Goetied;
import net.minecraft.resources.ResourceLocation;

public class Research {
    public String id;

    public Research(String id){
        this.id = id;
    }

    public ResourceLocation getLocation(){
        return Goetied.location(this.id);
    }

    public String getId(){
        return this.id;
    }
}