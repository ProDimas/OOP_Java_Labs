package org.fpm.di.test5;

import javax.inject.Inject;

public class B extends A {
    @Inject
    public B(C cInst) {
    }
}
