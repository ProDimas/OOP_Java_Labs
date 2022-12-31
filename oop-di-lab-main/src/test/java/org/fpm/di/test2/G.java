package org.fpm.di.test2;

import javax.inject.Inject;

public class G extends F {
    private final A aInst;

    public G() {
        this.aInst = null;
    }

    @Inject
    public G(A aInst) {
        this.aInst = aInst;
    }
}
