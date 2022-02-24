package de.obscure.dmn.internal;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.kie.dmn.api.core.*;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;

import java.io.*;
import java.util.Map;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class DmnOperations {

  @MediaType(value = "application/xml", strict = false)
  public byte[] executeDMN(@Config DmnConfiguration configuration,
                                @TypeResolver(InputParameterResolver.class) Map<String, Object> input) throws IOException {

    DMNRuntime dmnRuntime = configuration.getDmnRuntime();
    DMNContext ctx = dmnRuntime.newContext();
    input.forEach(ctx::set);

    DMNModel dmnModel = configuration.getDmnModel();

    DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, ctx);
    Object[] results = dmnResult.getDecisionResults().stream().map(DMNDecisionResult::getResult).toArray();
    try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
      XmlMapper mapper = new XmlMapper();
      mapper.writeValue(baos, results);
      return baos.toByteArray();
    }
  }

}
