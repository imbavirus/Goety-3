package za.co.infernos.goety.common.blocks.fluids;

import za.co.infernos.goety.Goety;
import za.co.infernos.goety.common.blocks.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Goety.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Goety.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> VOID_FLUID_TYPE = FLUID_TYPES.register("void", VoidFluidType::new);
    public static final DeferredHolder<Fluid, FlowingFluid> VOID_FLUID_SOURCE = FLUIDS.register("void", VoidFluid.Source::new);
    public static final DeferredHolder<Fluid, FlowingFluid> VOID_FLUID_FLOWING = FLUIDS.register("void_flowing", VoidFluid.Flowing::new);

    public static final DeferredHolder<FluidType, FluidType> END_MUD_FLUID_TYPE = FLUID_TYPES.register("end_mud", EndMudFluidType::new);
    public static final DeferredHolder<Fluid, FlowingFluid> END_MUD_FLUID_SOURCE = FLUIDS.register("end_mud", EndMudFluid.Source::new);
    public static final DeferredHolder<Fluid, FlowingFluid> END_MUD_FLUID_FLOWING = FLUIDS.register("end_mud_flowing", EndMudFluid.Flowing::new);

    public static void interactionInit() {
        // Water -> Void = Void Block (Source Void) / End Soil (Flowing Void)
        FluidInteractionRegistry.addInteraction(VOID_FLUID_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                NeoForgeMod.WATER_TYPE.value(),
                fluidState -> fluidState.isSource() ? ModBlocks.VOID_BLOCK.get().defaultBlockState() : ModBlocks.END_SOIL.get().defaultBlockState()
        ));

        // Void -> Water = End Rock (Source Water) / End Soil (Flowing Water)
        FluidInteractionRegistry.addInteraction(NeoForgeMod.WATER_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                VOID_FLUID_TYPE.get(),
                fluidState -> fluidState.isSource() ? ModBlocks.END_ROCK.get().defaultBlockState() : ModBlocks.END_SOIL.get().defaultBlockState()
        ));

        // Mud -> Void = Void Block (Source Void) / End Soil (Flowing Void)
        FluidInteractionRegistry.addInteraction(VOID_FLUID_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                END_MUD_FLUID_TYPE.get(),
                fluidState -> fluidState.isSource() ? ModBlocks.VOID_BLOCK.get().defaultBlockState() : ModBlocks.END_SOIL.get().defaultBlockState()
        ));

        // Void -> Mud = End Rock (Source Water) / End Soil (Flowing Water)
        FluidInteractionRegistry.addInteraction(END_MUD_FLUID_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                VOID_FLUID_TYPE.get(),
                fluidState -> fluidState.isSource() ? ModBlocks.END_ROCK.get().defaultBlockState() : ModBlocks.END_SOIL.get().defaultBlockState()
        ));

        // Void -> Lava = End Basalt (Source Void) / End Basalt (Flowing Void)
        FluidInteractionRegistry.addInteraction(VOID_FLUID_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                NeoForgeMod.LAVA_TYPE.value(),
                fluidState -> ModBlocks.END_BASALT.get().defaultBlockState()
        ));

        // Lava -> Void = End Basalt (Source Lava) / End Basalt (Flowing Lava)
        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                VOID_FLUID_TYPE.get(),
                fluidState -> ModBlocks.END_BASALT.get().defaultBlockState()
        ));

        // Mud -> Water = End Mud
        FluidInteractionRegistry.addInteraction(END_MUD_FLUID_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                NeoForgeMod.WATER_TYPE.value(),
                fluidState -> ModBlocks.END_MUD.get().defaultBlockState()
        ));

        // Water -> Mud = End Mud
        FluidInteractionRegistry.addInteraction(NeoForgeMod.WATER_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                END_MUD_FLUID_TYPE.get(),
                fluidState -> ModBlocks.END_MUD.get().defaultBlockState()
        ));

        // Mud -> Lava = Obsidian (Source Lava) / End Stone (Flowing Lava)
        FluidInteractionRegistry.addInteraction(END_MUD_FLUID_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                NeoForgeMod.LAVA_TYPE.value(),
                fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.END_STONE.defaultBlockState()
        ));

        // Lava -> Mud = End Stone Slate (Source Mud) / End Stone (Flowing Mud)
        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                END_MUD_FLUID_TYPE.get(),
                fluidState -> fluidState.isSource() ? ModBlocks.END_STONE_SLATE_BLOCK.get().defaultBlockState() : Blocks.END_STONE.defaultBlockState()
        ));

        FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                (level, currentPos, relativePos, currentState) -> level.getBlockState(currentPos.below()).is(ModBlocks.END_SOIL.get()) && level.getBlockState(relativePos).is(Blocks.BLUE_ICE),
                Blocks.OBSIDIAN.defaultBlockState()
        ));
    }
}