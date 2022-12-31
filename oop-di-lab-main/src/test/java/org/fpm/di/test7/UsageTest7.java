package org.fpm.di.test7;

import org.fpm.di.Container;
import org.fpm.di.Environment;
import org.fpm.di.LabEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Vector;

public class UsageTest7 {
    private Container container;

    @Before
    public void setUp() {
        Environment env = new LabEnvironment();
        container = env.configure(new Test7Configuration());
    }

    @Test
    public void shouldRegisterStandardLibraryClasses() {
        Assertions.assertEquals(container.getComponent(C.class).getVector(), container.getComponent(Vector.class));
    }
}
