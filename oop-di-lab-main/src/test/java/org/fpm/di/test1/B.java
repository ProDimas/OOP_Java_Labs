package org.fpm.di.test1;

import javax.inject.Inject;

public class B extends A {
    private C cInst;

    @Inject
    public B(C cInst) {
        super((B)null, cInst);
        this.cInst = cInst;
    }

    public String toString() {
        return "B of ".concat(this.cInst.toString());
    }
}
