package org.fpm.di;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Set;

class SuperComponentNode<T> extends ComponentNode<T> {
    private ComponentNode<? extends T> inherited;

    private final Class<? extends T> inheritedClass;

    private final boolean isSingleton;

    private T firstInstance = null;

    SuperComponentNode(Class<T> clazz, Class<? extends T> inheritedClass) {
        super(clazz);
        this.inheritedClass = inheritedClass;
        this.isSingleton = clazz.isAnnotationPresent(Singleton.class);
    }

    @Override
    <R> boolean isDependentOn(Class<R> clazz) {
        return this.inheritedClass.equals(clazz);
    }

    @Override
    <R> void satisfyDependency(ComponentNode<R> newNode) {
        if (inheritedClass.equals(newNode.getType())) {
            inherited = (ComponentNode<? extends T>) newNode;
        }
    }

    @Override
    Set<Class<?>> getUnsatisfiedDependencies() {
        if (inherited == null) {
            return Collections.singleton(inheritedClass);
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    Set<ComponentNode<?>> getSatisfiedDependencies() {
        if (inherited == null) {
            return Collections.emptySet();
        } else {
            return Collections.singleton(inherited);
        }
    }

    @Override
    T getClassInstance() {
        if (this.isSingleton) {
            if (this.firstInstance == null) {
                firstInstance = inherited.getClassInstance();
            }
            return firstInstance;
        }
        return inherited.getClassInstance();
    }
}
