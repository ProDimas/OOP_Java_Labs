package org.fpm.di.test1;

import org.fpm.di.*;
import org.junit.Before;
import org.junit.Test;

public class UsageTest {
    private Container container;

    @Before
    public void setUp() {
        Environment env = new LabEnvironment();
        container = env.configure(new Test1Configuration());
    }

    @Test
    public void shouldInjectPrototype() {
        org.junit.Assert.assertNotSame(container.getComponent(A.class), container.getComponent(A.class));
        org.junit.Assert.assertNotSame(container.getComponent(B.class), container.getComponent(B.class));
    }

    @Test
    public void shouldInjectSingleton() {
        org.junit.Assert.assertSame(container.getComponent(C.class), container.getComponent(C.class));
    }

    @Test
    public void shouldGetDifferentInstancesOfDifferentClasses() {
        org.junit.Assert.assertNotSame(container.getComponent(A.class), container.getComponent(B.class));
        org.junit.Assert.assertNotSame(container.getComponent(A.class), container.getComponent(C.class));
        org.junit.Assert.assertNotSame(container.getComponent(B.class), container.getComponent(C.class));
    }
}
