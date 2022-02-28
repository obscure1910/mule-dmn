package io.github.obscure1910.dmn.internal;

import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.feel.lang.SimpleType;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;

public class DmnTypeMapper {

    private DmnTypeMapper() {
        //just static methods
    }

    public static void handleSimpleType(InputDataNode input, ObjectTypeBuilder objectTypeBuilder, BaseTypeBuilder baseTypeBuilder) {
        DMNType dmnType = input.getType();
        ObjectFieldTypeBuilder objectFieldTypeBuilder = objectTypeBuilder.addField().key(input.getName());

        addType(dmnType.getName(), objectFieldTypeBuilder, baseTypeBuilder);
    }

    public static void addType(String typeName, ObjectFieldTypeBuilder objectFieldTypeBuilder, BaseTypeBuilder baseTypeBuilder) {
        switch (typeName) {
            case SimpleType.NUMBER:
                objectFieldTypeBuilder.value(baseTypeBuilder.numberType());
                break;
            case SimpleType.STRING:
                objectFieldTypeBuilder.value(baseTypeBuilder.stringType());
                break;
            case SimpleType.DATE:
                objectFieldTypeBuilder.value(baseTypeBuilder.dateType());
                break;
            case SimpleType.DATE_AND_TIME:
                objectFieldTypeBuilder.value(baseTypeBuilder.dateTimeType());
                break;
            case SimpleType.TIME:
                objectFieldTypeBuilder.value(baseTypeBuilder.timeType());
                break;
            default:
                objectFieldTypeBuilder.value(baseTypeBuilder.anyType());
        }
    }

}
