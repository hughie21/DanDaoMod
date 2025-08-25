package com.hughie.dandao.client.model.bedrock;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.AbstractBedrockEntityModel;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockModelPOJO;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockVersion;
import net.minecraft.world.entity.Entity;

import java.io.InputStream;

public class SimpleBedrockModel<T extends Entity> extends AbstractBedrockEntityModel<T> {
    public static final BedrockPart EMPTY = new BedrockPart();

    public SimpleBedrockModel(InputStream stream) {
        super(stream);
    }

    public SimpleBedrockModel(BedrockModelPOJO pojo, BedrockVersion version) {
        super(pojo, version);
    }

    public BedrockPart getPart(String partName) {
        return this.modelMap.getOrDefault(partName, EMPTY);
    }

    @Override
    public void setupAnim(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {
    }
}
