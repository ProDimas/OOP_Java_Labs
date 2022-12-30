package org.fpm.di;

import java.util.*;
import java.util.stream.Collectors;

class LabDependencyGraph {
    private final Set<ComponentNode<?>> listOfNodes;

    private final Set<Class<?>> unsatisfied;

    LabDependencyGraph() {
        listOfNodes = new HashSet<>();
        unsatisfied = new HashSet<>();
    }

    <T> void placeNode(ComponentNode<T> newNode) {
        this.listOfNodes.add(newNode);
    }

    <T> void setAvailableDependencies(ComponentNode<T> newNode) {
        for (ComponentNode<?> node : this.listOfNodes) {
            if (newNode.isDependentOn(node.getType())) {
                newNode.satisfyDependency(node);
            }
        }
    }

    <T> void satisfyDependentOn(ComponentNode<T> node) {
        listOfNodes.stream().filter((n) -> n.isDependentOn(node.getType())).forEach((n) -> n.satisfyDependency(node));
    }

    <T> List<ComponentNode<? extends T>> getNodesOfExtendingClasses(Class<T> clazz) {
        List<ComponentNode<? extends T>> toReturn = new LinkedList<>();
        for (ComponentNode<?> node : this.listOfNodes) {
            if (clazz.isAssignableFrom(node.getType())) {
                toReturn.add((ComponentNode<? extends T>) node);
            }
        }
        return toReturn;
    }

    <T> Optional<ComponentNode<T>> getNodeOf(Class<T> clazz) {
        return this.listOfNodes.stream()
                .filter((node) -> node.getType().equals(clazz))
                .map((node) -> (ComponentNode<T>) node)
                .findFirst();
    }

    <T> void registerUnsatisfiedDependency(Class<T> clazz) {
        this.unsatisfied.add(clazz);
    }

    Set<Class<?>> getUnsatisfied() {
        return this.unsatisfied;
    }

    void checkOnCyclicDependencies() {
        for (ComponentNode<?> node : listOfNodes) {
            Set<ComponentNode<?>> toInspect = node.getSatisfiedDependencies();
            while (!toInspect.isEmpty()) {
                if (toInspect.stream().anyMatch((n) -> n.isDependentOn(node.getType()))) {
                    throw new CyclicDependencyException(node.getType().getName().concat(": depends on itself"));
                }
                toInspect = toInspect.stream()
                        .flatMap((n) -> n.getSatisfiedDependencies().stream())
                        .collect(Collectors.toSet());
            }
        }
    }

    <T> ComponentNode<T> searchForComponent(Class<T> clazz) {
        return listOfNodes.stream()
                .filter((node) -> node.getType().equals(clazz))
                .map((node) -> (ComponentNode<T>) node)
                .findFirst().orElseThrow(() -> {
                    throw new NoSuchElementException(clazz.getName()
                            .concat(": has not been registered in container"));
                });
    }
}
