package io.github.obscure1910.dmn.internal;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.kie.dmn.api.core.*;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.Map;

import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_XML;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class DmnOperations {

    @MediaType(value = APPLICATION_XML, strict = false)
    public String executeDMN(@Config DmnConfiguration configuration,
                             @TypeResolver(InputParameterResolver.class) Map<String, Object> input) throws  XMLStreamException {
        DMNRuntime dmnRuntime = configuration.getDmnRuntime();
        DMNContext ctx = dmnRuntime.newContext();
        input.forEach(ctx::set);

        DMNModel dmnModel = configuration.getDmnModel();

        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, ctx);

        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        StringWriter out = new StringWriter();
        XMLStreamWriter sw = xmlOutputFactory.createXMLStreamWriter(out);

        sw.writeStartDocument();
        sw.writeStartElement("results");

        XmlMapper mapper = new XmlMapper(xmlInputFactory);

        dmnResult.getDecisionResults().forEach(result -> {
            try {
                sw.writeStartElement(XmlHelper.toValidXmlTag(result.getDecisionName()));
                mapper.writeValue(sw, result.getResult());
                sw.writeEndElement();
            } catch (XMLStreamException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        sw.writeEndDocument();

        return out.toString();
    }


}
