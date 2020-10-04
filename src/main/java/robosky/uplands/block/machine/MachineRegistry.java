package robosky.uplands.block.machine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import io.github.cottonmc.cotton.gui.CottonCraftingController;
import robosky.uplands.block.BlockRegistry;
import robosky.uplands.block.machine.base.BaseMachineBlock;
import robosky.uplands.block.machine.base.BaseMachineBlockEntity;
import robosky.uplands.block.machine.infuser.AegisaltInfuser;
import robosky.uplands.block.machine.infuser.AegisaltInfuserBlock;
import robosky.uplands.block.machine.infuser.InfuserContainer;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;

public final class MachineRegistry {

    private MachineRegistry() {
    }

    public static final Map<Identifier, BlockEntityType<? extends BaseMachineBlockEntity>> MACHINES = new HashMap<>();

    public static final MachineEntry<AegisaltInfuserBlock, AegisaltInfuser, InfuserContainer> AEGISALT_INFUSER =
        register("aegisalt_infuser", new AegisaltInfuserBlock(), AegisaltInfuser::new, InfuserContainer::new);

    private static <B extends BaseMachineBlock, E extends BaseMachineBlockEntity, C extends CottonCraftingController>
    MachineEntry<B, E, C> register(String name, B b, Supplier<E> e, @Nullable MachineScreenFactory<C> gui) {
        BlockRegistry.registerWithItem(name, b);
        return register(b, e, gui);
    }

    private static <B extends BaseMachineBlock, E extends BaseMachineBlockEntity, C extends CottonCraftingController>
    MachineEntry<B, E, C> register(B b, Supplier<E> e, @Nullable MachineScreenFactory<C> gui) {
        Identifier id = Registry.BLOCK.getId(b);
        BlockEntityType<E> t = new BlockEntityType<>(e, ImmutableSet.of(b), null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id, t);
        MACHINES.put(id, t);
        if(gui != null) {
            ContainerProviderRegistry.INSTANCE.registerFactory(id, (syncId, id2, player, buf) ->
                gui.create(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())));
        }
        return new MachineEntry<>(b, e, gui, t);
    }

    public static void init() {
    }

    @FunctionalInterface
    public interface MachineScreenFactory<C extends CottonCraftingController> {
        C create(int syncId, PlayerInventory inv, ScreenHandlerContext ctx);
    }

    public static final class MachineEntry<B extends BaseMachineBlock, E extends BaseMachineBlockEntity, C extends CottonCraftingController> {
        public final B block;
        public final Supplier<E> be;
        @Nullable
        public final MachineScreenFactory<C> gui;
        public final BlockEntityType<E> type;

        private MachineEntry(B b, Supplier<E> e, @Nullable MachineScreenFactory<C> gui, BlockEntityType<E> type) {
            this.block = b;
            this.be = e;
            this.gui = gui;
            this.type = type;
        }
    }
}
