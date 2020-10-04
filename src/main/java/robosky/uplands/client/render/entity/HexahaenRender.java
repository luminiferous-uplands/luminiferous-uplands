package robosky.uplands.client.render.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import robosky.uplands.UplandsMod;
import robosky.uplands.client.render.entity.model.HexahaenModel;
import robosky.uplands.entity.HexahaenEntity;

public class HexahaenRender extends MobEntityRenderer<HexahaenEntity, HexahaenModel> {

    public HexahaenRender(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new HexahaenModel(), 1.0f);
    }

    private static final Identifier[] TEXTURES = {
        UplandsMod.id("textures/entity/hexahaen/level_1.png"),
        UplandsMod.id("textures/entity/hexahaen/level_2.png"),
        UplandsMod.id("textures/entity/hexahaen/level_3.png"),
        UplandsMod.id("textures/entity/hexahaen/level_4.png"),
        UplandsMod.id("textures/entity/hexahaen/level_5.png"),
    };

    @Override
    public void render(HexahaenEntity entity, float f1, float f2, MatrixStack matrix, VertexConsumerProvider provider, int var1) {
        // set shadow size to 87.5% of normal
        this.shadowRadius = 0.875f;
        super.render(entity, f1, f2, matrix, provider, var1);
    }

    // TODO: reflecting causes weird lighting artifacts
    @Override
    protected void scale(HexahaenEntity entity, MatrixStack matrix, float f1) {
        if (entity.isLeftHanded())
            matrix.scale(-1.0f, 1.0f, 1.0f);

        super.scale(entity, matrix, f1);
    }

    @Override
    public Identifier getTexture(HexahaenEntity entity) {
        return TEXTURES[entity.getDataTracker().get(HexahaenEntity.STRENGTH) - 1];
    }
}
