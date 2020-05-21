package robosky.uplands.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.EntityType;
import robosky.uplands.entity.EntityRegistry;

@Environment(EnvType.CLIENT)
public class EntityRenderingRegistry {
    public static void init() {
        register(EntityRegistry.HEXAHAEN, HexahaenRender.class);
    }

    private static void register(EntityType<?> entity, Class<? extends MobEntityRenderer<?, ?>> renderer) {
        EntityRendererRegistry.INSTANCE.register(entity, (entityRenderDispatcher, context) -> {
            MobEntityRenderer<?, ?> render = null;
            try {
                render = renderer.getConstructor(entityRenderDispatcher.getClass()).newInstance(entityRenderDispatcher);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            return render;
        });
    }
}
