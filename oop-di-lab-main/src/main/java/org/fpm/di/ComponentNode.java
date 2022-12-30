package org.fpm.di;

import java.util.Set;

abstract class ComponentNode<T> {
    private final Class<T> nodeClass;

    ComponentNode(Class<T> clazz) {
        this.nodeClass = clazz;
    }

    Class<T> getType() {
        return this.nodeClass;
    }

    abstract <R> boolean isDependentOn(Class<R> clazz);

    abstract <R> void satisfyDependency(ComponentNode<R> newNode);

    abstract Set<Class<?>> getUnsatisfiedDependencies();

    abstract Set<ComponentNode<?>> getSatisfiedDependencies();

    abstract T getClassInstance();
}