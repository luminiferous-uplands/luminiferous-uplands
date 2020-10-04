package robosky.uplands.client.gui;

import java.util.function.BiFunction;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import robosky.uplands.block.machine.MachineRegistry;
import robosky.uplands.block.machine.infuser.InfuserContainer;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerContext;
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
        Identifier id = Registry.BLOCK.getId(entry.block);
        MachineRegistry.MachineScreenFactory<C> ctrl = entry.gui;
        assert ctrl != null : "null machine screen factory";
        ScreenProviderRegistry.INSTANCE.registerFactory(id,
            (syncId, unused, player, buf) ->
                screen.apply(ctrl.create(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos())), player));
    }

    public static void init() {
        registerGui(MachineRegistry.AEGISALT_INFUSER, InfuserScreen::new);
    }
}
