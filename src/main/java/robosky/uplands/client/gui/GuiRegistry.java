package robosky.uplands.client.gui;

import java.util.function.BiFunction;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import robosky.uplands.block.machine.MachineRegistry;
import robosky.uplands.block.machine.infuser.InfuserContainer;
import scala.Function3;

import net.minecraft.block.Block;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

// delete this class?
class InfuserScreen extends CottonInventoryScreen<InfuserContainer> {

    public InfuserScreen(InfuserContainer container, PlayerEntity player) {
        super(container, player);
    }
}

public final class GuiRegistry {

    private GuiRegistry() {
    }

    private static <C extends CottonCraftingController> void registerGui(
        MachineRegistry.MachineEntry<? extends Block, ?, C> entry,
        BiFunction<C, PlayerEntity, CottonInventoryScreen<C>> screen) {
        Identifier id = Registry.BLOCK.getId(entry.block());
        Function3<Object, PlayerInventory, BlockContext, C> ctrl = entry.gui().get();
        ScreenProviderRegistry.INSTANCE.registerFactory(id,
            (syncId, unused, player, buf) ->
                screen.apply(ctrl.apply(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
    }

    public static void init() {
        registerGui(MachineRegistry.aegisaltInfuser(), InfuserScreen::new);
    }
}
