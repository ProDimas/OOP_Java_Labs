package org.fpm.di.test7;

import javax.inject.Inject;
import java.util.Vector;

public class C implements B {
    private final Vector<?> vecInst;
    @Inject
    public C(Vector<?> vecInst) { this.vecInst = vecInst; }

    public Vector<?> getVector() { return this.vecInst; }
}
