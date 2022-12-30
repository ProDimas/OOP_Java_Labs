package org.fpm.di.test2;

import javax.inject.Inject;

public class D extends C {
    @Inject
    public D(E eInst, A aInst) {
        super(eInst);
    }
}
