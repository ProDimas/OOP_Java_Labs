package org.fpm.di.test3;

import org.fpm.di.*;
import org.junit.Test;

public class UsageTest {
    @Test
    public void shouldThrowAnBindException() {
        org.junit.Assert.assertThrows(BindingException.class, () -> {
            Container container = new LabEnvironment().configure(new Test3Configuration());
        });
    }
}
