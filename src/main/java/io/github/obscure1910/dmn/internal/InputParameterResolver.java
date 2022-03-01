package io.github.obscure1910.dmn.internal;

import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.DMNUnaryTest;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;

import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

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
                DMNType dmnType = input.getType();
                String inputName = input.getName();

                if(isEnumType(dmnType)) {
                    DmnTypeMapper.handleEnumType(dmnType.getBaseType(), dmnType.getAllowedValues(), inputName, objectTypeBuilder, baseTypeBuilder);
                } else {
                    DmnTypeMapper.handleSimpleType(dmnType, inputName, objectTypeBuilder, baseTypeBuilder);
                }

            });
        }

        return objectTypeBuilder.build();
    }

    @Override
    public String getCategoryName() {
        return "Input";
    }

    private boolean isEnumType(DMNType dmnType) {
        return !dmnType.getAllowedValues().isEmpty();
    }

}
