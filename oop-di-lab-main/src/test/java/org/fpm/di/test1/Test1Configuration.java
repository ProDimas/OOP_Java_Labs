package org.fpm.di.test1;

import org.fpm.di.Binder;
import org.fpm.di.Configuration;

public class Test1Configuration implements Configuration {
    @Override
    public void configure(Binder binder) {
        binder.bind(A.class);
        binder.bind(C.class);
        binder.bind(B.class);
    }
}
