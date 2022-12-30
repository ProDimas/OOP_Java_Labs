package org.fpm.di.test2;

import javax.inject.Inject;

public class G extends F {
    private A aInst;

    public G() {
        aInst = null;
    }

    @Inject
    public G(A aInst) { this.aInst = aInst; }
}
