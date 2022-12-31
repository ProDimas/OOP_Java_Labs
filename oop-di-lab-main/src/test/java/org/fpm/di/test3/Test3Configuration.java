package org.fpm.di.test3;

import org.fpm.di.Binder;
import org.fpm.di.Configuration;

public class Test3Configuration implements Configuration {
    public void configure(Binder binder) {
        binder.bind(X.class);
    }
}
