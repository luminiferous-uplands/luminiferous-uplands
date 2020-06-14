package robosky.uplands.client.render.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import robosky.uplands.entity.HexahaenEntity;

import java.util.Collections;

public class HexahaenModel extends CompositeEntityModel<HexahaenEntity> {
    private final ModelPart model;

    public HexahaenModel() {
        // width and height must be set before creating cuboids
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.model = new ModelPart(this, 0, 0);
        model.addCuboid(-10, 4, -10, 20, 20, 20);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return Collections.singletonList(model);
    }

    @Override
    public void setAngles(HexahaenEntity entity, float f1, float f2, float f3, float f4, float f5) {
    }
}
