package org.fpm.di.test5;

import org.fpm.di.Binder;
import org.fpm.di.Configuration;

public class Test5Configuration implements Configuration {
    public void configure(Binder binder) {
        binder.bind(A.class, B.class);
    }
}
