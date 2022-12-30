package org.fpm.di;

import java.lang.reflect.Modifier;
import java.util.*;

public class LabBinder implements Binder {
    private final LabDependencyGraph binderGraph;

    LabBinder() {
        binderGraph = new LabDependencyGraph();
    }

    @Override
    public <T> void bind(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz must be non null");
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            binderGraph.registerUnsatisfiedDependency(clazz);
            return;
        }
        Optional<ComponentNode<T>> optFoundNode = binderGraph.getNodeOf(clazz);
        if (optFoundNode.isPresent()) {
            throw new BindingException(clazz.getName().concat(": explicit node already registered"));
        }

        ComponentNode<T> newNode;
        try {
            newNode = new ConstructorComponentNode<>(clazz);
            if (newNode.isDependentOn(clazz)) {
                throw new CyclicDependencyException(clazz.getName().concat(": depends on itself"));
            }
            binderGraph.placeNode(newNode);
            binderGraph.setAvailableDependencies(newNode);
            binderGraph.satisfyDependentOn(newNode);
            newNode.getUnsatisfiedDependencies().forEach(binderGraph::registerUnsatisfiedDependency);
            this.binderGraph.getUnsatisfied().remove(clazz);
        } catch (ConstructorNotFoundException e) {
            binderGraph.registerUnsatisfiedDependency(clazz);
        }
    }

    @Override
    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        Objects.requireNonNull(clazz, "clazz must be non null");
        Objects.requireNonNull(implementation, "implementation must be non null");
        Optional<ComponentNode<T>> optClazzNode = binderGraph.getNodeOf(clazz);
        if (optClazzNode.isPresent()) {
            throw new BindingException(clazz.getName().concat(": explicit node already registered"));
        }

        ComponentNode<T> newClazzNode = new SuperComponentNode<>(clazz, implementation);
        if (newClazzNode.isDependentOn(clazz)) {
            throw new CyclicDependencyException(clazz.getName().concat(": depends on itself"));
        }
        binderGraph.placeNode(newClazzNode);
        binderGraph.setAvailableDependencies(newClazzNode);
        binderGraph.satisfyDependentOn(newClazzNode);
        newClazzNode.getUnsatisfiedDependencies().forEach(binderGraph::registerUnsatisfiedDependency);
        this.binderGraph.getUnsatisfied().remove(clazz);
    }

    @Override
    public <T> void bind(Class<T> clazz, T instance) {
        Objects.requireNonNull(clazz, "clazz must be non null");
        Optional<ComponentNode<T>> optFoundNode = binderGraph.getNodeOf(clazz);
        if (optFoundNode.isPresent()) {
            throw new BindingException(clazz.getName().concat(": explicit node already registered"));
        }

        ComponentNode<T> newNode = new InstanceComponentNode<>(clazz, instance);
        binderGraph.placeNode(newNode);
        binderGraph.satisfyDependentOn(newNode);
        this.binderGraph.getUnsatisfied().remove(clazz);
    }

    void assemble() {
        Set<Class<?>> unsatisfied = this.binderGraph.getUnsatisfied();
        do {
            unsatisfied = this.bindUnsatisfiedDependencies(unsatisfied);
        } while (!unsatisfied.isEmpty());
        this.binderGraph.checkOnCyclicDependencies();
    }

    private Set<Class<?>> bindUnsatisfiedDependencies(Set<Class<?>> dependencies) {
        Set<Class<?>> newUnsatisfiedDependencies = new HashSet<>();
        for (Class<?> clazz: dependencies) {
            try {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    binderGraph.registerUnsatisfiedDependency(clazz);
                    throw new ConstructorNotFoundException(clazz.getName().concat(": is abstract or interface"));
                }
                newUnsatisfiedDependencies.addAll(bindUnsatisfiedClassWithConstructor(clazz));
            } catch (ConstructorNotFoundException ex) {
                bindUnsatisfiedClassAsSuper(clazz);
            }
        }
        return newUnsatisfiedDependencies;
    }

    private <T> Set<Class<?>> bindUnsatisfiedClassWithConstructor(Class<T> clazz)
            throws ConstructorNotFoundException {
        ComponentNode<T> newNode = new ConstructorComponentNode<>(clazz);
        if (newNode.isDependentOn(clazz)) {
            throw new CyclicDependencyException(clazz.getName().concat(": depends on itself"));
        }
        this.binderGraph.placeNode(newNode);
        this.binderGraph.setAvailableDependencies(newNode);
        binderGraph.satisfyDependentOn(newNode);
        return newNode.getUnsatisfiedDependencies();
    }

    private <T> void bindUnsatisfiedClassAsSuper(Class<T> clazz) {
        ComponentNode<T> newNode;
        List<ComponentNode<? extends T>> nodesOfExtendingClasses = binderGraph
                .getNodesOfExtendingClasses(clazz);
        Class<? extends T> implementation;
        if (nodesOfExtendingClasses.size() > 1) {
            throw new BindingException(clazz.getName()
                    .concat(": component dependency cannot be resolved implicitly. ")
                    .concat("More than one registered inherited class that can be ")
                    .concat("used to get component of this class found"));
        } else if (nodesOfExtendingClasses.size() == 0) {
            List<Class<?>> unresolvedExtendingClasses = binderGraph.getUnsatisfied().stream()
                    .filter((cls) -> clazz.isAssignableFrom(cls) && !clazz.equals(cls))
                    .toList();
            if (unresolvedExtendingClasses.size() != 1) {
                throw new BindingException(clazz.getName()
                        .concat(": component dependency cannot be resolved implicitly. ")
                        .concat("More than one inherited class that can be ")
                        .concat("used to get component of this class found ")
                        .concat("(currently unresolved but still can be used after it will be satisfied)"));
            } else {
                implementation = (Class<? extends T>) unresolvedExtendingClasses.get(0);
            }
        } else {
            implementation = nodesOfExtendingClasses.get(0).getType();
        }
        newNode = new SuperComponentNode<>(clazz, implementation);
        binderGraph.placeNode(newNode);
        binderGraph.setAvailableDependencies(newNode);
        binderGraph.satisfyDependentOn(newNode);
    }

    LabDependencyGraph getGraph() {
        return this.binderGraph;
    }
}
