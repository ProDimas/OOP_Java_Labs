package org.fpm.di;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ConstructorComponentNode<T> extends ComponentNode<T> {
    private final Constructor<T> cons;
    private final Parameter[] parameters;
    private final ComponentNode<?>[] dependencies;
    private final boolean isSingleton;
    private T firstInstance = null;

    ConstructorComponentNode(Class<T> clazz) throws ConstructorNotFoundException {
        super(clazz);
        String message = clazz.getName().concat(": has no injectable constructors");
        Constructor<?>[] consPresent = clazz.getConstructors();
        List<Constructor<?>> consAnnotated = Arrays.stream(consPresent)
                .filter((cons) -> cons.isAnnotationPresent(Inject.class)).toList();
        Constructor<T> toInject;
        if (consAnnotated.size() == 1) {
            toInject = (Constructor<T>) consAnnotated.get(0);
        } else {
            if (consPresent.length != 1) {
                throw new ConstructorNotFoundException(message);
            }

            if (consPresent[0].getParameterCount() != 0) {
                throw new ConstructorNotFoundException(message);
            }

            toInject = (Constructor<T>) consPresent[0];
        }

        this.cons = toInject;
        this.parameters = toInject.getParameters();
        this.dependencies = new ComponentNode[this.parameters.length];
        this.isSingleton = clazz.isAnnotationPresent(Singleton.class);
    }

    <R> boolean isDependentOn(Class<R> clazz) {
        for (Parameter parameter : this.parameters) {
            if (parameter.getType().equals(clazz)) {
                return true;
            }
        }

        return false;
    }

    <R> void satisfyDependency(ComponentNode<R> newNode) {
        for(int i = 0; i < this.parameters.length; ++i) {
            if (this.parameters[i].getType().equals(newNode.getType())) {
                this.dependencies[i] = newNode;
            }
        }
    }

    Set<Class<?>> getUnsatisfiedDependencies() {
        Set<Class<?>> toReturn = new HashSet<>();

        for(int i = 0; i < this.parameters.length; i++) {
            if (this.dependencies[i] == null) {
                toReturn.add(this.parameters[i].getType());
            }
        }

        return toReturn;
    }

    Set<ComponentNode<?>> getSatisfiedDependencies() {
        Set<ComponentNode<?>> toReturn = new HashSet<>();

        for(int i = 0; i < this.parameters.length; i++) {
            if (this.dependencies[i] != null) {
                toReturn.add(this.dependencies[i]);
            }
        }

        return toReturn;
    }

    T getClassInstance() {
        if (this.isSingleton) {
            if (this.firstInstance == null) {
                try {
                    this.firstInstance = this.cons.newInstance(Arrays.stream(this.dependencies)
                            .map(ComponentNode::getClassInstance).toArray());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

            return this.firstInstance;
        } else {
            try {
                return this.cons.newInstance(Arrays.stream(this.dependencies)
                        .map(ComponentNode::getClassInstance).toArray());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
