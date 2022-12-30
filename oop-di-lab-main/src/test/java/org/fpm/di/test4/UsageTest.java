package org.fpm.di.test4;

import org.fpm.di.Container;
import org.fpm.di.Environment;
import org.fpm.di.LabEnvironment;
import org.junit.Before;
import org.junit.Test;

public class UsageTest {
    Container container;

    @Before
    public void setUp() {
        Environment env = new LabEnvironment();
        container = env.configure(new Test4Configuration());
    }

    @Test
    public void shouldRegisterInnerDependencies() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> container.getComponent(D.class));
    }
}
