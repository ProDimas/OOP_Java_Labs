package org.fpm.di.test2;

import org.fpm.di.Binder;
import org.fpm.di.Configuration;

public class Test2Configuration implements Configuration {
    public void configure(Binder binder) {
        binder.bind(B.class, C.class);
        binder.bind(D.class);
        binder.bind(G.class);
        binder.bind(F.class, new G());
    }
}
