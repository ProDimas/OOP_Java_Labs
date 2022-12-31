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

    <R> boolean isDependentOn(Class<R> clazz) {
        return this.inheritedClass.equals(clazz);
    }

    <R> void satisfyDependency(ComponentNode<R> newNode) {
        if (this.inheritedClass.equals(newNode.getType())) {
            this.inherited = (ComponentNode<? extends T>) newNode;
        }

    }

    Set<Class<?>> getUnsatisfiedDependencies() {
        return this.inherited == null ? Collections.singleton(this.inheritedClass) : Collections.emptySet();
    }

    Set<ComponentNode<?>> getSatisfiedDependencies() {
        return this.inherited == null ? Collections.emptySet() : Collections.singleton(this.inherited);
    }

    T getClassInstance() {
        if (this.isSingleton) {
            if (this.firstInstance == null) {
                this.firstInstance = this.inherited.getClassInstance();
            }

            return this.firstInstance;
        } else {
            return this.inherited.getClassInstance();
        }
    }
}
