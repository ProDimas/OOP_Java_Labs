package org.fpm.di.test4;

import org.fpm.di.Binder;
import org.fpm.di.Configuration;

public class Test4Configuration implements Configuration {
    @Override
    public void configure(Binder binder) {
        binder.bind(D.class);
    }
}
