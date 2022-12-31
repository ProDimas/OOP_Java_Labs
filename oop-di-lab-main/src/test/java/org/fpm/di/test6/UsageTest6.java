package org.fpm.di.test6;

import org.fpm.di.LabEnvironment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UsageTest6 {
    @Test
    public void shouldThrowNullPointerException() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new LabEnvironment().configure(new Test6Configuration()));
    }
}
