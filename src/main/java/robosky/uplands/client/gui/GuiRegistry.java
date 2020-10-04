package robosky.uplands.client.gui;

import java.util.function.BiFunction;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import robosky.uplands.block.machine.MachineRegistry;
import robosky.uplands.block.machine.infuser.InfuserContainer;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;

// delete this class?
class InfuserScreen extends CottonInventoryScreen<InfuserContainer> {

    public InfuserScreen(InfuserContainer container, PlayerEntity player) {
        super(container, player);
    }
}

public final class GuiRegistry {

    private GuiRegistry() {
    }

    private static <C extends SyncedGuiDescription> void registerGui(
        MachineRegistry.MachineEntry<? extends Block, ?, C> entry,
        BiFunction<C, PlayerEntity, CottonInventoryScreen<C>> screen) {
        ScreenRegistry.<C, CottonInventoryScreen<C>>register(entry.gui, (handler, inventory, title) -> screen.apply(handler, inventory.player));
    }

    public static void init() {
        registerGui(MachineRegistry.AEGISALT_INFUSER, InfuserScreen::new);
    }
}
