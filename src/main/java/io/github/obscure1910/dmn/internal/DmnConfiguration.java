package io.github.obscure1910.dmn.internal;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;
import org.mule.runtime.api.lifecycle.Disposable;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.io.InputStream;
import java.util.UUID;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(DmnOperations.class)
public class DmnConfiguration implements Initialisable, Disposable {

    private KieContainer kieContainer;
    private StatelessKieSession kSession;
    private DMNRuntime dmnRuntime;
    private DMNModel dmnModel;

    @Parameter
    private String namespace;

    @Parameter
    private String modelName;

    public DMNModel getDmnModel() {
        return dmnModel;
    }

    public DMNRuntime getDmnRuntime() {
        return dmnRuntime;
    }

    @Override
    public void initialise() {
        KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId = ks.newReleaseId("de.obscure.dmn.internal", "DmnResourceLoader", UUID.randomUUID().toString());
        KieFileSystem kfs = ks.newKieFileSystem();

        String dmnName = modelName + ".dmn";
        InputStream inputsream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dmnName);
        Resource resource = ks.getResources().newInputStreamResource(inputsream)
                .setSourcePath(dmnName)
                .setResourceType(ResourceType.DMN);

        kfs.write(resource);
        kfs.generateAndWritePomXML(releaseId);

        ks.newKieBuilder(kfs).buildAll();

        this.kieContainer = ks.newKieContainer(releaseId);
        this.kSession = kieContainer.newStatelessKieSession();
        this.dmnRuntime = KieRuntimeFactory.of(kSession.getKieBase()).get(DMNRuntime.class);
        this.dmnModel = dmnRuntime.getModel(namespace, modelName);
    }

    @Override
    public void dispose() {
        this.kieContainer.dispose();
    }
}
