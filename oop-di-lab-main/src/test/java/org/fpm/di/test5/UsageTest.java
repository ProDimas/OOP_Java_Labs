package org.fpm.di.test5;

import org.fpm.di.Container;
import org.fpm.di.CyclicDependencyException;
import org.fpm.di.Environment;
import org.fpm.di.LabEnvironment;
import org.junit.Test;

public class UsageTest {
    @Test
    public void shouldThrowCyclicDependencyException() {
        org.junit.jupiter.api.Assertions.assertThrows(CyclicDependencyException.class
                , () -> new LabEnvironment().configure(new Test5Configuration()));
    }
}
