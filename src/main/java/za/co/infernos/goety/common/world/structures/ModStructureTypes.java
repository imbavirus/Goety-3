package za.co.infernos.goety.common.world.structures;

import za.co.infernos.goety.Goety;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, Goety.MOD_ID);
    
    public static final DeferredHolder<StructureType<?>, StructureType<CryptStructure>> CRYPT = 
            STRUCTURE_TYPE.register("crypt", () -> new StructureType<CryptStructure>() {
                @Override
                public MapCodec<CryptStructure> codec() {
                    return CryptStructure.MAP_CODEC;
                }
            });
    
    public static final DeferredHolder<StructureType<?>, StructureType<FinalTerminalStructure>> FINAL_TERMINAL = 
            STRUCTURE_TYPE.register("final_terminal", () -> new StructureType<FinalTerminalStructure>() {
                @Override
                public MapCodec<FinalTerminalStructure> codec() {
                    return FinalTerminalStructure.MAP_CODEC;
                }
            });
}