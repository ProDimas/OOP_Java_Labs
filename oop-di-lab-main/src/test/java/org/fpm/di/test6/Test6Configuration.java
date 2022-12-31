package org.fpm.di.test6;

import org.fpm.di.Binder;
import org.fpm.di.Configuration;

public class Test6Configuration implements Configuration {
    @Override
    public void configure(Binder binder) {
        binder.bind(null);
        binder.bind(null, new Object());
    }
}
