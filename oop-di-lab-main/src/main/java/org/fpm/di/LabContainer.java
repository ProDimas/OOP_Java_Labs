package org.fpm.di;

public class LabContainer implements Container {
    private final LabDependencyGraph graph;

    LabContainer(LabDependencyGraph graph) {
        this.graph = graph;
    }

    public <T> T getComponent(Class<T> clazz) {
        return graph.searchForComponent(clazz).getClassInstance();
    }
}
