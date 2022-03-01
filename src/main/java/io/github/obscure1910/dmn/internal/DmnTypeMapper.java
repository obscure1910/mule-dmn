package io.github.obscure1910.dmn.internal;

import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.DMNUnaryTest;
import org.kie.dmn.feel.lang.SimpleType;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class DmnTypeMapper {

    private DmnTypeMapper() {
        //just static methods
    }

    public static void handleEnumType(DMNType dmnType, List<DMNUnaryTest> allowedValues, String inputName, ObjectTypeBuilder objectTypeBuilder, BaseTypeBuilder baseTypeBuilder) {
        ObjectFieldTypeBuilder objectFieldTypeBuilder = objectTypeBuilder.addField().key(inputName);

        switch (dmnType.getName()) {
            case SimpleType.NUMBER:
                Number[] numbers = allowedValues.stream().map(n -> new BigDecimal(n.toString())).toArray(Number[]::new);
                objectFieldTypeBuilder.value(baseTypeBuilder.numberType().enumOf(numbers));
                break;
            case SimpleType.STRING:
                String[] strings = allowedValues.stream().map(Object::toString).toArray(String[]::new);
                objectFieldTypeBuilder.value(baseTypeBuilder.stringType().enumOf(strings));
                break;
            default:
                objectFieldTypeBuilder.value(baseTypeBuilder.anyType());
        }
    }

    //correspondent to ItemDefinition in the DMNModel table 49 in FEEL Spec
    public static void handleSimpleType(DMNType dmnType, String inputName, ObjectTypeBuilder objectTypeBuilder, BaseTypeBuilder baseTypeBuilder) {
        ObjectFieldTypeBuilder objectFieldTypeBuilder = objectTypeBuilder.addField().key(inputName);
        switch (dmnType.getName()) {
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
