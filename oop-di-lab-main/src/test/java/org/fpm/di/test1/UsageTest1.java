package org.fpm.di.test1;

import org.fpm.di.Container;
import org.fpm.di.Environment;
import org.fpm.di.LabEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UsageTest1 {
    private Container container;

    @Before
    public void setUp() {
        Environment env = new LabEnvironment();
        this.container = env.configure(new Test1Configuration());
    }

    @Test
    public void shouldInjectPrototype() {
        Assertions.assertNotSame(this.container.getComponent(A.class), this.container.getComponent(A.class));
        Assertions.assertNotSame(this.container.getComponent(B.class), this.container.getComponent(B.class));
    }

    @Test
    public void shouldInjectSingleton() {
        Assertions.assertSame(this.container.getComponent(C.class), this.container.getComponent(C.class));
    }

    @Test
    public void shouldGetDifferentInstancesOfDifferentClasses() {
        Assertions.assertNotSame(this.container.getComponent(A.class), this.container.getComponent(B.class));
        Assertions.assertNotSame(this.container.getComponent(A.class), this.container.getComponent(C.class));
        Assertions.assertNotSame(this.container.getComponent(B.class), this.container.getComponent(C.class));
    }
}
