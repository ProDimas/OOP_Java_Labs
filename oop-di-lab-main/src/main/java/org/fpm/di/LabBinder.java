package org.fpm.di;

import java.lang.reflect.Modifier;
import java.util.*;

public class LabBinder implements Binder {
    private final LabDependencyGraph binderGraph;

    LabBinder() {
        this.binderGraph = new LabDependencyGraph();
    }

    public <T> void bind(Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz must be non null");

        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            this.binderGraph.registerUnsatisfiedDependency(clazz);
            return;
        }

        Optional<ComponentNode<T>> optFoundNode = this.binderGraph.getNodeOf(clazz);
        if (optFoundNode.isPresent()) {
            throw new BindingException(clazz.getName().concat(": explicit node already registered"));
        }
        try {
            ComponentNode<T> newNode = new ConstructorComponentNode<>(clazz);
            if (newNode.isDependentOn(clazz)) {
                throw new CyclicDependencyException(clazz.getName().concat(": depends on itself"));
            }

            this.binderGraph.placeNode(newNode);
            this.binderGraph.setAvailableDependencies(newNode);
            this.binderGraph.satisfyDependentOn(newNode);
            newNode.getUnsatisfiedDependencies().forEach(this.binderGraph::registerUnsatisfiedDependency);
            this.binderGraph.getUnsatisfied().remove(clazz);
        } catch (ConstructorNotFoundException e) {
            this.binderGraph.registerUnsatisfiedDependency(clazz);
        }
    }

    public <T> void bind(Class<T> clazz, Class<? extends T> implementation) {
        Objects.requireNonNull(clazz, "clazz must be non null");
        Objects.requireNonNull(implementation, "implementation must be non null");
        Optional<ComponentNode<T>> optClazzNode = this.binderGraph.getNodeOf(clazz);
        if (optClazzNode.isPresent()) {
            throw new BindingException(clazz.getName().concat(": explicit node already registered"));
        }

        ComponentNode<T> newClazzNode = new SuperComponentNode<>(clazz, implementation);
        if (newClazzNode.isDependentOn(clazz)) {
            throw new CyclicDependencyException(clazz.getName().concat(": depends on itself"));
        }

        this.binderGraph.placeNode(newClazzNode);
        this.binderGraph.setAvailableDependencies(newClazzNode);
        this.binderGraph.satisfyDependentOn(newClazzNode);
        newClazzNode.getUnsatisfiedDependencies().forEach(binderGraph::registerUnsatisfiedDependency);
        this.binderGraph.getUnsatisfied().remove(clazz);
    }

    public <T> void bind(Class<T> clazz, T instance) {
        Objects.requireNonNull(clazz, "clazz must be non null");
        Optional<ComponentNode<T>> optFoundNode = this.binderGraph.getNodeOf(clazz);
        if (optFoundNode.isPresent()) {
            throw new BindingException(clazz.getName().concat(": explicit node already registered"));
        }

        ComponentNode<T> newNode = new InstanceComponentNode<>(clazz, instance);
        this.binderGraph.placeNode(newNode);
        this.binderGraph.satisfyDependentOn(newNode);
        this.binderGraph.getUnsatisfied().remove(clazz);
    }

    void assemble() {
        Set<Class<?>> unsatisfied = this.binderGraph.getUnsatisfied();

        do {
            unsatisfied = this.bindUnsatisfiedDependencies(unsatisfied);
        } while(!unsatisfied.isEmpty());

        this.binderGraph.checkOnCyclicDependencies();
    }

    private Set<Class<?>> bindUnsatisfiedDependencies(Set<Class<?>> dependencies) {
        Set<Class<?>> newUnsatisfiedDependencies = new HashSet<>();

        for (Class<?> dependency : dependencies) {
            if (dependency.isInterface() || Modifier.isAbstract(dependency.getModifiers())) {
                this.bindUnsatisfiedClassAsSuper(dependency);
            } else {
                try {
                    newUnsatisfiedDependencies.addAll(this.bindUnsatisfiedClassWithConstructor(dependency));
                } catch (ConstructorNotFoundException e) {
                    this.bindUnsatisfiedClassAsSuper(dependency);
                }
            }
        }

        return newUnsatisfiedDependencies;
    }

    private <T> Set<Class<?>> bindUnsatisfiedClassWithConstructor(Class<T> clazz) throws ConstructorNotFoundException {
        ComponentNode<T> newNode = new ConstructorComponentNode<>(clazz);
        if (newNode.isDependentOn(clazz)) {
            throw new CyclicDependencyException(clazz.getName().concat(": depends on itself"));
        } else {
            this.binderGraph.placeNode(newNode);
            this.binderGraph.setAvailableDependencies(newNode);
            this.binderGraph.satisfyDependentOn(newNode);
            return newNode.getUnsatisfiedDependencies();
        }
    }

    private <T> void bindUnsatisfiedClassAsSuper(Class<T> clazz) {
        List<ComponentNode<? extends T>> nodesOfExtendingClasses = this.binderGraph.getNodesOfExtendingClasses(clazz);
        if (nodesOfExtendingClasses.size() > 1) {
            throw new BindingException(clazz.getName()
                    .concat(": component dependency cannot be resolved implicitly. ")
                    .concat("More than one registered inherited class that can be ")
                    .concat("used to get component of this class found"));
        }

        Class<? extends T> implementation;
        if (nodesOfExtendingClasses.size() == 0) {
            List<Class<?>> notYetSatisfiedExtendingClasses = this.binderGraph.getUnsatisfied().stream()
                    .filter((cls) -> clazz.isAssignableFrom(cls) && !clazz.equals(cls))
                    .toList();
            if (notYetSatisfiedExtendingClasses.size() != 1) {
                throw new BindingException(clazz.getName()
                        .concat(": component dependency cannot be resolved implicitly. ")
                        .concat("More than one inherited class that can be ")
                        .concat("used to get component of this class found ")
                        .concat("(currently unresolved but still can be used after it will be satisfied)"));
            }

            implementation = (Class<? extends T>) notYetSatisfiedExtendingClasses.get(0);
        } else {
            implementation = nodesOfExtendingClasses.get(0).getType();
        }

        ComponentNode<T> newNode = new SuperComponentNode<>(clazz, implementation);
        this.binderGraph.placeNode(newNode);
        this.binderGraph.setAvailableDependencies(newNode);
        this.binderGraph.satisfyDependentOn(newNode);
    }

    LabDependencyGraph getGraph() {
        return this.binderGraph;
    }
}
