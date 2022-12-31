package org.fpm.di;

public class LabEnvironment implements Environment {
    public Container configure(Configuration configuration) {
        LabBinder bindingPerformer = new LabBinder();
        configuration.configure(bindingPerformer);
        bindingPerformer.assemble();
        return new LabContainer(bindingPerformer.getGraph());
    }
}
