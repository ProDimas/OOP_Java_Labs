package org.fpm.di.test3;

import org.fpm.di.BindingException;
import org.fpm.di.LabEnvironment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UsageTest3 {
    @Test
    public void shouldThrowBindException() {
        Assertions.assertThrows(BindingException.class, () -> new LabEnvironment().configure(new Test3Configuration()));
    }
}
