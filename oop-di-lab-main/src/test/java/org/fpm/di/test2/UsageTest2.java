package org.fpm.di.test2;

import org.fpm.di.Container;
import org.fpm.di.Environment;
import org.fpm.di.LabEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UsageTest2 {
    private Container container;

    @Before
    public void setUp() {
        Environment env = new LabEnvironment();
        this.container = env.configure(new Test2Configuration());
    }

    @Test
    public void shouldInjectDifferentiateInstanceAndConstructor() {
        Assertions.assertNotEquals(this.container.getComponent(G.class), this.container.getComponent(F.class));
    }

    @Test
    public void shouldInterfaceBeASingleton() {
        Assertions.assertSame(this.container.getComponent(B.class), this.container.getComponent(B.class));
    }

    @Test
    public void shouldImplicitlyRegisterClassCAsD() {
        Assertions.assertEquals(this.container.getComponent(C.class).getClass(),
                this.container.getComponent(D.class).getClass());
    }

    @Test
    public void shouldRegisterAbstractFAsGInstance() {
        Assertions.assertEquals(this.container.getComponent(F.class).getClass(),
                this.container.getComponent(G.class).getClass());
    }

    @Test
    public void shouldRegisterClassAWithConstructorAndNotAsE() {
        Assertions.assertNotEquals(this.container.getComponent(A.class).getClass(),
                this.container.getComponent(E.class).getClass());
    }
}
