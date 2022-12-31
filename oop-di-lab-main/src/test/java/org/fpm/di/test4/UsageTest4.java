package org.fpm.di.test4;

import org.fpm.di.Container;
import org.fpm.di.Environment;
import org.fpm.di.LabEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UsageTest4 {
    Container container;

    public UsageTest4() {
    }

    @Before
    public void setUp() {
        Environment env = new LabEnvironment();
        this.container = env.configure(new Test4Configuration());
    }

    @Test
    public void shouldRegisterInnerDependencies() {
        Assertions.assertDoesNotThrow(() -> this.container.getComponent(D.class));
    }
}
