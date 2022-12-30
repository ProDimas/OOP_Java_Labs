package org.fpm.di.test1;

import javax.inject.Inject;

public class A {
    private B bInst;
    private C cInst;

    @Inject
    public A(B bInst, C cInst) {
        this.bInst = bInst;
        this.cInst = cInst;
    }

    @Override
    public String toString() {
        return "It's A of ".concat(bInst.toString()).concat(" + ").concat(cInst.toString());
    }
}
