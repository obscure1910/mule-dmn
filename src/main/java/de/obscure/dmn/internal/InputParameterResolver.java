package de.obscure.dmn.internal;

import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.feel.lang.SimpleType;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.InputTypeResolver;

import java.util.Optional;
import java.util.Set;

public class InputParameterResolver implements InputTypeResolver<String> {

    @Override
    public MetadataType getInputMetadata(MetadataContext metadataContext, String key)  {
        Optional<DmnConfiguration> optionalDmnConfiguration = metadataContext.getConfig();
        BaseTypeBuilder baseTypeBuilder = metadataContext.getTypeBuilder();
        ObjectTypeBuilder objectTypeBuilder = baseTypeBuilder.objectType();

        if(optionalDmnConfiguration.isPresent()) {
            DmnConfiguration dmnConfiguration = optionalDmnConfiguration.get();
            DMNModel dmnModel = dmnConfiguration.getDmnModel();
            Set<InputDataNode> inputs = dmnModel.getInputs();

            inputs.forEach(input -> {
                //correspondent to ItemDefinition in the DMNModel table 49 in FEEL Spec
//                DMNType dmnType = input.getType();

                handleSimpleType(input, objectTypeBuilder, baseTypeBuilder);
                //ToDo handle enums and complex types
//                if(dmnType.isComposite()) {
//                    //is complex type
//                    throw new RuntimeException("Complex: " + dmnType.getName() + " " + input.getName());
//                } else if (dmnType.isCollection()) {
//                    //is collection
//                    throw new RuntimeException("Collection: " + dmnType.getName() + " " + input.getName());
//                } else if (!dmnType.getAllowedValues().isEmpty()) {
//                    handleEnumType(input, objectTypeBuilder, baseTypeBuilder);
//                } else {
//                    handleSimpleType(input, objectTypeBuilder, baseTypeBuilder);
//                }
            });

        }

        return objectTypeBuilder.build();
    }

    @Override
    public String getCategoryName() {
        return "Input";
    }

    void handleSimpleType(InputDataNode input, ObjectTypeBuilder objectTypeBuilder, BaseTypeBuilder baseTypeBuilder) {
        DMNType dmnType = input.getType();
        ObjectFieldTypeBuilder objectFieldTypeBuilder = objectTypeBuilder.addField().key(input.getName());

        switch (dmnType.getName()) {
            case SimpleType.NUMBER:        objectFieldTypeBuilder.value(baseTypeBuilder.numberType()); break;
            case SimpleType.STRING:        objectFieldTypeBuilder.value(baseTypeBuilder.stringType()); break;
            case SimpleType.DATE:          objectFieldTypeBuilder.value(baseTypeBuilder.dateType()); break;
            case SimpleType.DATE_AND_TIME: objectFieldTypeBuilder.value(baseTypeBuilder.dateTimeType()); break;
            case SimpleType.TIME:          objectFieldTypeBuilder.value(baseTypeBuilder.timeType()); break;
            default:                       objectFieldTypeBuilder.value(baseTypeBuilder.anyType());
        }
    }

    void handleEnumType(InputDataNode input, ObjectTypeBuilder objectTypeBuilder, BaseTypeBuilder baseTypeBuilder) {
        DMNType dmnType = input.getType();
        ObjectFieldTypeBuilder objectFieldTypeBuilder = objectTypeBuilder.addField().key(input.getName());
        //ToDo check reference type number, string, ...
        String[] unaryTestList = dmnType.getAllowedValues().stream().map(Object::toString).toArray(String[]::new);

        objectFieldTypeBuilder.value(baseTypeBuilder.stringType().defaultValue(unaryTestList[0]).enumOf(unaryTestList).enumLabelsOf(unaryTestList));
    }



}
