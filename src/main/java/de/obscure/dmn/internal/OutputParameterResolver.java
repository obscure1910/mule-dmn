package de.obscure.dmn.internal;

import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.feel.lang.SimpleType;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

import java.util.Optional;

//Todo
public class OutputParameterResolver implements OutputTypeResolver<String> {
    @Override
    public MetadataType getOutputType(MetadataContext metadataContext, String key) {
        /*
        Optional<DmnConfiguration> optionalDmnConfiguration = metadataContext.getConfig();
        BaseTypeBuilder baseTypeBuilder = metadataContext.getTypeBuilder();
        ObjectTypeBuilder objectTypeBuilder = baseTypeBuilder.objectType();

        if(optionalDmnConfiguration.isPresent()) {
            DmnConfiguration dmnConfiguration = optionalDmnConfiguration.get();
            DMNModel dmnModel = dmnConfiguration.getDmnModel();
            DecisionNode outputDecisionNode = dmnModel.getDecisionByName(dmnConfiguration.getOutputDecisionName());
            //correspondent to ItemDefinition in the DMNModel table 49 in FEEL Spec
            DMNType dmnType = outputDecisionNode.getResultType();
            if(dmnType.isComposite()) {
                //is complex type
                throw new RuntimeException("Complex: " + dmnType.getName() + " " + outputDecisionNode.getName());
            } else if (dmnType.isCollection()) {
                //is collection
                throw new RuntimeException("Collection: " + dmnType.getName() + " " + outputDecisionNode.getName());
            } else if (!dmnType.getAllowedValues().isEmpty()) {
                //is enum
                throw new RuntimeException("Enum: " + dmnType.getName() + " " + outputDecisionNode.getName());
            } else {
                handleSimpleType(outputDecisionNode, objectTypeBuilder, baseTypeBuilder);
            }
        }
        */
        return null;
    }

    @Override
    public String getCategoryName() {
        return "Output";
    }


    void handleSimpleType(DecisionNode outputDecisionNode, ObjectTypeBuilder objectTypeBuilder, BaseTypeBuilder baseTypeBuilder) {
        DMNType dmnType = outputDecisionNode.getResultType();
        ObjectFieldTypeBuilder objectFieldTypeBuilder = objectTypeBuilder.addField().key(outputDecisionNode.getName());

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
