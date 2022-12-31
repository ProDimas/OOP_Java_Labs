package org.fpm.di;

import java.util.Collections;
import java.util.Set;

final class InstanceComponentNode<T> extends ComponentNode<T> {
    private final T instance;

    InstanceComponentNode(Class<T> clazz, T instance) {
        super(clazz);
        this.instance = instance;
    }

    <R> boolean isDependentOn(Class<R> clazz) {
        return false;
    }

    <R> void satisfyDependency(ComponentNode<R> newNode) {
        throw new UnsupportedOperationException("satisfyDependency of InstanceComponentNode for "
                .concat(this.getType().getName()).concat(" is not supported for always explicit newNode"));
    }

    Set<Class<?>> getUnsatisfiedDependencies() {
        return Collections.emptySet();
    }

    Set<ComponentNode<?>> getSatisfiedDependencies() {
        return Collections.emptySet();
    }

    T getClassInstance() {
        return this.instance;
    }
}
