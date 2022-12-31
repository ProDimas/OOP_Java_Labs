package org.fpm.di.test5;

import org.fpm.di.CyclicDependencyException;
import org.fpm.di.LabEnvironment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UsageTest5 {
    @Test
    public void shouldThrowCyclicDependencyException() {
        Assertions.assertThrows(CyclicDependencyException.class, () -> {
            (new LabEnvironment()).configure(new Test5Configuration());
        });
    }
}
