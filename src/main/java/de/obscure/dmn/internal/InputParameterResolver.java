package de.obscure.dmn.internal;

import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;

import java.util.Optional;
import java.util.Set;

public class InputParameterResolver implements InputTypeResolver<String> {

    @Override
    public MetadataType getInputMetadata(MetadataContext metadataContext, String key) {
        Optional<DmnConfiguration> optionalDmnConfiguration = metadataContext.getConfig();
        BaseTypeBuilder baseTypeBuilder = metadataContext.getTypeBuilder();
        ObjectTypeBuilder objectTypeBuilder = baseTypeBuilder.objectType();

        if (optionalDmnConfiguration.isPresent()) {
            DmnConfiguration dmnConfiguration = optionalDmnConfiguration.get();
            DMNModel dmnModel = dmnConfiguration.getDmnModel();
            Set<InputDataNode> inputs = dmnModel.getInputs();

            inputs.forEach(input -> {
                //correspondent to ItemDefinition in the DMNModel table 49 in FEEL Spec
                DmnTypeMapper.handleSimpleType(input, objectTypeBuilder, baseTypeBuilder);
            });
        }

        return objectTypeBuilder.build();
    }

    @Override
    public String getCategoryName() {
        return "Input";
    }

}
