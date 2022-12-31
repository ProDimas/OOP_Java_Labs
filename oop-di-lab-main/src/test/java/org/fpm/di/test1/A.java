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

    public String toString() {
        return "It's A of ".concat(this.bInst.toString()).concat(" + ").concat(this.cInst.toString());
    }
}
