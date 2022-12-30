package org.fpm.di.test2;

import org.fpm.di.Container;
import org.fpm.di.Environment;
import org.fpm.di.LabEnvironment;
import org.junit.Before;
import org.junit.Test;

public class UsageTest {
    private Container container;

    @Before
    public void setUp() {
        Environment env = new LabEnvironment();
        container = env.configure(new Test2Configuration());
    }

    @Test
    public void shouldInjectDifferentiateInstanceAndConstructor() {
        org.junit.Assert.assertNotEquals(container.getComponent(G.class), container.getComponent(F.class));
    }

    @Test
    public void shouldInterfaceBeASingleton() {
        org.junit.Assert.assertSame(container.getComponent(B.class), container.getComponent(B.class));
    }

    @Test
    public void shouldImplicitlyRegisterClassCAsD() {
        org.junit.Assert.assertEquals(container.getComponent(C.class).getClass()
                , container.getComponent(D.class).getClass());
    }

    @Test
    public void shouldRegisterAbstractFAsG() {
        org.junit.Assert.assertEquals(container.getComponent(F.class).getClass()
                , container.getComponent(G.class).getClass());
    }

    @Test
    public void shouldRegisterClassAWithConstructorAndNotAsE() {
        org.junit.Assert.assertNotEquals(container.getComponent(A.class).getClass()
                , container.getComponent(E.class).getClass());
    }
}
