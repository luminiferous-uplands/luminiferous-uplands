package robosky.uplands;

import robosky.uplands.block.BlockRenderingRegistry;
import robosky.uplands.client.gui.GuiRegistry;
import robosky.uplands.client.render.blockentity.BlockEntityRenderingRegistry;
import robosky.uplands.client.render.entity.EntityRenderingRegistry;

import net.fabricmc.api.ClientModInitializer;

public final class UplandsModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderingRegistry.init();
        GuiRegistry.init();
        BlockEntityRenderingRegistry.init();
        EntityRenderingRegistry.init();
    }
}
