package org.fpm.di;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

class ConstructorComponentNode<T> extends ComponentNode<T> {
    private final Constructor<T> cons;

    private final Parameter[] parameters;

    private final ComponentNode<?>[] dependencies;

    private final boolean isSingleton;

    private T firstInstance = null;

    ConstructorComponentNode(Class<T> clazz)
            throws ConstructorNotFoundException {
        super(clazz);
        Constructor<?> toInject;
        String message = clazz.getName().concat(": has no injectable constructors");
        Constructor<?>[] consPresent = clazz.getConstructors();
        List<Constructor<?>> consAnnotated = Arrays.stream(consPresent)
                .filter((cons) -> cons.isAnnotationPresent(Inject.class))
                .toList();
        if (consAnnotated.size() == 1) {
            toInject = consAnnotated.get(0);
        } else if (consPresent.length == 1) {
            if (consPresent[0].getParameterCount() == 0) {
                toInject = consPresent[0];
            } else {
                throw new ConstructorNotFoundException(message);
            }
        } else {
            throw new ConstructorNotFoundException(message);
        }
        this.cons = (Constructor<T>) toInject;
        this.parameters = toInject.getParameters();
        this.dependencies = new ComponentNode[this.parameters.length];
        this.isSingleton = clazz.isAnnotationPresent(Singleton.class);
    }

    @Override
    <R> boolean isDependentOn(Class<R> clazz) {
        Objects.requireNonNull(clazz, "clazz must be non null");
        for (Parameter parameter : this.parameters) {
            if (parameter.getType().equals(clazz)) return true;
        }
        return false;
    }

    @Override
    <R> void satisfyDependency(ComponentNode<R> newNode) {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType().equals(newNode.getType())) {
                dependencies[i] = newNode;
            }
        }
    }

    @Override
    Set<Class<?>> getUnsatisfiedDependencies() {
        Set<Class<?>> toReturn = new HashSet<>();
        for (int i = 0; i < parameters.length; i++) {
            if (dependencies[i] == null) {
                toReturn.add(parameters[i].getType());
            }
        }
        return toReturn;
    }

    @Override
    Set<ComponentNode<?>> getSatisfiedDependencies() {
        Set<ComponentNode<?>> toReturn = new HashSet<>();
        for (int i = 0; i < parameters.length; i++) {
            if (dependencies[i] != null) {
                toReturn.add(dependencies[i]);
            }
        }
        return toReturn;
    }

    @Override
    T getClassInstance() {
        if (isSingleton) {
            if (firstInstance == null) {
                try {
                    firstInstance = this.cons.newInstance(Arrays.stream(this.dependencies)
                            .map(ComponentNode::getClassInstance).toArray());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            return firstInstance;
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
